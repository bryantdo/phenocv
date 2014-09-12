package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * This class represents an image of 3-channel color. The class itself naturally works in BGR (note,
 * not RGB), but a developer can pull out matrices of other color channels to work with those if
 * need be.
 *
 * Asking for a color conversion of the same channel more than once doesn't cause it to be
 * re-computed. However manually setting the pixels of this image does wipe out the different
 * calculated images as they're no longer accurate as the pixels have changed.
 *
 * Wraps the OpenCV {@link Mat} "class".
 *
 * If a developer extends this class, they must take care to reset the color conversion objects
 * any time they modify the values of the root OpenCV matrix.
 *
 * @author cjmcentee
 */
public class ColorImage extends Image {

    private ColorHSVFactory hsvImage;
    private ColorLabFactory labImage;
    private ColorYCrCbFactory ycrcbImage;
    private ColorLuvFactory luvImage;
    private ColorHLSFactory hlsImage;
    private ColorXYZFactory xyzImage;
    private ColorYUVFactory yuvImage;


    /// ======================================================================
    /// Constructors
    /// ======================================================================
    ColorImage(Mat mat) {
        super(mat);
        _init();
    }

    /**
     * Creates an all black color-formatted image of the supplied dimensions.
     *
     * width is number of columns, height is number of rows
     *
     * @see GrayImage
     *
     * @param width     width of the image
     * @param height    height of the image
     */
    public ColorImage(int width, int height) {
        super(width, height, CvType.CV_8UC3);
        _init();
    }

    /**
     * Creates a color-formatted image of the file at the supplied file destination.
     *
     * If the image is grayscale, it will be stored as a color image.
     *
     * @see GrayImage
     *
     * @param filename  destination on the filesystem of the image
     */
    public ColorImage(String filename) {
        super(filename, Highgui.CV_LOAD_IMAGE_COLOR);
        _init();
    }

    /**
     * Returns an empty image with zero pixels
     *
     * @return      empty image
     */
    public static ColorImage empty() {
        return new ColorImage(new Mat());
    }

    private void _init() {
        hsvImage = new ColorHSVFactory();
        labImage = new ColorLabFactory();
        ycrcbImage = new ColorYCrCbFactory();
        luvImage = new ColorLuvFactory();
        hlsImage = new ColorHLSFactory();
        xyzImage = new ColorXYZFactory();
        yuvImage = new ColorYUVFactory();
    }


    /// ======================================================================
    /// Image Manipulation
    /// ======================================================================

    /**
     * Sets the pixels of this image to the supplied pixels.
     *
     * The pixel array splits the color channels into three parts. So the array should be
     * the form:
     *      {pixel1Blue, pixel1Green, pixel1Red, pixel2Blue, pixel2Green, ... etc}
     *
     * The width represents the width of the image.
     * The width does not have to be the width of this image as it is overwritten
     *  if it is different.
     *
     * @param pixels        pixels to set this image to
     * @param width         width of the image
     */
    @Override
    public void setPixels(byte[] pixels, int width) {
        int numberPixels = pixels.length / 3;
        int columns = width;
        int rows = numberPixels / columns;

        image.release();

        image = new Mat(rows, columns, CvType.CV_8UC3);
        image.put(0, 0, pixels);

        resetFactories();
    }

    /// ======================================================================
    /// Conversion and Copying
    /// ======================================================================
    @Override
    public Image copy() {
        Mat copiedMatrix = new Mat();
        image.copyTo(copiedMatrix);

        ColorImage colorCopy = new ColorImage(copiedMatrix);
        return colorCopy;
    }

    /**
     * Converts this image to a {@link GrayImage}.
     *
     * @return          grayscale form of this image
     */
    public GrayImage toGrayscale() {
        Mat grayscale = new Mat();
        Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_BGR2GRAY, 1);
        return new GrayImage(grayscale);
    }


    /// ======================================================================
    /// Releasable
    /// ======================================================================
    @Override
    public void release() {
        super.release();
        hsvImage.release();
        labImage.release();
        ycrcbImage.release();
        luvImage.release();
        hlsImage.release();
        xyzImage.release();
        yuvImage.release();
    }


    /// ======================================================================
    /// Lower level OpenCV Java bindings access
    /// ======================================================================
    public Mat cvAsBGRMatrix() {
        Mat bgr = new Mat();
        image.copyTo(bgr);
        return bgr;
    }

    /**
     * Access to the OpenCV Java binding root object by specified channel.
     *
     * For use in adding functionality to this library without
     * forking or modifying its source code.
     *
     * @param histogramPartition       the channel of the resulting Matrix
     * @return              {@link Mat} representing the image in the requested format format
     */
    public Mat cvAsChannel(HistogramPartition histogramPartition) {
        switch (histogramPartition.colorSpace) {
            case BGR:
                return cvAsBGRMatrix();
            case HSV:
                return cvAsHSVMatrix();
            case Lab:
                return cvAsLabMatrix();
            case YCrCb:
                return cvAsYCrCbMatrix();
            case Luv:
                return cvAsLuvMatrix();
            case HLS:
                return cvAsHLSMatrix();
            case XYZ:
                return cvAsXYZMatrix();
            case YUV:
                return cvAsYUVMatrix();
            default:
                return cvAsBGRMatrix();
        }
    }

    public Mat cvAsYUVMatrix() {
        return yuvImage.getFrom(image);
    }

    public Mat cvAsXYZMatrix() {
        return xyzImage.getFrom(image);
    }

    public Mat cvAsHSVMatrix() {
        return hsvImage.getFrom(image);
    }

    public Mat cvAsHLSMatrix() {
        return hlsImage.getFrom(image);
    }

    public Mat cvAsLabMatrix() {
        return labImage.getFrom(image);
    }

    public Mat cvAsYCrCbMatrix() {
        return ycrcbImage.getFrom(image);
    }

    public Mat cvAsLuvMatrix() {
        return luvImage.getFrom(image);
    }


    /// ======================================================================
    /// Helper Methods
    /// ======================================================================
    @Override
    String debugString() {
        return "Color Image\n" + super.debugString();
    }

    private void resetFactories() {
        hsvImage.reset();
        labImage.reset();
        ycrcbImage.reset();
        luvImage.reset();
        hlsImage.reset();
        xyzImage.reset();
        yuvImage.reset();
    }
}
