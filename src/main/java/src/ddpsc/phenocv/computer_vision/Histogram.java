package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.utility.Copy;
import src.ddpsc.phenocv.utility.Lists;

import java.util.List;

/**
 * @author cjmcentee
 */
public final class Histogram implements Writable, Releasable {

    public static final double NO_SCALING = 1;

    Mat histogram;
    HistogramChannels channels;


    /// ======================================================================
    /// Constructors
    /// ======================================================================
    private Histogram() {
    }

    Histogram(Channel channel, Mat histogramMat) {
        channels = new HistogramChannels(channel);

        histogram = histogramMat;
    }

    Histogram(Channel channel1, Channel channel2, Mat histogramMat) {
        channels = new HistogramChannels(channel1, channel2);

        histogram = histogramMat;
    }

    Histogram(HistogramChannels channels, Mat histogramMat) {
        this.channels = new HistogramChannels(channels);

        histogram = histogramMat;
    }

    public static Histogram blank(HistogramChannels channels) {
        Histogram histogram = new Histogram();
        histogram.channels = channels;
        histogram.histogram = null;

        return histogram;
    }

    public Histogram copy() {
        Histogram copy = new Histogram();
        copy.channels = channels;
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

        List<Mat> imageMatrix = channels.convertedImage(image);
        Imgproc.calcBackProject(
                imageMatrix,
                channels.channelIndices(),
                histogram,
                backProjectedImage,
                channels.channelRanges(),
                NO_SCALING);

        ReleaseContainer.releaseMatrices(imageMatrix);

        return new GrayImage(backProjectedImage);
    }

    public static Histogram fromImage(HistogramChannels channels, ColorImage image, Mask mask) {

        Mat histogram = new Mat();
        List<Mat> convertedMatrix = channels.convertedImage(image);
        Imgproc.calcHist(
                convertedMatrix,
                channels.channelIndices(),
                mask.image,
                histogram,
                channels.channelSizes(),
                channels.channelRanges());

        ReleaseContainer.releaseMatrices(convertedMatrix);

        return new Histogram(channels, histogram);
    }

    public static Histogram fromImage(HistogramChannels channels, ColorImage image) {
        return fromImage(channels, image, Mask.showsAll(image.size()));
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
            this.channels = mergingHistogram.channels;
            this.histogram = Copy.matrix(mergingHistogram.histogram);

            return;
        }

        if (channels.equals(mergingHistogram.channels)) {
//            Mat combinedHistogram = new Mat();

            Core.add(this.histogram, mergingHistogram.histogram, this.histogram);
//
//            Mat oldHistogram = this.histogram;
//            this.histogram = combinedHistogram;
//            oldHistogram.release();
        }
    }

    /// ======================================================================
    /// Releasable
    /// ======================================================================
    public void release() {
        histogram.release();
    }

    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void writeTo(String filename) {
        Highgui.imwrite(filename, histogram);
    }
}
