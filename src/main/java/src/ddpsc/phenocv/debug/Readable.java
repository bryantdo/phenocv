package src.ddpsc.phenocv.debug;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * @author cjmcentee
 */
public class Readable {

    // Constants to process image type integers from CvType
    // They are private to CvType, but useful here so we repeat them
    private static final byte CV_CN_SHIFT = 3;
    private static final int CV_MAT_DEPTH_MASK = (0x01 << CV_CN_SHIFT) - 0x01;

    /**
     * Displays an openCV pixel type from a {@link Mat} object in a human readable format.
     *
     * Gray images are 1-dimensional unsigned 8bit
     * Color images are 3-dimensional unsigned 8bit
     *
     * @param cvType
     * @return
     */
    public static String type(int cvType) {
        String type;

        int channels = 1 + (cvType >> CV_CN_SHIFT);
        type = channels + "-dimensional ";

        int depth = cvType & CV_MAT_DEPTH_MASK;
        switch (depth) {
            case CvType.CV_8U:
                type += "unsigned 8bit";
                break;
            case CvType.CV_8S:
                type += "signed 8bit";
                break;
            case CvType.CV_16U:
                type += "unsigned 16bit";
                break;
            case CvType.CV_16S:
                type += "signed 16bit";
                break;
            case CvType.CV_32S:
                type += "signed 32bit";
                break;
            case CvType.CV_32F:
                type += "float";
                break;
            case CvType.CV_64F:
                type += "double";
                break;
            default:
                type += "user defined";
                break;
        }

        return type;
    }

    public static String matrix(Mat m) {
        return "Matrix:"
            + "\nTotal:" + m.total()
            + "\nType:" + Readable.type(m.type())
            + "\nSize:" + m.size()
            + "\nChannels:" + m.channels();
    }

    /**
     * Displays a BGR openCV color pixel in a human readable format
     *
     * As a note, the openCV {@link Mat} object stores its pixels in a
     * 1-D byte array. The channels for each pixel are presented
     * in blue-green-red ordering.
     *
     * @see Readable#colorByte
     *
     * @param blue      blue channel of pixel
     * @param green     green channel of pixel
     * @param red       red channel of pixel
     * @return          a string triplet of the pixel channels
     */
    public static String pixel(byte blue, byte green, byte red) {
        return "(" + colorByte(blue) + ", " + colorByte(green) + ", " + colorByte(red) + ")";
    }

    /**
     * Returns the color value byte of an openCV {@link Mat} as an integer in [0, 256).
     *
     * The pixel color channels of 8bit images loop such that:
     *       0     is  black
     *       127   is  midgrey
     *      -128   is  midgray + 1
     *      -1     is  white
     *
     * This method returns them in the familiar format as usually found in image
     * processing application.
     *
     * @see Readable#pixel
     *
     * @param colorValue        openCV color channel
     * @return                  color channel in the familiar format
     */
    public static int colorByte(byte colorValue) {
        return colorValue & 0xff;
    }
}
