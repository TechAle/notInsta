package com.example.testjava.utils.imageProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

import filters.ImgUtils;

public class FilterManager {

    public static Bitmap changeHue(Bitmap bitmap, int r, int g, int b) {
        Bitmap filtered = test(bitmap, r, g, b);
        return filtered;
    }


    //------- TEST ---//

    private static Bitmap test(Bitmap bm, int r, int g, int b) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int[] pixels = new int[width * height];
        bm.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] filtered = pixels.clone();
        int[] lookupTable = getLookupTable(r, g, b);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int idx = width * y + x;
                filtered[idx] = calculatePixelValue(pixels[idx], lookupTable);
            }
        }

        return Bitmap.createBitmap(filtered, width, height, Bitmap.Config.ARGB_8888);
    }

    private static int calculatePixelValue(int pixel, int[] lookupTable) {
        int[] rgb = ImgUtils.getRGB(pixel);
        int r = lookupTable[rgb[0]];
        int g = lookupTable[rgb[1] + 255];
        int b = lookupTable[rgb[2] + 512];

        return Color.rgb(r, g, b);
    }

    private static int[] getLookupTable(int red, int green, int blue) {
        int[] lt = new int[256 * 3];
        for (int i = 0; i < 256; i++){
            lt[i] = ImgUtils.clipRGB(i + red);
            lt[i+256] = ImgUtils.clipRGB(i + green);
            lt[i+512] = ImgUtils.clipRGB(i + blue);
        }

        return lt;
    }
}
