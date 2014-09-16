package test.ddpsc.phenocv.computer_vision_test;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.utility.OpenCV;

import static org.hamcrest.CoreMatchers.*;

/**
 * Untested:
 *      ColorImage#getChannel
 *
 * @author cjmcentee
 */
public class ColorImageTest {

    // Load openCV native library
    static {
        OpenCV.Load();
    }

    @Test
    public void TestEmpty() {
        ColorImage empty = ColorImage.empty();

        Assert.assertThat("Empty color images must have no pixels.",
                0, equalTo(empty.numberPixels()));
    }

    @Test
    public void TestLoadFile() {
        ColorImage gradient = new ColorImage(TestFiles.TINY_TEST);

        Assert.assertArrayEquals("Color gradient must load correctly.",
                TestFiles.TINY_PIXELS_BGR, gradient.pixels());
    }

    @Test
    public void TestCopy() {
        ColorImage gradient = new ColorImage(TestFiles.TINY_TEST);
        ColorImage copy = (ColorImage) gradient.copy();

        Assert.assertArrayEquals("Copy must keep same pixel values.",
                gradient.pixels(), copy.pixels());
        Assert.assertThat("Copy must make different references.",
                gradient, not(sameInstance(copy)));
    }

    @Test
    public void TestAsBGRMatrix() {
        ColorImage image = new ColorImage(TestFiles.TINY_TEST);
        Mat imageMatrix = image.cvAsBGRMatrix();

        byte imageMatrixPixels[] = new byte[(int)imageMatrix.total() * 3];
        imageMatrix.get(0, 0, imageMatrixPixels);

        Assert.assertArrayEquals("cvAsGray must return a matrix with the same pixel values.",
                TestFiles.TINY_PIXELS_BGR, imageMatrixPixels);
    }

    @Test
    public void TestSetPixels() {
        ColorImage setImage = ColorImage.empty();
        setImage.setPixels(TestFiles.TINY_PIXELS_BGR, 5);

        Assert.assertArrayEquals("Set pixels must correctly set the pixels of the image.",
                TestFiles.TINY_PIXELS_BGR, setImage.pixels());
    }

    @Test
    public void TestToGrayscale() {
        ColorImage image = new ColorImage(TestFiles.TINY_TEST);
        GrayImage grayImage = image.toGrayscale();

        Assert.assertThat("Grayscale conversion must result in 1 8bit channel.",
                CvType.CV_8UC1, is(grayImage.valueType()));
    }


}
