package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * @author cjmcentee
 */
public class ColorPixel {


    static final private int RED_HUE        = 0/2;
    static final private int YELLOW_HUE     = 60/2;
    static final private int GREEN_HUE      = 120/2;
    static final private int CYAN_HUE       = 180/2;
    static final private int BLUE_HUE       = 240/2;
    static final private int MAGENTA_HUE    = 300/2;


    final public ColorSpace colorSpace;
    final public int channel1, channel2, channel3;

    /// ======================================================================
    /// Constructors
    /// ======================================================================
    public ColorPixel() {
        this(0, 0, 0, ColorSpace.BGR);
    }

    public ColorPixel(int channel1, int channel2, int channel3) {
        this(channel1, channel2, channel3, ColorSpace.BGR);
    }

    public ColorPixel(int channel1, int channel2, int channel3, ColorSpace colorSpace) {
        this.channel1 = channel1;
        this.channel2 = channel2;
        this.channel3 = channel3;
        this.colorSpace = colorSpace;
    }

    ColorPixel(Scalar scalarColor) {
        this(scalarColor, ColorSpace.BGR);
    }

    ColorPixel(Scalar scalarColor, ColorSpace colorSpace) {
        double channels[] = scalarColor.val;

        if (channels.length > 0)
            channel1 = (int) channels[0];
        else
            channel1 = 0;

        if (channels.length > 1)
            channel2 = (int) channels[1];
        else
            channel2 = 0;

        if (channels.length > 2)
            channel3 = (int) channels[2];
        else
            channel3 = 0;

        this.colorSpace = colorSpace;
    }


    /// ======================================================================
    /// Properties
    /// ======================================================================
    public boolean isGreen() {
        ColorPixel hsv = HSV();
        int hue = hsv.channel1;

        return hue < CYAN_HUE && hue > YELLOW_HUE;
    }


    /// ======================================================================
    /// Conversion
    /// ======================================================================
    /**
     * Converts this pixel to BGR.
     *
     * Data type ranges are:
     *      BLUE            [0, 256)
     *      GREEN           [0, 256)
     *      RED             [0, 256)
     *
     * @return      BGR formatted pixel
     */
    public ColorPixel BGR() {
        return convert(ColorSpace.BGR);
    }

    /**
     * Converts this pixel to HSV.
     *
     * Data type ranges are:
     *      HUE             [0, 180)
     *      SATURATION      [0, 256)
     *      VALUE           [0, 256)
     *
     * @return      HSV formatted pixel
     */
    public ColorPixel HSV() {
        return convert(ColorSpace.HSV);
    }

    ColorPixel convert(ColorSpace newColorSpace) {
        if (colorSpace == newColorSpace)
            return this;

        Mat singlePixel = new Mat(new Size(1, 1), CvType.CV_8UC3);
        Mat convertedPixel = new Mat(new Size(1, 1), CvType.CV_8UC3);
        Imgproc.cvtColor(singlePixel, convertedPixel, ColorSpace.convertCode(colorSpace, newColorSpace));

        byte pixel[] = new byte[3];
        convertedPixel.get(0, 0, pixel);

        return new ColorPixel(pixel[0], pixel[1], pixel[2], newColorSpace);
    }
}
