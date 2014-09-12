package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.utility.Copy;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a histogram. A histogram is a measure of the overall color of each pixel
 * in an image.
 *
 * The histogram can represents any color space desired by the user.
 *
 * These histograms are only 3D histograms are they have the highest fidelity
 * and best isolation rate when back-projecting. As this API is specialized
 * for plant identification, ancillary elements like {@link GrayImage} histograms
 * and 2D histograms are excluded.
 *
 * The main purpose of this class is to get a histogram and then back-project
 * it onto a different image in an attempt to isolate certain elements of an image.
 *
 * @author cjmcentee
 */
public final class Histogram implements Releasable {

    public static final double NO_SCALING = 1;

    Mat histogram;
    HistogramPartition histogramPartition;


    /// ======================================================================
    /// Constructors
    /// ======================================================================
    private Histogram() {
    }

    Histogram(HistogramPartition histogramPartition, Mat histogramMat) {
        this.histogramPartition = histogramPartition;

        histogram = histogramMat;
    }

    public static Histogram blank(HistogramPartition histogramPartition) {
        Histogram histogram = new Histogram();
        histogram.histogramPartition = histogramPartition;
        histogram.histogram = new Mat();

        return histogram;
    }

    public Histogram copy() {
        Histogram copy = new Histogram();
        copy.histogramPartition = histogramPartition;
        copy.histogram = Copy.matrix(histogram);

        return copy;
    }


    /// ======================================================================
    /// Image Manipulation
    /// ======================================================================

    /**
     * Returns a {@link GrayImage} with each pixel representing the probability
     * that the supplied image's corresponding pixel is in this histogram.
     *
     * The brighter the pixel in the output mask, the more likely the corresponding pixel
     * in the supplied image is in the histogram.
     *
     * The output mask is of the same dimensions as the input image.
     *
     * @param image image to back project onto
     * @return mask representing back projection probabilities
     * @see GrayImage
     */
    public GrayImage backProjectionOf(ColorImage image) {

         Mat backProjectedImage = new Mat();

        List<Mat> imageMatrix = Arrays.asList(image.cvAsChannel(histogramPartition));
        Imgproc.calcBackProject(
                imageMatrix,
                histogramPartition.indices(),
                histogram,
                backProjectedImage,
                histogramPartition.ranges(),
                NO_SCALING);

        ReleaseContainer.releaseMatrices(imageMatrix);

        return new GrayImage(backProjectedImage);
    }

    public static Histogram fromImage(HistogramPartition histogramPartition, ColorImage image, GrayImage mask) {

        Mat histogram = new Mat();
        List<Mat> convertedMatrix = Arrays.asList(image.cvAsChannel(histogramPartition));
        Imgproc.calcHist(
                convertedMatrix,
                histogramPartition.indices(),
                mask.image,
                histogram,
                histogramPartition.sizes(),
                histogramPartition.ranges());

        ReleaseContainer.releaseMatrices(convertedMatrix);

        return new Histogram(histogramPartition, histogram);
    }

    public static Histogram fromImage(HistogramPartition histogramPartition, ColorImage image) {
        return fromImage(histogramPartition, image, GrayImage.maskShowAll(image.size()));
    }

    /**
     * Adds the histogram of the supplied image-mask combo to this histogram.
     *
     * Similar to {@link Histogram#fromImage(HistogramPartition, ColorImage, GrayImage)}, but
     * instead of returning a new histogram, it adds the data to this histogram.
     *
     * @param histogramPartition       channel to convert the image into
     * @param image         image to get histogram data from
     * @param mask          mask to select which pixels are turned into histogram data
     */
    public void addImageData(HistogramPartition histogramPartition, ColorImage image, GrayImage mask) {

        if (this.histogramPartition != histogramPartition)
            return;

        List<Mat> convertedMatrix = Arrays.asList(image.cvAsChannel(histogramPartition));
        Imgproc.calcHist(
                convertedMatrix,
                histogramPartition.indices(),
                mask.image,
                this.histogram,
                histogramPartition.sizes(),
                histogramPartition.ranges(),
                true);

        ReleaseContainer.releaseMatrices(convertedMatrix);
    }

    public void addImageData(HistogramPartition histogramPartition, ColorImage image) {
        addImageData(histogramPartition, image, GrayImage.maskShowAll(image.size()));
    }



    /**
     * Adds the values of the supplied histogram to this histogram.
     *
     * @param mergingHistogram      histogram being merged into this one
     */
    public void combineWith(Histogram mergingHistogram) {

        if (mergingHistogram == null || mergingHistogram.histogram == null)
            return;

        if (this.histogram == null) { // not same as this being null, this.histogram is Matrix field
            this.histogramPartition = mergingHistogram.histogramPartition;
            this.histogram = Copy.matrix(mergingHistogram.histogram);

            return;
        }

        if (histogramPartition.equals(mergingHistogram.histogramPartition))
            Core.add(this.histogram, mergingHistogram.histogram, this.histogram);
    }

    public void smooth() {
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
        Imgproc.morphologyEx(histogram, histogram, Imgproc.MORPH_OPEN, kernel);
    }

    /// ======================================================================
    /// Releasable
    /// ======================================================================
    public void release() {
        histogram.release();
    }
}
