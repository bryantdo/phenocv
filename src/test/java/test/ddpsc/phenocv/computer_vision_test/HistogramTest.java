package test.ddpsc.phenocv.computer_vision_test;

import org.junit.Test;
import org.opencv.core.Core;
import src.ddpsc.phenocv.computer_vision.HistogramPartition;
import src.ddpsc.phenocv.computer_vision.ColorSpace;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.computer_vision.Histogram;
import src.ddpsc.phenocv.utility.OpenCV;

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
        OpenCV.Load();
    }

    private static final String NAME_PREFIX = "Histogram_Test_";
    private static final HistogramPartition BGR = new HistogramPartition(ColorSpace.BGR, 75);

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
                + "back_project_grad_onto_spot_result.png");
        spotOntoSpot.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back_project_spot_onto_spot_result.png");

        gradOntoGrad.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back_project_grad_onto_grad_result.png");
        spotOntoGrad.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back_project_spot_onto_grad_result.png");

        gradOntoSpot.threshold();
        spotGradient.maskWith(gradOntoSpot);
        spotGradient.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back_project_grad_onto_spot_isolation.png");
    }

    @Test
    public void TestSmooth() {
        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);

        Histogram gradientHistogram = Histogram.fromImage(BGR, gradient);
        GrayImage backProjection = gradientHistogram.backProjectionOf(gradient);

        backProjection.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back_project_smoothed_histogram_grad_onto_grad_result.png");

        backProjection.threshold();
        gradient.maskWith(backProjection);
        gradient.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back_project_smoothed_histogram_grad_onto_grad_isolation.png");
    }

    @Test
    public void TestInputImageBackProjectionDensity() {
        // Back project both gradients onto both images (4 back projections)
        ColorImage largeGradient = new ColorImage(TestFiles.LARGE_GRADIENT);
        ColorImage spotGradient = new ColorImage(TestFiles.GRADIENT_GREENSPOT);

        Histogram gradientHistogram = Histogram.fromImage(BGR, largeGradient);

        GrayImage gradOntoSpot = gradientHistogram.backProjectionOf(spotGradient);

        gradOntoSpot.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back_project_large_grad_onto_spot_result.png");

        gradOntoSpot.threshold();
        spotGradient.maskWith(gradOntoSpot);
        spotGradient.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX
                + "back_project_large_grad_onto_spot_isolation.png");
    }

}
