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

import com.example.mobileproject.R;
import com.example.mobileproject.dataLayer.repositories.PostManager;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;

public final class CameraActivity extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    int TAKE_PICTURE = 100;
    private ImageView imageView;
    private Bitmap photo;
    private ProcessedImageViewModel viewModel;
    private int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);
        Button nextButton = findViewById(R.id.next_button);
        Button prevButton = findViewById(R.id.prev_button);
        
        currentFragment = 0;
        changeFragment();
        PostManager pr = ServiceLocator.getInstance().getPostRepo(getApplicationContext());
        if(pr == null) return;
        viewModel = new ViewModelProvider(this, new ProcessedImageVMFactory(pr)).get(ProcessedImageViewModel.class);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    String text;
                    if(result.successful()){
                        text = getResources().getString(R.string.post_created);
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