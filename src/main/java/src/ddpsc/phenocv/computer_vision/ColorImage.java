package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ColorImage extends Image {

    private HSVFactory hsvImage;
    private LabFactory labImage;
    private YCrCbFactory ycrcbImage;


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
        hsvImage = new HSVFactory();
        labImage = new LabFactory();
        ycrcbImage = new YCrCbFactory();
    }


    /// ======================================================================
    /// Image Manipulation
    /// ======================================================================
    @Override
    public void setPixels(byte[] pixels, int width) {
        int numberPixels = pixels.length / 3;
        int columns = width;
        int rows = numberPixels / columns;

        image.release();

        image = new Mat(rows, columns, CvType.CV_8UC3);
        image.put(0, 0, pixels);
    }

    //    public void grabCut(GrayImage mask) {
//        Mat background = new Mat();
//        Mat foreground = new Mat();
//        Imgproc.grabCut(image, mask.image, rectangle(), background, foreground, 4, Imgproc.GC_INIT_WITH_MASK);
//    }


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

    /**
     * Returns a {@link GrayImage} of the specified channel.
     *
     * @param channel       channel to retrieve
     * @return              image of the specified channel
     */
    public GrayImage getChannel(Channel channel) {

        // Get each channel
        List<Mat> channels = new ArrayList<Mat>();
        Mat convertedImage = getWholeMatOf(channel); // reference to field variable, do not release
        Core.split(convertedImage, channels);

        // Return only the relevant channel
        switch (channel) {
            case VALUE:
            case RED:
            case B:
            case Cb:
                channels.get(0).release();
                channels.get(1).release();
                return new GrayImage(channels.get(2));

            case SATURATION:
            case GREEN:
            case A:
            case Cr:
                channels.get(0).release();
                channels.get(2).release();
                return new GrayImage(channels.get(1));

            case HUE:
            case BLUE:
            case LIGHTNESS:
            case Y:
            default:
                channels.get(1).release();
                channels.get(2).release();
                return new GrayImage(channels.get(0));
        }
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
     * Access to the OpenCV Java binding root object.
     *
     * For use in adding functionality to this library without
     * forking or modifying its source code.
     *
     * @return      {@link Mat} representing the image in HSV format
     */
    public Mat cvAsHSVMatrix() {
        return hsvImage.getFrom(image);
    }

    /**
     * Access to the OpenCV Java binding root object.
     *
     * For use in adding functionality to this library without
     * forking or modifying its source code.
     *
     * @return      {@link Mat} representing the image in Lab format
     */
    public Mat cvAsLabMatrix() {
        return labImage.getFrom(image);
    }

    /**
     * Access to the OpenCV Java binding root object.
     *
     * For use in adding functionality to this library without
     * forking or modifying its source code.
     *
     * @return      {@link Mat} representing the image in YCrCb format
     */
    public Mat cvAsYCrCbMatrix() {
        return ycrcbImage.getFrom(image);
    }


    /// ======================================================================
    /// Helper Methods
    /// ======================================================================
    @Override
    String debugString() {
        return "Color Image\n" + super.debugString();
    }

    protected Mat makeMatOf(Channel channel1, Channel channel2, Channel channel3) {

        GrayImage channel1Image = getChannel(channel1);
        GrayImage channel2Image = getChannel(channel2);
        GrayImage channel3Image = getChannel(channel3);
        List<Mat> channels = Arrays.asList(channel1Image.image, channel2Image.image, channel3Image.image);

        Mat newImage = new Mat();
        Core.merge(channels, newImage);

        channel1Image.release();
        channel2Image.release();
        channel3Image.release();

        return newImage;
    }

    protected Mat makeMatOf(Channel channel1, Channel channel2) {

        GrayImage channel1Image = getChannel(channel1);
        GrayImage channel2Image = getChannel(channel2);
        List<Mat> channels = Arrays.asList(channel1Image.image, channel2Image.image);

        Mat newImage = new Mat();
        Core.merge(channels, newImage);

        channel1Image.release();
        channel2Image.release();

        return newImage;
    }

    protected Mat makeMatOf(Channel channel) {
        GrayImage channelImage = getChannel(channel);
        return channelImage.image;
    }

    protected Mat getWholeMatOf(Channel channel) {
        switch (channel) {
            case HUE:
            case SATURATION:
            case VALUE:
                return cvAsHSVMatrix();
            case LIGHTNESS:
            case A:
            case B:
                return cvAsLabMatrix();
            case Y:
            case Cr:
            case Cb:
                return cvAsYCrCbMatrix();
            case BLUE:
            case GREEN:
            case RED:
            default:
                return image;
        }
    }
}
