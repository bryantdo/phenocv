package src.ddpsc.phenocv.algorithm_test;

import org.junit.Test;
import org.opencv.core.Core;
import src.ddpsc.phenocv.algorithm.ColorIsolation;
import src.ddpsc.phenocv.computer_vision.*;
import src.ddpsc.phenocv.utility.PerformanceTimer;
import src.ddpsc.phenocv.utility.Lists;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ColorIsolationTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String SOURCE_ROOT = "C:\\Development\\DDPSC\\PhenoCV\\resources\\images\\plant test images\\";
    private static final String RESULTS_ROOT = "C:\\Development\\DDPSC\\PhenoCV\\resources\\images\\plant test images\\results\\";

    private static final String FILETYPE = ".png";

    // Training images
    private static final int TRAINING_SIZE = 1; // numbering starts at 1, ends on this value
    private static final String TRAIN_PREFIX = "train plant (";
    private static final String TRAIN_IMAGE_SUFFIX = ")" + FILETYPE;
    private static final String TRAIN_MASK_SUFFIX = ") mask" + FILETYPE;

    // Trained histogram
    private static final String COLOR_ISOLATION_NAME = "color isolation histogram";

    // Test images
    private static final int TEST_SIZE = 20
            ; // numbering starts at 1, ends on this value
    private static final String TEST_PREFIX = "test plant (";
    private static final String TEST_SUFFIX = ")" + FILETYPE;
    private static final String RESULTS_SUFFIX = ") test results";

    @Test
    public void TestAllChannels() {

        System.out.println("Color Isolation Algorithm Testing:");

        List<Tuple<String, String>> trainingFiles = getTrainingPairs();
        List<Tuple<ColorImage, Mask>> trainingPairs = Lists.loadMaskedImagePairs(trainingFiles);
        List<ColorImage> testImages = getTestImages();

        List<HistogramChannels> histogramChannels = HistogramChannels.allCombinations();

        for (HistogramChannels channel : histogramChannels) {

            System.out.println("Channel: " + channel.toString());
            ColorIsolation colorIsolation = new ColorIsolation(channel);
            colorIsolation.train(trainingPairs);
            colorIsolation.writeTo(RESULTS_ROOT + COLOR_ISOLATION_NAME + " " + channel.toString() + FILETYPE);
            for (int i = 0; i < testImages.size(); i++) {
                ColorImage image = testImages.get(i);

                PerformanceTimer.start();
                //ColorImage isolatedImage = colorIsolation.isolate(testImages.get(i));
                Tuple<GrayImage, ColorImage> isolationData = colorIsolation.detailedIsolation(image);
                PerformanceTimer.end();

                String backProjectionSaveName =
                        RESULTS_ROOT + TEST_PREFIX + i + RESULTS_SUFFIX
                                + " " + channel.toString() + " back projection" + FILETYPE;
                isolationData.item1.writeTo(backProjectionSaveName);

                String imageSaveName =
                        RESULTS_ROOT + TEST_PREFIX + i + RESULTS_SUFFIX
                                + " " + channel.toString() + FILETYPE;
                isolationData.item2.writeTo(imageSaveName);

                ReleaseContainer.releaseBoth(isolationData);
            }

            colorIsolation.release();
        }

        System.out.println(" ++++++ Color Isolation Algorithm Test ++++++\n" + PerformanceTimer.readout());
    }

    private static List<Tuple<String, String>> getTrainingPairs() {

        List<Tuple<String, String>> trainingPairs = new ArrayList<Tuple<String, String>>(TRAINING_SIZE);
        for (int i = 1; i <= TRAINING_SIZE; i++) {
            String imageFilename = SOURCE_ROOT + TRAIN_PREFIX + i + TRAIN_IMAGE_SUFFIX;
            String maskFilename = SOURCE_ROOT + TRAIN_PREFIX + i + TRAIN_MASK_SUFFIX;
            trainingPairs.add(new Tuple<String, String>(imageFilename, maskFilename));
        }

        return trainingPairs;
    }

    private static List<ColorImage> getTestImages() {

        List<ColorImage> images = new ArrayList<ColorImage>(TEST_SIZE + 1);
        for (int i = 1; i <= TEST_SIZE; i++) {
            String imageFilename = SOURCE_ROOT + TEST_PREFIX + i + TEST_SUFFIX;
            images.add(new ColorImage(imageFilename));
        }

        return images;
    }

}
