package filters;

import android.graphics.Color;

// Class containing static utils methods for image processing
public class ImgUtils {

    // Gets the RGB value of a pixel
    public static int[] getRGB(int pixel) {
        int[] rgb = new int[3];
        rgb[0] = Color.red(pixel);
        rgb[1] = Color.green(pixel);
        rgb[2] = Color.blue(pixel);

        return rgb;
    }

    // Clips the rgb values and returns the pixel
    public static int clipRGB(int[] values) {
        int[] rgb = values.clone();
        for (int i = 0; i < 3; i++) {
            if (rgb[i] < 0) rgb[i] = 0;
            if (rgb[i] > 255) rgb[i] = 255;
        }

        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }

    // Clips single channel value
    public static int clipRGB(int value) {
        if (value < 0) return 0;
        if (value > 255) return 255;
        return value;
    }
}
