package com.example.testjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import filters.FilterManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button BSelectImage;

    ImageView IVPreviewImage;
    int[] pixels;
    int width;
    int height;
    Bitmap bitmap;

    int SELECT_PICTURE = 200;
    private final Map<String, Object> filtri;


    public MainActivity() {
        filtri = new HashMap<>();
        filtri.put("RGB", Integer.MIN_VALUE);
        filtri.put("Grigio", false);
        filtri.put("Contrasto", Integer.MIN_VALUE);
        filtri.put("Blur", Integer.MIN_VALUE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        BSelectImage.setOnClickListener(v -> imageChooser());
        findViewById(R.id.rgbButton).setOnClickListener(v -> changeRGB());
        findViewById(R.id.grigioButton).setOnClickListener(v -> changeGray());
        findViewById(R.id.contrastoButton).setOnClickListener(v -> changeContrasto());
        findViewById(R.id.blurButton).setOnClickListener(v -> changeBlur());
        System.out.println("created");
    }

    void changeBlur() {
        int value = (int) filtri.get("Blur");
        if (value == Integer.MIN_VALUE) {
            value = Integer.parseInt(((TextView) findViewById(R.id.blurInput)).getText().toString());
        } else value = Integer.MIN_VALUE;
        filtri.put("Blur", value);

        filterAndDisplay("Blur");
    }

    void changeContrasto() {
        int value = (int) filtri.get("Contrasto");
        if (value == Integer.MIN_VALUE) {
            value = Integer.parseInt(((TextView) findViewById(R.id.contrastoInput)).getText().toString());
        } else value = Integer.MIN_VALUE;
        filtri.put("Contrasto", value);

        filterAndDisplay("Contrasto");
    }

    void changeRGB() {
        System.out.println("event fired");
        int value = (int) filtri.get("RGB");
        System.out.println("working");
        if (value == Integer.MIN_VALUE) {
            value = Color.argb(255,
                    Integer.parseInt(((TextView) findViewById(R.id.redInput)).getText().toString()),
                    Integer.parseInt(((TextView) findViewById(R.id.greenInput)).getText().toString()),
                    Integer.parseInt(((TextView) findViewById(R.id.blueInput)).getText().toString()));

        } else value = Integer.MIN_VALUE;
        filtri.put("RGB", value);

        bitmap = FilterManager.colorize(bitmap, 100, 0, 0);
        IVPreviewImage.setImageBitmap(bitmap);
    }

    void changeGray() {
        filtri.put("Grigio", !((boolean) filtri.get("Grigio")));

        bitmap = FilterManager.gammaCorrection(bitmap, 3);
        IVPreviewImage.setImageBitmap(bitmap);
    }

    // the Select Image Button is clicked
    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                    InputStream inputStream;
                    try {
                        inputStream = getContentResolver().openInputStream(selectedImageUri);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    width = bitmap.getWidth();
                    height = bitmap.getHeight();
                    pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

                    IVPreviewImage.setImageBitmap(bitmap);
                }
            }
        }
    }


    public void filterAndDisplay(String filtro) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int idx = width * y + x;
                pixels[idx] = FilterEngine.addFiltro(pixels, width, height, idx, x, y, filtro, filtri.get(filtro));
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);

        // update the preview image in the layout
        IVPreviewImage.setImageBitmap(bitmap);
    }

}