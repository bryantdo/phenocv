package src.ddpsc.phenocv.algorithm;

import src.ddpsc.phenocv.computer_vision.*;
import src.ddpsc.phenocv.utility.Lists;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.List;

/**
 * @author cjmcentee
 */
public class ColorIsolation implements Writable {

    boolean trained = false;
    HistogramChannels channels;
    Histogram histogram;

    public ColorIsolation(HistogramChannels channels) {
        this.channels = channels;
        histogram = Histogram.Blank(channels);
    }

    public void train(List<Tuple<ColorImage, Mask>> trainingImages) {
        if (trainingImages.size() == 0)
            return;

        for (Tuple<ColorImage, Mask> trainingPair : trainingImages) {
            ColorImage image = trainingPair.item1;
            Mask mask = trainingPair.item2;

            Histogram maskHistogram = Histogram.fromImage(channels, image, mask);

            histogram = histogram.combineWith(maskHistogram);
        }

        trained = true;
    }

    public void trainFiles(List<Tuple<String, String>> trainingFiles) {
        train(Lists.loadMaskedImagePairs(trainingFiles));
    }

    public ColorImage isolate(ColorImage image) {
        if (trained == false)
            return image;

        else {
            GrayImage backProjection = histogram.backProjectionOf(image);
            Mask mask = backProjection.toMask();

            ColorImage copy = (ColorImage) image.copy();
            copy.maskWith(mask);
            return copy;
        }
    }

    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void writeTo(String filename) {
        histogram.writeTo(filename);
    }
}
