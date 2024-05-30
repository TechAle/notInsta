package com.example.mobileproject.UI.Camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.mobileproject.R;
import com.example.mobileproject.service.SyncLTRWorker;
import com.example.mobileproject.utils.Result;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;

public final class CameraActivity extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    int TAKE_PICTURE = 100;
    private ImageView imageView;
    private Button nextButton;
    private Button prevButton;
    private Bitmap photo;
    private ProcessedImageViewModel viewModel;
    private int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        
        currentFragment = 0;
        changeFragment();

        viewModel = new ViewModelProvider(this).get(ProcessedImageViewModel.class);
        recover();
        final Observer<Bitmap> observer = bitmap -> imageView.setImageBitmap(bitmap);
        viewModel.getTempImage().observe(this, observer);
        nextButton.setOnClickListener(view -> {
            currentFragment++;
            changeFragment();
        });
        prevButton.setOnClickListener(view -> {
            currentFragment--;
            changeFragment();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            if (requestCode == SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                    InputStream inputStream;
                    try {
                        inputStream = getContentResolver().openInputStream(selectedImageUri);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    photo = BitmapFactory.decodeStream(inputStream);
                }
            } else {
                Bundle b = data.getExtras();
                if(b == null){
                    photo = null;
                } else {
                    photo = (Bitmap) b.get("data");
                }
            }
            //imageView.setImageBitmap(photo);
            viewModel.getProcessedImage().setValue(photo);
            viewModel.getTempImage().setValue(photo);
            viewModel.reset();
        }
    }


    // PRIVATE METHODS //

    // Recovers from activity change
    private void recover() {
        if (viewModel.getProcessedImage() != null) {
            Bitmap image = viewModel.getProcessedImage().getValue();
            imageView.setImageBitmap(image);
        }
    }

    // Substitutes the fragment depending on the current state
    //TODO: Gestire qua un possibile NullPointerException (clicco 'avanti' senza aver selezionato un immagine)
    private void changeFragment() {
        switch (currentFragment){
            case -1:
                finish();
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, ImageChooserFragment.class, null)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, ImageViewerFragment.class, null)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, PostDescriptionFragment.class, null)
                        .commit();
                break;
            case 3:
                // Send post
                viewModel.postImage(this).observe(this, result -> {
                    String text = "";
                    if(result.successful()){
                        Result.PostCreationSuccess.ResponseType type = ((Result.PostCreationSuccess) result).getData();
                        switch(type){
                            case SUCCESS:
                                text = getResources().getString(R.string.post_created);
                                break;
                            case LOCAL:
                                Constraints constraints = new Constraints.Builder()
                                        .setRequiredNetworkType(NetworkType.CONNECTED)
                                        .build();
                                OneTimeWorkRequest syncWorkRequest =
                                        new OneTimeWorkRequest.Builder(SyncLTRWorker.class)
                                                .setConstraints(constraints)
                                                .build();
                                WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork("LTR", ExistingWorkPolicy.KEEP, syncWorkRequest);
                                text = getResources().getString(R.string.post_created_local_only);
                                break;
                            case REMOTE:
                                text = getResources().getString(R.string.post_created_remote_only);
                                break;
                            case NO_REMOTE_IMAGE: //spero non esca mai
                                text = getResources().getString(R.string.post_created_no_remote_image);
                                break;
                        }
                    } else {
                        text = getResources().getString(R.string.post_not_created);
                    }
                    Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, ImageChooserFragment.class, null)
                            .commit();
                    currentFragment = 0;
                });
                break;
        }
    }

}
