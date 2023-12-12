package com.example.testjava.UI.post_creation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;


import com.example.testjava.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class TestActivity extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    int TAKE_PICTURE = 100;
    private ImageView imageView;
    private Bitmap photo;
    private ProcessedImageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        imageView = findViewById(R.id.imageView);
        viewModel = new ViewModelProvider(this).get(ProcessedImageViewModel.class);
        final Observer<Bitmap> observer = new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        };
        viewModel.getTempImage().observe(this, observer);
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
                photo = (Bitmap) data.getExtras().get("data");
            }
            //imageView.setImageBitmap(photo);
            viewModel.getProcessedImage().setValue(photo);
            viewModel.getTempImage().setValue(photo);
        }
    }
}