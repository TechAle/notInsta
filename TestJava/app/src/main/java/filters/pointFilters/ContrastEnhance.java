package filters.pointFilters;

import android.graphics.Bitmap;
import android.graphics.Color;
import filters.ImgUtils;

public class ContrastEnhance extends PointFilter{

    // Attributes
    double lambda;
    final double e = 2.7182818284590452353602874713527;

    public ContrastEnhance(Bitmap bm) {
        super(bm);
    }

    // Override apply
    public Bitmap apply(double lambda) {
        this.lambda = lambda;
        return super.apply();
    }



    //--- PRIVATE METHODS -------//

    protected int[] getLookupTable() {
        int[] lt = new int[256];
        for (int i = 0; i < 256; i++){
            lt[i] = ImgUtils.clipRGB((int) (255 / (1 + Math.pow(e, (-lambda * ((double) 4*i/(double)255 - 2))))));
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
