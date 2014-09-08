package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.CvType;
import org.opencv.highgui.Highgui;

/**
 * @author cjmcentee
 */
public class GrayImage extends Image {

    /// ======================================================================
    /// Constructors
    /// ======================================================================
    GrayImage(Mat image) {
        super(image);
    }

    /**
     * Creates an all black grayscale-formatted image of the supplied dimensions.
     *
     * Width is number of columns, height is number of rows
     *
     * @see ColorImage
     *
     * @param width     width of the image
     * @param height    height of the image
     */
    public GrayImage(int width, int height) {
        super(width, height, CvType.CV_8UC1);
    }

    /**
     * Creates a grayscale-formatted image of the file at the supplied file destination.
     *
     * If the image is colored, it will be converted to grayscale.
     *
     * @see ColorImage
     *
     * @param filename  destination on the filesystem of the image
     */
    public GrayImage(String filename) {
        super(filename, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    }

    /**
     * Returns an empty image with zero pixels
     *
     * @return      empty image
     */
    public static GrayImage empty() {
        return new GrayImage(new Mat());
    }

    /// ======================================================================
    /// Image Manipulation
    /// ======================================================================


    /// ======================================================================
    /// Conversion and Copying
    /// ======================================================================

    @Override
    public Image copy() {
        Mat copy = new Mat();
        image.copyTo(copy);
        return new GrayImage(copy);
    }

    /**
     * Returns a mask of this image. Any pixels below the threshold value
     * are treated as block (black), and above as show (white).
     *
     * @param threshold     threshold below which all pixels are blocked
     * @return              mask
     */
    public Mask toMask(int threshold) {
        Mat mask = new Mat();
        Imgproc.threshold(image, mask, threshold, 255, Imgproc.THRESH_BINARY);
        return new Mask(mask);
    }

    /**
     * Returns a mask of this image. Any non-black pixels are treated
     * as show (white), and black pixels as block (black).
     *
     * @return              mask
     */
    public Mask toMask() {
        return toMask(1);
    }

    /// ======================================================================
    /// Helper Methods
    /// ======================================================================
    @Override
    String debugString() {
        return "Gray Image\n" + super.debugString();
    }
}
