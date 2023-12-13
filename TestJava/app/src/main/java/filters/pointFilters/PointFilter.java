package filters.pointFilters;

import android.graphics.Bitmap;
import android.graphics.Color;
import filters.Filter;
import filters.ImgUtils;

public class PointFilter extends Filter {

    public PointFilter(Bitmap bm) {
        super(bm);
    }

    // Applies the filter
    public Bitmap apply() {
        int[] lookupTable = getLookupTable();
        int[] filtered = loop(lookupTable);

        return Bitmap.createBitmap(filtered, width, height, Bitmap.Config.ARGB_8888);
    }



    //--- PRIVATE METHODS --------//

    // Loop for applying the filter
    protected int[] loop(int[] arr) {
        int[] filtered = pixels.clone();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int idx = width * y + x;
                filtered[idx] = calculatePixelValue(pixels[idx], arr);
            }
        }

        return filtered;
    }

    // Calculates the new pixel based on the lookup table
    protected int calculatePixelValue(int pixel, int[] lookupTable) {
        int[] rgb = ImgUtils.getRGB(pixel);
        int r = lookupTable[rgb[0]];
        int g = lookupTable[rgb[1] + 255];
        int b = lookupTable[rgb[2] + 512];

        return Color.rgb(r, g, b);
    }

    // Creates the lookup table for filter application
    protected int[] getLookupTable() {
        return new int[0];
    }
}
