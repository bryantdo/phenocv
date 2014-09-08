package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;

import java.util.*;

/**
 * @author cjmcentee
 */
public final class ColorImage extends Image {

    public static final int CHANNEL_SIZE = 256;
    public static final float CHANNEL_MIN = 0;
    public static final float CHANNEL_MAX = 255;


    private HSV hsvImage;
    private Lab labImage;
    private YCrCb ycrcbImage;

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
        hsvImage = new HSV();
        labImage = new Lab();
        ycrcbImage = new YCrCb();
    }


    /// ======================================================================
    /// Image Manipulation
    /// ======================================================================

    public void grabCut(GrayImage mask) {
        Mat background = new Mat();
        Mat foreground = new Mat();
        Imgproc.grabCut(image, mask.image, rectangle(), background, foreground, 4, Imgproc.GC_INIT_WITH_MASK);
    }

    public void trainBackgroundSubtract(BackgroundSubtractor subtractor) {
        subtractor.apply(image, new Mat(), 10);
    }

    public GrayImage getForegroundMask(BackgroundSubtractor subtractor) {
        Mat foregroundMask = new Mat();
        subtractor.apply(image, foregroundMask, 0);
        return new GrayImage(foregroundMask);
    }

    public ColorImage segment(GrayImage contourMask) {

        Mat markers = new Mat();
        contourMask.image.convertTo(markers, CvType.CV_32S);

        Imgproc.watershed(image, markers);

        Highgui.imwrite("segmentation markers.png", markers);
        return this;
    }

    public void subtractBackground(ColorImage background) {
        ColorImage difference = background.minus(this);
        GrayImage differenceGray = difference.toGrayscale();
        Mask foregroundMask = differenceGray.toMask(20);
        this.maskWith(foregroundMask);
    }

    public ColorImage minus(ColorImage subtrahend) {
        ColorImage difference = new ColorImage(width(), height());

        int minuendPixels[] = toInt(this.pixels());
        int subtrahendPixels[] = toInt(subtrahend.pixels());
        byte differencePixels[] = difference.pixels();

        for (int p = 0; p < minuendPixels.length; p++)
            differencePixels[p] = (byte)(Math.max(0, (int) minuendPixels[p] - (int) subtrahendPixels[p]));

        difference.setPixels(differencePixels);
        return difference;
    }

    private static int[] toInt(byte bytes[]) {
        int ints[] = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            ints[i] = bytes[i] & 0xff;
        return ints;
    }

    /// ======================================================================
    /// Conversion and Copying
    /// ======================================================================

    @Override
    public Image copy() {
        Mat copy = new Mat();
        image.copyTo(copy);
        return new ColorImage(copy);
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

        Mat convertedImage = getWholeMatOf(channel);
        List<Mat> channels = new ArrayList<Mat>();
        Core.split(convertedImage, channels);

        switch (channel) {
            case HUE:
            case BLUE:
            case LIGHTNESS:
            case Y:
                return new GrayImage(channels.get(0));

            case SATURATION:
            case GREEN:
            case A:
            case Cr:
                return new GrayImage(channels.get(1));

            case VALUE:
            case RED:
            case B:
            case Cb:
                return new GrayImage(channels.get(2));

            default:
                return new GrayImage(channels.get(0));
        }
    }

    public ColorImage asHSV() {
        return new ColorImage(hsvImage.from(image));
    }

    public ColorImage asLab() {
        return new ColorImage(labImage.from(image));
    }

    public ColorImage asYCrCb() {
        return new ColorImage(ycrcbImage.from(image));
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
        return newImage;
    }

    protected Mat makeMatOf(Channel channel1, Channel channel2) {

        GrayImage channel1Image = getChannel(channel1);
        GrayImage channel2Image = getChannel(channel2);
        List<Mat> channels = Arrays.asList(channel1Image.image, channel2Image.image);

        Mat newImage = new Mat();
        Core.merge(channels, newImage);
        return newImage;
    }

    protected Mat makeMatOf(Channel channel) {
        GrayImage channelImage = getChannel(channel);
        List<Mat> channels = Arrays.asList(channelImage.image);

        Mat newImage = new Mat();
        Core.merge(channels, newImage);
        return newImage;
    }

    protected Mat getWholeMatOf(Channel channel) {
        switch (channel) {
            case HUE:
            case SATURATION:
            case VALUE:
                return hsvImage.from(image);
            case BLUE:
            case GREEN:
            case RED:
                return image;
            case LIGHTNESS:
            case A:
            case B:
                return labImage.from(image);
            case Y:
            case Cr:
            case Cb:
                return ycrcbImage.from(image);
            default:
                return image;
        }
    }
}
