package src.ddpsc.phenocv.computer_vision_test;

import org.junit.Test;
import org.opencv.core.Core;
import src.ddpsc.phenocv.computer_vision.*;

/**
 * Untested:
 *      Histogram#blank
 *      Histogram#copy
 *      Histogram#combineWith
 *
 * @author cjmcentee
 */
public class HistogramTest {

    // Load openCV native library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String NAME_PREFIX = "Histogram Test ";
    private static final HistogramChannels BLUE_RED = new HistogramChannels(Channel.BLUE, Channel.RED);

    @Test
    public void TestFromImage() {
        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);
        ColorImage spotGradient = new ColorImage(TestFiles.GRADIENT_GREENSPOT);

        Histogram gradientHistogram = Histogram.fromImage(BLUE_RED, gradient);
        gradientHistogram.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "fromImage(" + TestFiles.GRADIENT_NAME + ") result.png");

        Histogram spotGradientHistogram = Histogram.fromImage(BLUE_RED, spotGradient);
        spotGradientHistogram.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "fromImage(" + TestFiles.GRADIENT_GREENSPOT_NAME + ") result.png");
    }

    @Test
    public void TestBackProjectionOf() {
        // Back project both gradients onto both images (4 back projections)
        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);
        ColorImage spotGradient = new ColorImage(TestFiles.GRADIENT_GREENSPOT);

        Histogram gradientHistogram = Histogram.fromImage(BLUE_RED, gradient);
        Histogram spotGradientHistogram = Histogram.fromImage(BLUE_RED, spotGradient);

        GrayImage gradOntoSpot = gradientHistogram.backProjectionOf(spotGradient);
        GrayImage spotOntoSpot = spotGradientHistogram.backProjectionOf(spotGradient);

        GrayImage gradOntoGrad = gradientHistogram.backProjectionOf(gradient);
        GrayImage spotOntoGrad = spotGradientHistogram.backProjectionOf(gradient);

        gradOntoSpot.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project grad onto spot result.png");
        spotOntoSpot.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project spot onto spot result.png");

        gradOntoGrad.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project grad onto grad result.png");
        spotOntoGrad.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project spot onto grad result.png");

        Mask gradOntoSpotMask = gradOntoSpot.toMask();
        spotGradient.maskWith(gradOntoSpotMask);
        spotGradient.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project grad onto spot isolation.png");
    }

    @Test
    public void TestCombineWith() {
        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);
        ColorImage spotGradient = new ColorImage(TestFiles.GRADIENT_GREENSPOT);

        Histogram gradientHistogram = Histogram.fromImage(BLUE_RED, gradient);
        Histogram spotGradientHistogram = Histogram.fromImage(BLUE_RED, spotGradient);

        gradientHistogram.combineWith(spotGradientHistogram);

        gradientHistogram.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "combined gradient and spot histograms.png");
    }


}
