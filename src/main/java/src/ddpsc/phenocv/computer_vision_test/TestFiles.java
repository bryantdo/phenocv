package src.ddpsc.phenocv.computer_vision_test;

import org.opencv.core.Core;

/**
 * @author cjmcentee
 */
public class TestFiles {

    // Load openCV native library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public final static String PLANT_ROOT = "C:\\Development\\DDPSC\\PhenoCV\\resources\\images\\";
    public final static String TEST_RESULT_ROOT = "C:\\Development\\DDPSC\\PhenoCV\\resources\\images\\computer vision test results\\";
    public final static String ARTIFICIAL_ROOT = "C:\\Development\\DDPSC\\PhenoCV\\resources\\images\\artificial test images\\";

    public final static String GRADIENT_NAME = "gradient.png";
    public final static String GRADIENT = ARTIFICIAL_ROOT + GRADIENT_NAME;

    public final static String GRADIENT_GREENSPOT_NAME = "gradient with green spot.png";
    public final static String GRADIENT_GREENSPOT = ARTIFICIAL_ROOT + GRADIENT_GREENSPOT_NAME;

    public final static String GRADIENT_MASK_NAME = "gradient mask.png";
    public final static String GRADIENT_MASK = ARTIFICIAL_ROOT + GRADIENT_MASK_NAME;

    public final static String TINY_TEST = ARTIFICIAL_ROOT + "tiny test image.png";
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
