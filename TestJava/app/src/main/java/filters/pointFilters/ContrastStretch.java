package filters.pointFilters;

import android.graphics.Bitmap;
import filters.ImgUtils;

// Stretches the istogram of the image
public class ContrastStretch extends PointFilter {

    // Attributes
    private int[] minMaxRed;
    private int[] minMaxGreen;
    private int[] minMaxBlue;

    public ContrastStretch(Bitmap bm) {
        super(bm);
    }

    // Override apply
    public Bitmap apply() {
        int[] temp = ImgUtils.getRGB(pixels[0]);
        minMaxRed = new int[] {temp[0], temp[0]};
        minMaxGreen = new int[] {temp[1], temp[1]};
        minMaxBlue = new int[] {temp[2], temp[2]};
        findMinMax();
        return super.apply();
    }

    //--- PRIVATE METHODS ------//

    protected int[] getLookupTable() {
        int[] lt = new int[256 * 3];
        int redLength = minMaxRed[1] - minMaxRed[0];
        int greenLength = minMaxGreen[1] - minMaxGreen[0];
        int blueLength = minMaxBlue[1] - minMaxBlue[0];

        for (int i = 0; i < 256; i++){
            lt[i] = ImgUtils.clipRGB((i-minMaxRed[0]) * 255 / redLength);
            lt[i+256] = ImgUtils.clipRGB((i-minMaxGreen[0]) * 255 / greenLength);
            lt[i+512] = ImgUtils.clipRGB((i-minMaxBlue[0]) * 255 / blueLength);
        }

        return lt;
    }

    // Finds the minimum and maximum pixel values for the channels
    private void findMinMax() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int idx = width * y + x;
                updateMinMax(pixels[idx]);
            }
        }
    }

    // Finds the minimum and maximum value for each channel
    private void updateMinMax(int pixel) {
        int[] channels = ImgUtils.getRGB(pixel);

        minMaxRed[0] = Math.min(minMaxRed[0], channels[0]);
        minMaxGreen[0] = Math.min(minMaxGreen[0], channels[1]);
        minMaxBlue[0] = Math.min(minMaxBlue[0], channels[2]);

        minMaxRed[1] = Math.max(minMaxRed[1], channels[0]);
        minMaxGreen[1] = Math.max(minMaxGreen[1], channels[1]);
        minMaxBlue[1] = Math.max(minMaxBlue[1], channels[2]);
    }
}
