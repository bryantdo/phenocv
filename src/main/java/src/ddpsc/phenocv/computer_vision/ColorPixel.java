package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Scalar;
import src.ddpsc.phenocv.utility.Convert;
import src.ddpsc.phenocv.utility.MathX;

/**
 * Represents a colored pixel of an image in the BGR range.
 *
 * Wraps the OpenCV {@link Scalar} "class".
 *
 * @author cjmcentee
 */
public class ColorPixel {

    public static final ColorPixel BLACK = new ColorPixel(0, 0, 0);

    static final private int RED_HUE        = 0;
    static final private int YELLOW_HUE     = 30;
    static final private int GREEN_HUE      = 60;
    static final private int CYAN_HUE       = 90;
    static final private int BLUE_HUE       = 120;
    static final private int MAGENTA_HUE    = 150;

    final public int blue, green, red;

    /// ======================================================================
    /// Constructors
    /// ======================================================================
    public ColorPixel() {
        this(0, 0, 0);
    }

    private ColorPixel(int blue, int green, int red) {
        this.blue = blue;
        this.green = green;
        this.red = red;
    }

    public static ColorPixel fromInt(int blue, int green, int red) {
        return new ColorPixel(blue, green, red);
    }

    public static ColorPixel fromByte(byte blue, byte green, byte red) {
        return new ColorPixel(Convert.toInt(blue), Convert.toInt(green), Convert.toInt(red));
    }

    ColorPixel(Scalar scalarColor) {
        double channels[] = scalarColor.val;

        if (channels.length > 0)
            blue = (int) channels[0];
        else
            blue = 0;

        if (channels.length > 1)
            green = (int) channels[1];
        else
            green = 0;

        if (channels.length > 2)
            red = (int) channels[2];
        else
            red = 0;
    }


    /// ======================================================================
    /// Properties
    /// ======================================================================
    public boolean isGreen() {
        int hue = hue();

        return hue < CYAN_HUE - 10 && hue > YELLOW_HUE + 10;
    }

    @Override
    public String toString() {
        return "(" + blue + ", " + green + ", " + red + ")";
    }


    /// ======================================================================
    /// Conversion
    /// ======================================================================

    Scalar scalar() {
        return new Scalar(blue, green, red);
    }

    public int hue() {
        int min = MathX.min(red, green, blue);
        int max = MathX.max(red, green, blue);
        double delta = max - min;

        if(max == 0) // black
            return -1;

        double hue;
        if(red == max)
            hue = (green - blue) / delta; // between yellow & magenta
        else if(green == max)
            hue = 2 + (blue - red) / delta;  // between cyan & yellow
        else
            hue = 4 + (red - green) / delta;  // between magenta & cyan

        hue *= 60.0; // degrees

        if(hue < 0) // in range 0 - 360
            hue += 360.0;

        return (int) (hue / 2); // in range 0 - 180 (openCV convention for 8bit depth images)
    }
}
