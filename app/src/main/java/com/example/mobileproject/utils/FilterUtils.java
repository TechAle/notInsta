package com.example.mobileproject.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public final class FilterUtils {

    public static Bitmap applyFilter(Bitmap bitmap, String filter, int x, int y, int z) {
        Bitmap filtered = null;

        switch (filter) {
            case "hue":
                filtered = changeHue(bitmap, x, y, z);
                break;
            case "brightness":
                filtered = regulateBrightness(bitmap, z);
                break;
            case "contrast":
                filtered = enhanceContrast(bitmap, z);
                break;
            case "saturation":
                filtered = changeSaturation(bitmap, z);
                break;
            case "pixelate":
                filtered = pixelate(bitmap, z);
                break;
        }

        return filtered;
    }

    // Applies a ColorMatrix to a bitmap
    private static Bitmap getBitmapFromColorMatrix (ColorMatrix cm, Bitmap bitmap) {
        Bitmap out = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(out);
        Paint paint = new Paint();

        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return out;
    }



    //------- FILTERS ---//


    // Changes hue
    private static Bitmap changeHue(Bitmap bitmap, int r, int g, int b) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                1, 0, 0, 0, r,
                0, 1, 0, 0, g,
                0, 0, 1, 0, b,
                0, 0, 0, 1, 0
        });

        return getBitmapFromColorMatrix(matrix, bitmap);
    }


    // Regulates brightness
    private static Bitmap regulateBrightness(Bitmap bitmap, int x) {
        float value = x * 2 - 255;

        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                1, 0, 0, 0, value,
                0, 1, 0, 0, value,
                0, 0, 1, 0, value,
                0, 0, 0, 1, 0
        });

        return getBitmapFromColorMatrix(matrix, bitmap);
    }


    // Enhances contrast
    private static Bitmap enhanceContrast(Bitmap bitmap, int x) {
        float value = (float) x / 255;
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]
            {
                    value,0,0,0, 0.5f*255*(value+1),
                    0,value,0,0, 0.5f*255*(value+1),
                    0,0,value,0, 0.5f*255*(value+1),
                    0,0,0,1,0
            });

        return getBitmapFromColorMatrix(matrix, bitmap);
    }


    // Changes saturation
    private static Bitmap changeSaturation(Bitmap bitmap, int value) {
        float x = value / 25.5f;
        float lumR = 0.3086f;
        float lumG = 0.6094f;
        float lumB = 0.0820f;

        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]
            {
                    lumR*(1-x)+x,lumG*(1-x),lumB*(1-x),0,0,
                    lumR*(1-x),lumG*(1-x)+x,lumB*(1-x),0,0,
                    lumR*(1-x),lumG*(1-x),lumB*(1-x)+x,0,0,
                    0,0,0,1,0,
                    0,0,0,0,1
            });

        return getBitmapFromColorMatrix(matrix, bitmap);
    }


    // Pixelates the image
    private static Bitmap pixelate(Bitmap bitmap, int x) {
        int value = x / 15;
        if (value == 0) {
            value = 1;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, width / value, height / value, false);

        return Bitmap.createScaledBitmap(resizedBitmap, width, height, false);
    }
}
