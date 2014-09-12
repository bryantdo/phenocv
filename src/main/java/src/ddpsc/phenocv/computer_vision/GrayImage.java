package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * This is a gray scale image in a single channel.
 *
 * Its primary use, aside from being an image, is to be a mask.
 *
 * A mask is used to be overlaid onto another image to hide/reveal
 * particular pixels within the other image. In order for a mask
 * to do this, it must be of the same dimensions as the target
 * image. Then the target image and the mask can be placed
 * on top of one another and any pixels of the mask that are black
 * are filtered out of the target image. The only remaining pixels
 * are those where the mask was non-black.
 *
 * It is recommended that users threshold any GrayImage objects
 * before using them as masks to avoid strange and unexpected errors.
 *
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
     * Returns an empty image with zero pixels.
     *
     * @return      empty image
     */
    public static GrayImage empty() {
        return new GrayImage(new Mat());
    }

    /**
     * Generates a GrayImage that behaves as a mask that blocks all pixels.
     *
     * This means the image is all black.
     *
     * @param size      size of the returned image
     * @return          an all black image
     */
    public static GrayImage maskBlockAll(Size size) {
        GrayImage show = new GrayImage(new Mat(size, CvType.CV_8UC1));
        show.image.setTo(new Scalar(0));

        return show;
    }

    /**
     * Generates a GrayImage that behaves as a mask that reveals all pixels.
     *
     * This means the image is all white.
     *
     * @param size      size of the returned image
     * @return          an all white image
     */
    public static GrayImage maskShowAll(Size size) {
        GrayImage show = new GrayImage(new Mat(size, CvType.CV_8UC1));
        show.image.setTo(new Scalar(255));

        return show;
    }

    /// ======================================================================
    /// Image Manipulation
    /// ======================================================================

    /**
     * Sets the pixels of this image to the pixels supplied.
     *
     * Because GrayImage is a single channel image, the pixels, unlike
     * {@link ColorImage#setPixels(byte[], int)} are arrayed in the
     * expected manner:
     *  {pixel1, pixel2, pixel3, pixel4, etc...}
     *
     *  The width is the width of the image the pixels represent.
     *  The width does not have to be the width of this image as it is overwritten
     *  if it is different.
     *
     * @param pixels        new pixels for this image
     * @param width         width of the new image
     */
    @Override
    public void setPixels(byte[] pixels, int width) {
        int numberPixels = pixels.length / 1;
        int columns = width;
        int rows = numberPixels / columns;
        image = new Mat(rows, columns, CvType.CV_8UC1);
        image.put(0, 0, pixels);
    }

    /**
     * Filters the mask with a morphological filter of the specified type and size.
     *
     * All filters are done with an ellipse shaped template.
     *
     * Filter strength is the same as the size of the template in pixels.
     * Values in the range of 3-20 are typical.
     *
     * @param morphType         type of filter to use
     * @param strength          strength of filter
     */
    public void morphologicalFilter(MorphType morphType, double strength) {
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(strength, strength));
        Imgproc.morphologyEx(image, image, morphType.type(), kernel);

        kernel.release();
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
     * Returns a masked form of this image. Any pixels below the threshold value
     * are treated as block (black), and above as show (white).
     *
     * @param threshold     value below which pixels are blocked in the mask
     */
    public void threshold(int threshold) {
        Imgproc.threshold(image, image, threshold, 255, Imgproc.THRESH_BINARY);
    }

    /**
     * Returns a masked form of this image where blocked pixels are those
     * that are relatively lower to their neighbors by approximately
     * relativeDifference in value.
     *
     * @param relativeDifference    approx difference between a pixel and its neighbors to be masked
     */
    public void relativeThreshold(int relativeDifference) {
        Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 10, relativeDifference);
    }

    /**
     * Returns a masked form of this image where an automatic algorithm
     * attempts to determine which pixels are background and which are foreground.
     * In the resulting mask, background pixels are blocked, foreground pixels are shown.
     *
     * Uses Otsu's method of histogram analysis.
     */
    public void toMaskAutomatic() {
        Imgproc.threshold(image, image, 1, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
    }

    /**
     * Returns a masked form of this image. Any non-black pixels are treated
     * as show (white), and black pixels as block (black).
     */
    public void threshold() {
        threshold(1);
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
