package test.ddpsc.phenocv.computer_vision_test;

import org.junit.Test;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.ColorPixel;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.utility.OpenCV;

/**
 * @author cjmcentee
 */
public class ColorPixelTest {

    static {
        OpenCV.load();
    }

    @Test
    public void TestIsGreen() {
        ColorImage rainbow = new ColorImage(TestFiles.RAINBOW);

        byte pixels[] = rainbow.pixels();

        byte maskPixels[] = new byte[rainbow.numberPixels()];
        for (int i = 0; i < rainbow.numberPixels(); i++) {
            byte blue  = pixels[3*i];
            byte green = pixels[3*i + 1];
            byte red   = pixels[3*i + 2];

            ColorPixel color = ColorPixel.fromByte(blue, green, red);
            if (color.isGreen())
                maskPixels[i] = (byte)255;
            else
                maskPixels[i] = (byte)  0;
        }

        GrayImage mask = new GrayImage(rainbow.size());
        mask.setPixels(maskPixels, rainbow.width());

        rainbow.maskWith(mask);
        rainbow.writeTo(TestFiles.TEST_RESULT_ROOT + "only_green_pixels.png");
    }
}
