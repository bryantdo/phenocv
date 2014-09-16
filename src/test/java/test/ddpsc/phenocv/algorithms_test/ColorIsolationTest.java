package test.ddpsc.phenocv.algorithms_test;

import org.junit.Test;
import org.opencv.core.Core;
import src.ddpsc.phenocv.algorithms.ColorIsolation;
import src.ddpsc.phenocv.computer_vision.*;
import src.ddpsc.phenocv.utility.Lists;
import src.ddpsc.phenocv.utility.PerformanceTimer;
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

    private static final String TRAIN_ROOT = "/Users/bryantd/IdeaProjects/phenocv/resources/images/color isolation training/";
    private static final String TEST_ROOT = "/Users/bryantd/IdeaProjects/phenocv/resources/images/color isolation test/";
    private static final String RESULTS_ROOT = "/Users/bryantd/IdeaProjects/phenocv/resources/images/color isolation test/results/";

    private static final String FILETYPE = ".png";

    // Training images
    private static final int TRAINING_SIZE = 14; // numbering starts at 1, ends on this value
    private static final String TRAIN_PREFIX = "train plant (";
    private static final String TRAIN_IMAGE_SUFFIX = ")" + FILETYPE;
    private static final String TRAIN_MASK_SUFFIX = ") mask" + FILETYPE;

    // Trained histogram
    private static final String COLOR_ISOLATION_NAME = "color isolation histogram";

    // Test images
    private static final int TEST_SIZE_INIT = 1;
    private static final int TEST_SIZE = 20
            ; // numbering starts at 1, ends on this value
    private static final String TEST_PREFIX = "test plant (";
    private static final String TEST_SUFFIX = ")" + FILETYPE;
    private static final String RESULTS_SUFFIX = ") test results";

    @Test
    public void TestAllChannels() {

        System.out.println("Color Isolation Algorithm Testing:");

        List<Tuple<String, String>> trainingFiles = getTrainingPairs();
        List<Tuple<ColorImage, GrayImage>> trainingPairs = Lists.loadMaskedImagePairs(trainingFiles);
        List<ColorImage> testImages = getTestImages();

        //HistogramPartition[] histogramPartitions = HistogramPartition.values(100);
        HistogramPartition[] histogramPartitions = new HistogramPartition[] {
                new HistogramPartition(ColorSpace.HSV, 100),
                new HistogramPartition(ColorSpace.HLS, 100)};

        for (HistogramPartition histogramPartition : histogramPartitions) {

            System.out.println("Channel: " + histogramPartition.toString());
            ColorIsolation colorIsolation = new ColorIsolation(histogramPartition);
            colorIsolation.train(trainingPairs);
            for (int i = 0; i < testImages.size(); i++) {
                ColorImage image = testImages.get(i);
                ColorImage copy = (ColorImage) image.copy();

                PerformanceTimer.start();
                colorIsolation.fastIsolation(copy);
                PerformanceTimer.end();

                /*String originalSaveName =
                        RESULTS_ROOT + histogramPartition.toString() + " "
                                + TEST_PREFIX + i + RESULTS_SUFFIX
                                + " original" + FILETYPE;
                image.writeTo(originalSaveName);*/

                String imageSaveName =
                        RESULTS_ROOT + histogramPartition.toString() + " "
                                + TEST_PREFIX + i + RESULTS_SUFFIX
                                + FILETYPE;
                copy.writeTo(imageSaveName);

                /*String backProjectionSaveName =
                        RESULTS_ROOT + histogramPartition.toString() + " "
                                + TEST_PREFIX + i + RESULTS_SUFFIX
                                + " back projection" + FILETYPE;
                isolationData.item1.writeTo(backProjectionSaveName);

                String imageSaveName =
                        RESULTS_ROOT + histogramPartition.toString() + " "
                                + TEST_PREFIX + i + RESULTS_SUFFIX
                                + FILETYPE;
                isolationData.item2.writeTo(imageSaveName);

                ReleaseContainer.releaseBoth(isolationData);*/
            }

            colorIsolation.release();
        }

        System.out.println(" ++++++ Color Isolation Algorithm Test ++++++\n" + PerformanceTimer.readout());
    }

    private static List<Tuple<String, String>> getTrainingPairs() {

        List<Tuple<String, String>> trainingPairs = new ArrayList<Tuple<String, String>>(TRAINING_SIZE);
        for (int i = 1; i <= TRAINING_SIZE; i++) {
            String imageFilename = TRAIN_ROOT + TRAIN_PREFIX + i + TRAIN_IMAGE_SUFFIX;
            String maskFilename = TRAIN_ROOT + TRAIN_PREFIX + i + TRAIN_MASK_SUFFIX;
            trainingPairs.add(new Tuple<String, String>(imageFilename, maskFilename));
        }

        return trainingPairs;
    }

    private static List<ColorImage> getTestImages() {

        List<ColorImage> images = new ArrayList<ColorImage>(TEST_SIZE + 1);
        for (int i = TEST_SIZE_INIT; i <= TEST_SIZE; i++) {
            String imageFilename = TEST_ROOT + TEST_PREFIX + i + TEST_SUFFIX;
            images.add(new ColorImage(imageFilename));
        }

        return images;
    }

}
