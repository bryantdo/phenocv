package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

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
    @Override
    public void setPixels(byte[] pixels, int width) {
        int numberPixels = pixels.length / 1;
        int columns = width;
        int rows = numberPixels / columns;
        image = new Mat(rows, columns, CvType.CV_8UC1);
        image.put(0, 0, pixels);
    }

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
     * Returns a {@link Mask} of this image. Any pixels below the threshold value
     * are treated as block (black), and above as show (white).
     *
     * @param threshold     value below which pixels are blocked in the mask
     * @return              mask
     */
    public Mask toMask(int threshold) {
        Mat mask = new Mat();
        Imgproc.threshold(image, mask, threshold, 255, Imgproc.THRESH_BINARY);
        return new Mask(mask);
    }

    /**
     * Returns a {@link Mask} of this image where blocked pixels are those
     * that are relatively lower to their neighbors by approximately
     * relativeDifference in value.
     *
     * @param relativeDifference    approx difference between a pixel and its neighbors to be masked
     * @return                      mask
     */
    public Mask toMaskRelative(int relativeDifference) {
        Mat mask = new Mat();
        Imgproc.adaptiveThreshold(image, mask, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 10, relativeDifference);
        return new Mask(mask);
    }

    /**
     * Returns a {@link Mask} of this image where an automatic algorithm
     * attempts to determine which pixels are background and which are foreground.
     * In the resulting mask, background pixels are blocked, foreground pixels are shown.
     *
     * Uses Otsu's method of histogram analysis.
     *
     * @return      mask
     */
    public Mask toMaskAutomatic() {
        Mat mask = new Mat();
        Imgproc.threshold(image, mask, 1, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        return new Mask(mask);
    }

    /**
     * Returns a {@link Mask} of this image. Any non-black pixels are treated
     * as show (white), and black pixels as block (black).
     *
     * @return              mask of this image
     */
    public Mask toMask() {
        return toMask(1);
    }

    /// ======================================================================
    /// Lower level OpenCV Java bindings access
    /// ======================================================================
    public Mat cvAsGray() {
        return image;
    }

    /// ======================================================================
    /// Helper Methods
    /// ======================================================================
    @Override
    String debugString() {
        return "Gray Image\n" + super.debugString();
    }
}
