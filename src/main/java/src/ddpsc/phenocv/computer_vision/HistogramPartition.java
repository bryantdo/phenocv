package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;

/**
 * Defines a type of histogram by both number of bins and color channel.
 *
 * All histograms divide image pixel values into a series of buckets, or bins.
 *
 * This class defines what those bins are. Its two parameters are:
 *      color space
 *      number of bins
 *
 * The fewer bins, the most discrete the histogram is. For most color spaces,
 * any value above 255 won't give any more refinement as the color space itself
 * is capped at 255.
 *
 * Hue in HSV/HLS is special, it is capped at 180.
 *
 * The more bins, the sparser the histogram, but also the more refined it is
 * in separating pixels from each other. For any particular requirement,
 * the bins have to be set by the user's desires. There is no one fixed
 * best value.
 *
 * Default is max-bins (255).
 *
 * @author cjmcentee
 */
public class HistogramPartition {

    private static final int DEFAULT_BINS = 255;

    public final ColorSpace colorSpace;
    protected final int numberBins;

    public HistogramPartition(ColorSpace space) {
        this(space, DEFAULT_BINS);
    }

    public HistogramPartition(ColorSpace space, int magnitude) {
        this.colorSpace = space;
        this.numberBins = magnitude;
    }

    @Override
    public String toString() {
        return colorSpace.name();
    }

    MatOfInt sizes() {
        return new MatOfInt(numberBins, numberBins, numberBins);
    }

    MatOfFloat ranges() {
        return new MatOfFloat(0, 255, 0, 255, 0, 255);
    }

    MatOfInt indices() {
        return new MatOfInt(0, 1, 2);
    }

    /**
     * All the histogram partitions associated with a given number of bins.
     *
     * @param numberBins        number of bins each histogram partition will have
     * @return                  all color space histograms with a given number of bins
     */
    public static HistogramPartition[] values(int numberBins) {
        ColorSpace spaces[] = ColorSpace.values();
        HistogramPartition partitions[] = new HistogramPartition[spaces.length];

        for (int i = 0; i < spaces.length; i++)
            partitions[i] = new HistogramPartition(spaces[i], numberBins);

        return partitions;
    }

    public static HistogramPartition[] values() {
        return values(DEFAULT_BINS);
    }
}
