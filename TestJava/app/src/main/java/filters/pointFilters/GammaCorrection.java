package filters.pointFilters;

import android.graphics.Bitmap;
import android.graphics.Color;
import filters.ImgUtils;

public class GammaCorrection extends PointFilter{

    // Attributes
    double gamma;

    public GammaCorrection(Bitmap bm) {
        super(bm);
    }

    // Override apply
    public Bitmap apply(double gamma) {
        this.gamma = gamma;
        return super.apply();
    }



    //--- PRIVATE METHODS -------//

    protected int[] getLookupTable() {
        int[] lt = new int[256];
        for (int i = 0; i < 256; i++){
            lt[i] = ImgUtils.clipRGB((int) (Math.pow(((double) i / 255), gamma) * 255));
        }

        return lt;
    }

    // Override in order to shorten the table
    protected int calculatePixelValue(int pixel, int[] lookupTable) {
        int[] rgb = ImgUtils.getRGB(pixel);
        int r = lookupTable[rgb[0]];
        int g = lookupTable[rgb[1]];
        int b = lookupTable[rgb[2]];

        return Color.rgb(r, g, b);
    }
}
