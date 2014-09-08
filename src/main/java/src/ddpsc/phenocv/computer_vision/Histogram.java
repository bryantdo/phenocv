package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * @author cjmcentee
 */
public final class Histogram implements Writable {

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

    public static Histogram Blank(HistogramChannels channels) {
        Histogram histogram = new Histogram();
        histogram.channels = channels;
        histogram.histogram = null;

        return histogram;
    }

    public Histogram copy() {
        Histogram copy = new Histogram();
        copy.channels = channels;
        copy.histogram = histogram;

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

        Imgproc.calcBackProject(
                channels.convertedImage(image),
                channels.channelIndices(),
                histogram,
                backProjectedImage,
                channels.channelRanges(),
                NO_SCALING);

        return new GrayImage(backProjectedImage);
    }

    public static Histogram fromImage(HistogramChannels channels, ColorImage image, Mask mask) {

        Mat histogram = new Mat();
        Imgproc.calcHist(
                channels.convertedImage(image),
                channels.channelIndices(),
                mask.image,
                histogram,
                channels.channelSizes(),
                channels.channelRanges());

        return new Histogram(channels, histogram);
    }

    public static Histogram fromImage(HistogramChannels channels, ColorImage image) {
        return fromImage(channels, image, Mask.showsAll());
    }

    /**
     * Adds the values of the supplied histogram to this histogram.
     *
     * @param histogram
     */
    public Histogram combineWith(Histogram histogram) {

        if (this.histogram == null)
            return histogram.copy();

        if (histogram.histogram == null)
            return this.copy();

        Mat combined = new Mat();
        Core.add(this.histogram, histogram.histogram, combined);

        Histogram combinedHistogram = new Histogram();
        combinedHistogram.channels = this.channels;
        combinedHistogram.histogram = combined;

        return combinedHistogram;
    }

    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void writeTo(String filename) {
        Highgui.imwrite(filename, histogram);
    }
}
