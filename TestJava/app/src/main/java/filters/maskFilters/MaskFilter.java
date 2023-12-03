package filters.maskFilters;

import android.graphics.Bitmap;
import android.graphics.Color;
import filters.Filter;
import filters.ImgUtils;

public class MaskFilter extends Filter {

    //Attributes
    protected int maskLength;
    protected int[] cache;

    public MaskFilter(Bitmap bm) {
        super(bm);
    }

    // Applies the filter
    public Bitmap apply(int maskLength) {
        this.maskLength = maskLength;
        int[][] mask = getMask();
        int[] filtered = loop(mask);

        return Bitmap.createBitmap(filtered, width, height, Bitmap.Config.ARGB_8888);
    }



    //--- PRIVATE METHODS -------//

    // Loop for applying the filter
    protected int[] loop(int[][] arr) {
        int[] filtered = pixels.clone();

        // It moves the mask like a snake, in order to minimize cache update
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int idx = width * y + x;
                filtered[idx] = calculatePixelValue(pixels[idx], x, y, arr);
            }
            x++;
            // Comes back
            for (int y = height - 1; y >= 0; y--) {
                int idx = width * y + x;
                filtered[idx] = calculatePixelValue(pixels[idx], x, y, arr);
            }
        }

        return filtered;
    }

    // Shifts the cache on X axis of one column
    protected void shiftCacheX() {
        
    }

    // Calculates the new pixel based on the mask
    protected int calculatePixelValue(int pixel, int x, int y, int[][] mask) {
        int[] rgb = ImgUtils.getRGB(pixel);

        return 0;
    }

    // Creates the mask for filter application
    protected int[][] getMask() {
        return new int[0][0];
    }
}
