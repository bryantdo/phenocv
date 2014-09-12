package test.ddpsc.phenocv.computer_vision_test;

import org.junit.Test;
import org.opencv.core.Core;
import src.ddpsc.phenocv.computer_vision.Channel;
import src.ddpsc.phenocv.computer_vision.ColorSpace;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.computer_vision.Histogram;

/**
 * Untested:
 *      Histogram#blank
 *      Histogram#copy
 *
 * @author cjmcentee
 */
public class HistogramTest {

    // Load openCV native library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String NAME_PREFIX = "Histogram Test ";
    private static final Channel BGR = new Channel(ColorSpace.BGR, 75);

    @Test
    public void TestBackProjectionOf() {
        // Back project both gradients onto both images (4 back projections)
        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);
        ColorImage spotGradient = new ColorImage(TestFiles.GRADIENT_GREENSPOT);

        Histogram gradientHistogram = Histogram.fromImage(BGR, gradient);
        Histogram spotGradientHistogram = Histogram.fromImage(BGR, spotGradient);

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

        gradOntoSpot.threshold();
        spotGradient.maskWith(gradOntoSpot);
        spotGradient.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project grad onto spot isolation.png");
    }

    @Test
    public void TestSmooth() {
        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);

        Histogram gradientHistogram = Histogram.fromImage(BGR, gradient);
        GrayImage backProjection = gradientHistogram.backProjectionOf(gradient);

        backProjection.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project smoothed histogram grad onto grad result.png");

        backProjection.threshold();
        gradient.maskWith(backProjection);
        gradient.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project smoothed histogram grad onto grad isolation.png");
    }

    @Test
    public void TestInputImageBackProjectionDensity() {
        // Back project both gradients onto both images (4 back projections)
        ColorImage largeGradient = new ColorImage(TestFiles.LARGE_GRADIENT);
        ColorImage spotGradient = new ColorImage(TestFiles.GRADIENT_GREENSPOT);

        Histogram gradientHistogram = Histogram.fromImage(BGR, largeGradient);

        GrayImage gradOntoSpot = gradientHistogram.backProjectionOf(spotGradient);

        gradOntoSpot.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project large grad onto spot result.png");

        gradOntoSpot.threshold();
        spotGradient.maskWith(gradOntoSpot);
        spotGradient.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back project large grad onto spot isolation.png");
    }

}
