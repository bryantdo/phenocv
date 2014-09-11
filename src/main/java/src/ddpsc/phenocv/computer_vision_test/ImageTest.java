package src.ddpsc.phenocv.computer_vision_test;

import org.junit.Test;
import org.opencv.core.Core;
import src.ddpsc.phenocv.computer_vision.*;

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
 *      Image#size
 *      Image#valueType
 *
 * @author cjmcentee
 */
public class ImageTest {

    // Load openCV native library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final static String NAME_PREFIX = "Image Test ";

    @Test
    public void TestMaskWithMask() {

        ColorImage image = new ColorImage(TestFiles.GRADIENT);
        Mask mask = new Mask(TestFiles.GRADIENT_MASK);

        image.maskWith(mask);

        image.writeTo(TestFiles.TEST_RESULT_ROOT + NAME_PREFIX + "maskWith(Mask) mask application results.png");
    }

}
