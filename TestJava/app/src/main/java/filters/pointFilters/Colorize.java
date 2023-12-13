package filters.pointFilters;

import android.graphics.Bitmap;
import filters.ImgUtils;

// Modifies the RGB channels of the image
public class Colorize extends PointFilter {

    // Attributes
    int red;
    int green;
    int blue;


    // Constructor
    public Colorize(Bitmap bm) {
        super(bm);
    }

    // Override apply
    public Bitmap apply(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        return super.apply();
    }

    //--- PRIVATE METHODS ---//

    protected int[] getLookupTable() {
        int[] lt = new int[256 * 3];
        for (int i = 0; i < 256; i++){
            lt[i] = ImgUtils.clipRGB(i + red);
            lt[i+256] = ImgUtils.clipRGB(i + green);
            lt[i+512] = ImgUtils.clipRGB(i + blue);
        }

        return lt;
    }
}
