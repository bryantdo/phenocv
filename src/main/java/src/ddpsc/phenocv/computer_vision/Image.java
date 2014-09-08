package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.debug.Readable;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.core.Size;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author cjmcentee
 */
public abstract class Image implements Writable {

    protected Mat image;

    /// ======================================================================
    /// Constructors
    /// ======================================================================
    protected Image() {
        image = new Mat();
    }

    protected Image(Mat mat) {
        image = mat;
    }

    protected Image(int width, int height, int colorType) {
        image = new Mat(height, width, colorType);
    }

    protected Image(String filename, int imageType) {
        image = Highgui.imread(filename, imageType);
    }

    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void writeTo(String filename) {
        Highgui.imwrite(filename, image);
    }

    public static void writeTo(List<Tuple<Image, String>> imageWrites) {
        for (Tuple<Image, String> imageWrite : imageWrites) {
            Image image = imageWrite.item1;
            String filename = imageWrite.item2;

            image.writeTo(filename);
        }
    }

    /// ======================================================================
    /// Image Manipulation
    /// ======================================================================

    /**
     * Pixels set to black where they overlap with blocked (black) pixels in the mask
     *
     * @see Mask
     *
     * @param mask          mask to block pixels with
     */
    public void maskWith(Mask mask) {
        image.copyTo(image, mask.image);
    }

    /**
     * Applies a median filter to the image.
     *
     * Generally median filters sharpen and smooth the image while removing
     * small dots of noise.
     *
     * The median filter behaves by setting each pixel to the median value of
     * any of its neighboring pixels within the specified radius.
     *
     * The strength of the filter is the same as that radius. Usual values
     * are in the range of 2-20.
     *
     * @param strength        strength of filter
     */
    public void medianFilter(int strength) {
        Imgproc.medianBlur(image, image, strength);
    }

    /**
     * Finds and returns the contours of the image. Works best on {@link Mask} images
     * and high contrast {@link GrayImage}.
     *
     * @return              contours of the image
     */
    public List<MatOfPoint> findContours() {
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat heirarchy = new Mat();
        Imgproc.findContours(image, contours, heirarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);

        return contours;
    }

    /**
     * Draws the contours on the image as lines of random colors 4 pixels thick.
     *
     * If the image is a {@link GrayImage}, draws the contours as random shades of
     * gray.
     *
     * @param contours      contours to draw
     */
    public void drawContours(List<MatOfPoint> contours) {

        Random random = new Random();
        for (MatOfPoint contour : contours) {
            List<MatOfPoint> singleContour = Arrays.asList(contour);
            Scalar randomColor = new Scalar(
                    127 + random.nextInt(128),
                    127 + random.nextInt(128),
                    127 + random.nextInt(128));

            Imgproc.drawContours(image, singleContour, -1, randomColor, 4);
        }
    }

    /// ======================================================================
    /// Conversion and Copying
    /// ======================================================================

    /**
     * Deep copy of the image
     *
     * @return      copy of the image
     */
    public abstract Image copy();



    /// ======================================================================
    /// Properties
    /// ======================================================================
    protected byte[] pixels() {
        byte pixels[] = new byte[numberPixels() * image.channels()];
        image.get(0, 0, pixels);
        return pixels;
    }

    protected void setPixels(byte[] pixels) {
        image.put(0, 0, pixels);
    }

    public int numberPixels() {
        return (int)image.total();
    }

    public int width() {
        return image.width();
    }

    public int height() {
        return image.height();
    }

    protected Rect rectangle() {
        return new Rect(0, 0, width(), height());
    }


    /// ======================================================================
    /// Helper Methods
    /// ======================================================================
    String debugString() {
        String debug =  "Image Dimensions: " + width() + ", " + height()
                + "\nImage Pixel Type: " + Readable.type(image.type());

        return debug;
    }
}
