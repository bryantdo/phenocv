package test.ddpsc.phenocv.computer_vision_test;

import org.opencv.core.Core;
import src.ddpsc.phenocv.utility.OpenCV;

import java.io.File;

/**
 * @author cjmcentee
 */
public class TestFiles {

    // Load openCV native library
    static {
        OpenCV.Load();
    }

    private static final String PROJECT_ROOT = System.getProperty("user.dir");

    public final static String PLANT_ROOT = PROJECT_ROOT
            + File.separator + "resources"
            + File.separator + "images";

    public final static String TEST_RESULT_ROOT = PROJECT_ROOT
            + File.separator + "resources"
            + File.separator + "images"
            + File.separator + "computer_vision_test_results" + File.separator;

    public final static String TEST_SOURCE_ROOT = PROJECT_ROOT
            + File.separator + "resources"
            + File.separator + "images"
            + File.separator + "computer_vision_training" + File.separator;

    public final static String RAINBOW_NAME = "rainbow.png";
    public final static String RAINBOW = TEST_SOURCE_ROOT + RAINBOW_NAME;

    public final static String SUBSHAPES_NAME = "many_shapes.png";
    public final static String SUBSHAPES = TEST_SOURCE_ROOT + SUBSHAPES_NAME;

    public final static String SHAPE_NAME = "simple_shape.png";
    public final static String SHAPE = TEST_SOURCE_ROOT + SHAPE_NAME;

    public final static String LARGE_GRADIENT_NAME = "large_gradient.png";
    public final static String LARGE_GRADIENT = TEST_SOURCE_ROOT + LARGE_GRADIENT_NAME;

    public final static String GRADIENT_NAME = "gradient.png";
    public final static String GRADIENT = TEST_SOURCE_ROOT + GRADIENT_NAME;

    public final static String GRADIENT_GREENSPOT_NAME = "gradient_with_green_spot.png";
    public final static String GRADIENT_GREENSPOT = TEST_SOURCE_ROOT + GRADIENT_GREENSPOT_NAME;

    public final static String GRADIENT_MASK_NAME = "gradient_mask.png";
    public final static String GRADIENT_MASK = TEST_SOURCE_ROOT + GRADIENT_MASK_NAME;

    public final static String TINY_TEST = TEST_SOURCE_ROOT + "tiny_test_image.png";
    public final static byte TINY_PIXELS_BGR[] =
            new byte[] {// BLUE   GREEN     RED
                    (byte)255, (byte)255, (byte)255, // white to black
                    (byte)191, (byte)191, (byte)191,
                    (byte)128, (byte)128, (byte)128,
                    (byte) 64, (byte) 64, (byte) 64,
                    (byte)  0, (byte)  0, (byte)  0,

                    (byte)255, (byte)255, (byte)  0, // cyan to black
                    (byte)191, (byte)191, (byte)  0,
                    (byte)128, (byte)128, (byte)  0,
                    (byte) 64, (byte) 64, (byte)  0,
                    (byte)  0, (byte)  0, (byte)  0,

                    (byte)  0, (byte)255, (byte)128, // off-green to black
                    (byte)  0, (byte)191, (byte) 96,
                    (byte)  0, (byte)128, (byte) 64,
                    (byte)  0, (byte) 64, (byte) 32,
                    (byte)  0, (byte)  0, (byte)  0,

                    (byte)  0, (byte)  0, (byte)255, // red to black
                    (byte)  0, (byte)  0, (byte)191,
                    (byte)  0, (byte)  0, (byte)128,
                    (byte)  0, (byte)  0, (byte) 64,
                    (byte)  0, (byte)  0, (byte)  0,

                    (byte)255, (byte)  0, (byte)128, // purple to black
                    (byte)191, (byte)  0, (byte) 96,
                    (byte)128, (byte)  0, (byte) 64,
                    (byte) 64, (byte)  0, (byte) 32,
                    (byte)  0, (byte)  0, (byte)  0};

    public final static byte TINY_PIXELS_GRAY[] =
            new byte[] {
                    (byte)255, (byte)191, (byte)128, (byte) 64, (byte)  0, // white to black
                    (byte)178, (byte)133, (byte) 89, (byte) 44, (byte)  0, // cyan to black
                    (byte)187, (byte)140, (byte) 94, (byte) 47, (byte)  0, // off-green to black
                    (byte) 76, (byte) 57, (byte) 38, (byte) 19, (byte)  0, // red to black
                    (byte) 67, (byte) 50, (byte) 33, (byte) 16, (byte)  0};// purple to black
    public final static byte TINY_PIXELS_DEFAULT_MASK[] =
            new byte[] {
                    (byte)255, (byte)255, (byte)255, (byte)255, (byte)  0, // white to black
                    (byte)255, (byte)255, (byte)255, (byte)255, (byte)  0, // cyan to black
                    (byte)255, (byte)255, (byte)255, (byte)255, (byte)  0, // off-green to black
                    (byte)255, (byte)255, (byte)255, (byte)255, (byte)  0, // red to black
                    (byte)255, (byte)255, (byte)255, (byte)255, (byte)  0};// purple to black
    public final static byte TINY_PIXELS_128THRESH_MASK[] =
            new byte[] {
                    (byte)255, (byte)255, (byte)  0, (byte)  0, (byte)  0, // white to black
                    (byte)255, (byte)255, (byte)  0, (byte)  0, (byte)  0, // cyan to black
                    (byte)255, (byte)255, (byte)  0, (byte)  0, (byte)  0, // off-green to black
                    (byte)  0, (byte)  0, (byte)  0, (byte)  0, (byte)  0, // red to black
                    (byte)  0, (byte)  0, (byte)  0, (byte)  0, (byte)  0};// purple to black
}
