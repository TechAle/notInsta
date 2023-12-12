package com.example.testjava.UI.post_creation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.testjava.R;

import java.util.Objects;


public class ImageFilterActivity extends AppCompatActivity {

    private String filter;
    private int numberOfSeekBars;
    private int[] settings;

    private ProcessedImageViewModel viewModel;

    // View Elements
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private ImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);
        Intent intent = getIntent();
        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap((Bitmap) intent.getParcelableExtra("image"));
        filter = "non";
        numberOfSeekBars = 1;
        settings = new int[0];
        viewModel = new ViewModelProvider(this).get(ProcessedImageViewModel.class);
        loadUI();
    }


    //------- PRIVATE METHODS ---//

    // Loads the UI according to the fragment's parameters
    private void loadUI() {
        if (!Objects.equals(filter, "none")) {
            switch (numberOfSeekBars) {
                case 1:
                    seekBar1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    seekBar1.setVisibility(View.VISIBLE);
                    seekBar2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    seekBar1.setVisibility(View.VISIBLE);
                    seekBar2.setVisibility(View.VISIBLE);
                    seekBar3.setVisibility(View.VISIBLE);
                    break;
            }
        }

        imageView.setImageBitmap(viewModel.getProcessedImage().getValue());
    }
}