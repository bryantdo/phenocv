package src.ddpsc.phenocv.algorithm_test;

import org.junit.Test;
import src.ddpsc.phenocv.algorithm.ColorIsolation;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.HistogramChannels;
import src.ddpsc.phenocv.computer_vision.Image;
import src.ddpsc.phenocv.computer_vision.Mask;
import src.ddpsc.phenocv.utility.Interval;
import src.ddpsc.phenocv.utility.Lists;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ColorIsolationTest {

    private static final String FILE_ROOT = "C:\\Development\\DDPSC\\PhenoCV\\resources\\images\\color isolation";
    private static final String COLOR_ISOLATION_NAME = "color isolation histogram";
    private static final String NAME_PREFIX = "side view plant (";

    private static final String FILETYPE = ".png";

    private static final String TRAIN_IMAGE_SUFFIX = ") image train" + FILETYPE;
    private static final String TRAIN_MASK_SUFFIX = ") mask train" + FILETYPE;
    private static final int TRAINING_SIZE = 5; // numbering starts at 1, ends on this value

    private static final String TEST_IMAGE_SUFFIX = ") image test" + FILETYPE;
    private static final int TEST_SIZE = 20; // numbering starts at 1, ends on this value

    private static final String ISOLATED_IMAGE_SUFFIX = ") image test results";

    @Test
    public static void TestAllChannels() {

        List<Tuple<String, String>> trainingFiles = getTrainingPairs();
        List<Tuple<ColorImage, Mask>> trainingPairs = Lists.loadMaskedImagePairs(trainingFiles);
        List<ColorImage> testImages = getTestImages();

        List<HistogramChannels> histogramChannels = HistogramChannels.allCombinations();

        for (HistogramChannels channel : histogramChannels) {

            ColorIsolation colorIsolation = new ColorIsolation(channel);
            colorIsolation.train(trainingPairs);
            colorIsolation.writeTo(FILE_ROOT + COLOR_ISOLATION_NAME + " " + channel.toString() + FILETYPE);

            List<Tuple<Image, String>> imageWrites = new ArrayList<Tuple<Image, String>>();
            for (int i = 0; i < testImages.size(); i++) {
                Interval.start();
                ColorImage isolatedImage = colorIsolation.isolate(testImages.get(i));
                Interval.end();
                String imageSaveName =
                        FILE_ROOT + NAME_PREFIX + i + ISOLATED_IMAGE_SUFFIX
                                + " " + channel.toString() + FILETYPE;

                imageWrites.add(new Tuple<Image, String>(isolatedImage, imageSaveName));
            }

            Image.writeTo(imageWrites);
        }

        System.out.println(" ++++++ Color Isolation Algorithm Test ++++++\n" + Interval.readout());
    }

    private static List<Tuple<String, String>> getTrainingPairs() {

        List<Tuple<String, String>> trainingPairs = new ArrayList<Tuple<String, String>>(TRAINING_SIZE);
        for (int i = 1; i <= TRAINING_SIZE; i++) {
            String imageFilename = FILE_ROOT + NAME_PREFIX + i + TRAIN_IMAGE_SUFFIX;
            String maskFilename = FILE_ROOT + NAME_PREFIX + i + TRAIN_MASK_SUFFIX;
            trainingPairs.add(new Tuple<String, String>(imageFilename, maskFilename));
        }

        return trainingPairs;
    }

    private static List<ColorImage> getTestImages() {

        List<ColorImage> images = new ArrayList<ColorImage>(TEST_SIZE + 1);
        for (int i = 1; i <= TEST_SIZE; i++) {
            String imageFilename = FILE_ROOT + NAME_PREFIX + i + TEST_IMAGE_SUFFIX;
            images.add(new ColorImage(imageFilename));
        }

        return images;
    }

}
