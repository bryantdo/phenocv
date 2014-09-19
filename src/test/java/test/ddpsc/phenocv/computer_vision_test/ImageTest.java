package test.ddpsc.phenocv.computer_vision_test;

import org.junit.Test;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.utility.OpenCV;

/**
 * Untested:
 *      Image#maskWith(Shape)
 *      Image#maskWith(ShapeCollection)
 *      Image#medianFilter
 *      Image#segment
 *
 *      Image#numberPixels
 *      Image#width
 *      Image#height
 *      Image#rectangle
 *      Image#ize
 *      Image#valueType
 *
 * @author cjmcentee
 */
public class ImageTest {

    // Load openCV native library
    static {
        OpenCV.load();
    }

    private final static String NAME_PREFIX = "Image_Test_";

    @Test
    public void TestMaskWithMask() {

        ColorImage image = new ColorImage(TestFiles.GRADIENT);
        GrayImage mask = new GrayImage(TestFiles.GRADIENT_MASK);

        image.maskWith(mask);

        image.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX + "maskWith(Mask)_mask_application_results.png");
    }

}
