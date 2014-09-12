package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.utility.Copy;

import java.util.Arrays;
import java.util.List;

/**
 * @author cjmcentee
 */
public final class Histogram implements Releasable {

    public static final double NO_SCALING = 1;

    Mat histogram;
    Channel channel;


    /// ======================================================================
    /// Constructors
    /// ======================================================================
    private Histogram() {
    }

    Histogram(Channel channel, Mat histogramMat) {
        this.channel = channel;

        histogram = histogramMat;
    }

    public static Histogram blank(Channel channel) {
        Histogram histogram = new Histogram();
        histogram.channel = channel;
        histogram.histogram = new Mat();

        return histogram;
    }

    public Histogram copy() {
        Histogram copy = new Histogram();
        copy.channel = channel;
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

        List<Mat> imageMatrix = Arrays.asList(image.cvAsChannel(channel));
        Imgproc.calcBackProject(
                imageMatrix,
                channel.indices(),
                histogram,
                backProjectedImage,
                channel.ranges(),
                NO_SCALING);

        ReleaseContainer.releaseMatrices(imageMatrix);

        return new GrayImage(backProjectedImage);
    }

    public static Histogram fromImage(Channel channel, ColorImage image, GrayImage mask) {

        Mat histogram = new Mat();
        List<Mat> convertedMatrix = Arrays.asList(image.cvAsChannel(channel));
        Imgproc.calcHist(
                convertedMatrix,
                channel.indices(),
                mask.image,
                histogram,
                channel.sizes(),
                channel.ranges());

        ReleaseContainer.releaseMatrices(convertedMatrix);

        return new Histogram(channel, histogram);
    }

    public static Histogram fromImage(Channel channel, ColorImage image) {
        return fromImage(channel, image, GrayImage.maskShowAll(image.size()));
    }

    /**
     * Adds the histogram of the supplied image-mask combo to this histogram.
     *
     * Similar to {@link Histogram#fromImage(Channel, ColorImage, GrayImage)}, but
     * instead of returning a new histogram, it adds the data to this histogram.
     *
     * @param channel       channel to convert the image into
     * @param image         image to get histogram data from
     * @param mask          mask to select which pixels are turned into histogram data
     */
    public void addImageData(Channel channel, ColorImage image, GrayImage mask) {

        if (this.channel != channel )
            return;

        List<Mat> convertedMatrix = Arrays.asList(image.cvAsChannel(channel));
        Imgproc.calcHist(
                convertedMatrix,
                channel.indices(),
                mask.image,
                this.histogram,
                channel.sizes(),
                channel.ranges(),
                true);

        ReleaseContainer.releaseMatrices(convertedMatrix);
    }

    public void addImageData(Channel channel, ColorImage image) {
        addImageData(channel, image, GrayImage.maskShowAll(image.size()));
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
            this.channel = mergingHistogram.channel;
            this.histogram = Copy.matrix(mergingHistogram.histogram);

            return;
        }

        if (channel.equals(mergingHistogram.channel))
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
