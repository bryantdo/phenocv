package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * @author cjmcentee
 */
public final class Mask extends GrayImage {

    /// ======================================================================
    /// Constructors
    /// ======================================================================
    Mask(Mat image) {
        super(image);
    }

    /**
     * Creates a mask of the supplied dimensions that blocks all pixels
     *
     * Width is number of columns, height is number of rows
     *
     * @see GrayImage
     *
     * @param width     width of the image
     * @param height    height of the image
     */
    public Mask(int width, int height) {
        super(width, height);
    }

    /**
     * Creates a binary mask of the file at the supplied file destination.
     *
     * If the image is not binary, it will be made binary
     *
     * @see GrayImage
     *
     * @param filename  destination on the filesystem of the image
     */
    public Mask(String filename) {
        super(filename);
        // threshold image in place
        Imgproc.threshold(image, image, 1, 255, Imgproc.THRESH_BINARY);
    }

    /**
     * Returns a mask that blocks all pixels
     *
     * @return      empty image
     */
    public static Mask blocksAll() {
        return new Mask(new Mat());
    }

    public static Mask showsAll() {
        Mask show = new Mask(new Mat());
        show.image.setTo(new Scalar(255));
        Imgproc.cvtColor(show.image, show.image, CvType.CV_8UC1);

        return show;
    }


    /// ======================================================================
    /// Image Processing
    /// ======================================================================

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
    }


    /// ======================================================================
    /// Conversion and Copying
    /// ======================================================================

    @Override
    public Image copy() {
        Mat copy = new Mat();
        image.copyTo(copy);
        return new Mask(copy);
    }

    /// ======================================================================
    /// Helper Methods
    /// ======================================================================
    @Override
    String debugString() {
        return "Mask\n" + super.debugString();
    }
}
