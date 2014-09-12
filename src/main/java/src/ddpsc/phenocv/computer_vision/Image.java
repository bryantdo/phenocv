package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.debug.Readable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public abstract class Image implements Writable, Releasable {

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
        if (image.channels() == 2) {
            Mat flatRed = new Mat(image.size(), CvType.CV_8UC1);

            List<Mat> blueGreenChannels = new ArrayList<Mat>(2);
            Core.split(image, blueGreenChannels);
            blueGreenChannels.add(flatRed);

            Mat threeChannelMatrix = new Mat();
            Core.merge(blueGreenChannels, threeChannelMatrix);

            Highgui.imwrite(filename, threeChannelMatrix);

            flatRed.release();
            ReleaseContainer.releaseMatrices(blueGreenChannels);
            threeChannelMatrix.release();
        }
        else
            Highgui.imwrite(filename, image);
    }


    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void release() {
        image.release();
    }


    /// ======================================================================
    /// Image Manipulation
    /// ======================================================================
    /**
     * Pixels set to black where they overlap with blocked (black) pixels in the mask
     *
     * @param mask mask to block pixels with
     *
     * @see GrayImage
     */
    public void maskWith(GrayImage mask) {
        Mat maskedMatrix = new Mat();
        image.copyTo(maskedMatrix, mask.image);

        Mat oldImage = image;
        image = maskedMatrix;
        oldImage.release();
    }

    /**
     * Pixels set to black where they don't overlap with the supplied {@link Shape}.
     * <p/>
     * Holes in the shape are considered not to overlap. Only the filled in portion
     * of the shape is overlapping.
     *
     * @param shape shape to mask with
     */
    public void maskWith(Shape shape) {
        GrayImage shapeMask = shape.imageMask(size());
        maskWith(shapeMask);

        shapeMask.release();
    }

    /**
     * Pixels set to black where they don't overlap with the supplied {@link ShapeCollection}.
     * <p/>
     * Holes in the collection are considered not to overlap. Only filled in shapes are
     * overlapping.
     *
     * @param shapes shapes to mask with
     */
    public void maskWith(ShapeCollection shapes) {
        GrayImage shapesMask = shapes.grayImage(size());
        shapesMask.threshold(1);
        maskWith(shapesMask);

        shapesMask.release();
    }

    /**
     * Applies a median filter to the image.
     * <p/>
     * Generally median filters sharpen and smooth the image while removing
     * small dots of noise.
     * <p/>
     * The median filter behaves by setting each pixel to the median value of
     * any of its neighboring pixels within the specified radius.
     * <p/>
     * The strength of the filter is the same as that radius. Usual values
     * are in the range of 2-20.
     *
     * @param strength strength of filter
     */
    public void medianFilter(int strength) {
        if (strength % 2 == 0)
            Imgproc.medianBlur(image, image, strength + 1);
        else
            Imgproc.medianBlur(image, image, strength);
    }


    /// ======================================================================
    /// Conversion and Copying
    /// ======================================================================

    /**
     * Deep copy of the image
     *
     * @return copy of the image
     */
    public abstract Image copy();


    /// ======================================================================
    /// Properties
    /// ======================================================================
    public byte[] pixels() {
        byte pixels[] = new byte[numberPixels() * image.channels()];
        image.get(0, 0, pixels);
        return pixels;
    }

    public abstract void setPixels(byte[] pixels, int width);

    /**
     * Returns the number of pixels in the image.
     *
     * This is equivalent to width*height.
     *
     * @return      number of pixels in the image
     */
    public int numberPixels() {
        return (int) image.total();
    }

    /**
     * Returns the width in pixels of this image/
     *
     * This is the same as returning the columns.
     *
     * @return      width in pixels of the image
     */
    public int width() {
        return image.width();
    }

    /**
     * Returns the height in pixels of this image.
     *
     * This is the same as returning the rows.
     *
     * @return      height in pixels of the image
     */
    public int height() {
        return image.height();
    }

    /**
     * Returns a rectangle with top left at (0, 0) and equal to
     * the size of this image.
     *
     * @return      rectangle size of the image
     */
    public Rect rectangle() {
        return new Rect(new Point(), image.size());
    }

    /**
     * Returns the size (width/height) of this image.
     *
     * @return      size of image
     */
    public Size size() {
        return image.size();
    }

    /**
     * Returns the type of this image in OpenCV's typing format.
     *
     * Use {@link src.ddpsc.phenocv.debug.Readable#type(int)} to process
     * the returned value into something more meaningful.
     *
     * All pixels will be of the type supplied. OpenCV does not mix types.
     *
     * @return      openCV type of the image pixels
     */
    public int valueType() {
        return image.type();
    }

    /// ======================================================================
    /// Helper Methods
    /// ======================================================================
    String debugString() {
        String debug = "Image Dimensions: " + width() + ", " + height()
                + "\nImage Pixel Type: " + Readable.type(image.type());

        return debug;
    }
}
