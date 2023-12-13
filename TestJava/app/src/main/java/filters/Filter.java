package filters;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Filter {
    // Attributes
    protected int width;
    protected int height;
    protected int[] pixels;


    // Contructor
    public Filter(Bitmap bm){
        width = bm.getWidth();
        height = bm.getHeight();
        pixels = new int[width * height];
        bm.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    // Applies the filter
    public Bitmap apply() {
        return null;
    }



    //--- PRIVATE METHODS -------//

    // Loop for applying the filter
    protected int[] loop(int[] arr) {
        return new int[0];
    }

    // Calculates the new pixel based on the lookup table
    protected int calculatePixelValue(int pixel, int[] lookupTable) {
        return 0;
    }

}
