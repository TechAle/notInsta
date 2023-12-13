package com.example.testjava;


import android.graphics.Color;

public class FilterEngine {


    // Applies a filter to the image
    public static int addFiltro(int[] pixels, int width, int height, int idx, int x, int y, String filtro, Object params) {
        int pixel = pixels[idx];
        int alpha = Color.alpha(pixel);
        int red = Color.red(pixel);
        int green = Color.green(pixel);
        int blue = Color.blue(pixel);

        switch (filtro) {
            case "Grigio":
                return grayscale(alpha, red, green, blue);
            case "RGB":
                return colorize(alpha, red, green, blue, (int) params);
            case "Contrasto":
                return sharpen(alpha, red, green, blue, width, height, x, y, pixels, (int) params);
            case "Blur":
                return blur(alpha, pixel, width, height, x, y, pixels, (int) params);
        }

        return -1;
    }



    //--- PRIVATE METHODS -------//


    // Returns a pixel with increased value based on vector
    private static int colorize(int a, int r, int g, int b, int vector){
        int redNew = Color.red(vector) + r;
        int greenNew = Color.green(vector) + g;
        int blueNew = Color.blue(vector) + b;

        // Keeps values between 0 and 255
        if (redNew < 0) redNew = 0;
        if (redNew > 255) redNew = 255;
        if (greenNew < 0) greenNew = 0;
        if (greenNew > 255) greenNew = 255;
        if (blueNew < 0) blueNew = 0;
        if (blueNew > 255) blueNew = 255;

        return Color.argb(a, redNew, greenNew, blueNew);
    }


    // Returns a gray pixel
    private static int grayscale(int a, int r, int g, int b){
        int gray = (r + g + b) / 3;

        return Color.argb(a, gray, gray, gray);
    }


    // Blurs the pixels based on surroundings
    private static int blur(int a, int rgb, int width, int height, int x, int y, int[] pixels, int offset){
        int midR = 0;
        int midG = 0;
        int midB = 0;
        int count = 0;
        /*
        for (int z = x-offset; z < x+offset+1; z++) {
            for (int w = y-offset; w < y+offset+1; w++) {
                if (z < width && z >= 0 && w < height && w >= 0) {
                    int idx = width * w + z;
                    midR += Color.red(pixels[idx]);
                    midG += Color.green(pixels[idx]);
                    midB += Color.blue(pixels[idx]);
                    count++;
                }
            }
        }*/
        int test = 0;
        for (int z = x-offset; z < x+offset+1; z++) {
            if (z < width && z >= 0) {
                int px = pixels[width * y + z];
                midR += Color.red(px);
                midG += Color.green(px);
                midB += Color.blue(px);
                count++;
                test += px;
            }
        }
        for (int w = y-offset; w < y+offset+1; w++) {
            if (w < height && w >= 0 && w != y) {
                int px = pixels[width * w + x];
                midR += Color.red(px);
                midG += Color.green(px);
                midB += Color.blue(px);
                count++;
                test += px;
            }
        }

        int t = Color.red(test / count);
        int i = Color.green(test / count);
        int u = Color.blue(test / count);
        int r = midR / count;
        int g = midG / count;
        int b = midB / count;
        if (r < 0) r = 0;
        if (r > 255) r = 255;
        if (g < 0) g = 0;
        if (g > 255) g = 255;
        if (b < 0) b = 0;
        if (b > 255) b = 255;

        return Color.argb(a, r, g, b);
    }


    // Sharpens
    private static int sharpen(int a, int r, int g, int b, int width, int height, int x, int y, int[] pixels, int offset){
        int gray = (r + g + b) / 3;

        for (int z = x-1; z < x+1; z++) {
            for (int w = y-1; w < y+1; w++) {
                if (z < width && z >= 0 && w < height && w >= 0) {
                    int idx = width * w + z;
                    int g2 = (Color.red(pixels[idx]) + Color.green(pixels[idx]) + Color.blue(pixels[idx])) / 3;
                    if (g2 > gray + offset){
                        gray = 0;
                    };
                }
            }
        }

        return Color.argb(a, gray, gray, gray);
    }
}