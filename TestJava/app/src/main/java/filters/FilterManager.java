package filters;

import android.graphics.Bitmap;
import filters.pointFilters.Colorize;
import filters.pointFilters.ContrastEnhance;
import filters.pointFilters.ContrastStretch;
import filters.pointFilters.GammaCorrection;

// Class for applying filters
public class FilterManager {

    // Applies Colorize filter
    public static Bitmap colorize(Bitmap bm, int red, int green, int blue) {
        Colorize cr = new Colorize(bm);
        return cr.apply(red, green, blue);
    }

    // Applies Contrast Stretch filter
    public static Bitmap contrastStretch(Bitmap bm) {
        ContrastStretch cs = new ContrastStretch(bm);
        return cs.apply();
    }

    // Applies Contrast Enhance filter
    public static Bitmap contrastEnhance(Bitmap bm, double lambda) {
        ContrastEnhance ce = new ContrastEnhance(bm);
        return ce.apply(lambda);
    }

    // Applies Gamma Correction filter
    public static Bitmap gammaCorrection(Bitmap bm, double gamma) {
        GammaCorrection gc = new GammaCorrection(bm);
        return gc.apply(gamma);
    }
}
