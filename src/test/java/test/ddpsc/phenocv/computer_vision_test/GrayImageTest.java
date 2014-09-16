package test.ddpsc.phenocv.computer_vision_test;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.utility.OpenCV;

import static org.hamcrest.CoreMatchers.*;

/**
 * Untested:
 *      GrayImage#toMaskAutomatic
 *      GrayImage#relativeThreshold
 *
 * @author cjmcentee
 */
public class GrayImageTest {

    // Load openCV native library
    static {
        OpenCV.Load();
    }

    @Test
    public void TestEmpty() {
        GrayImage empty = GrayImage.empty();

        Assert.assertThat("Empty gray images must have no pixels.",
                0, equalTo(empty.numberPixels()));
    }

    @Test
    public void TestLoadFile() {
        GrayImage gradient = new GrayImage(TestFiles.TINY_TEST);

        Assert.assertArrayEquals("Color gradient must load correctly.",
                TestFiles.TINY_PIXELS_GRAY, gradient.pixels());
    }

    @Test
    public void TestCopy() {
        GrayImage gradient = new GrayImage(TestFiles.TINY_TEST);
        GrayImage copy = (GrayImage) gradient.copy();

        Assert.assertArrayEquals("Copy must keep same pixel values.",
                gradient.pixels(), copy.pixels());
        Assert.assertThat("Copy must make different references.",
                gradient, not(sameInstance(copy)));
    }

    @Test
    public void TestAsGrayMatrix() {
        GrayImage image = new GrayImage(TestFiles.TINY_TEST);
        Mat imageMatrix = image.cvAsGray();

        byte imageMatrixPixels[] = new byte[(int)imageMatrix.total()];
        imageMatrix.get(0, 0, imageMatrixPixels);

        Assert.assertArrayEquals("cvAsGray must return a matrix with the same pixel values.",
                TestFiles.TINY_PIXELS_GRAY, imageMatrixPixels);
    }

    @Test
    public void TestSetPixels() {
        GrayImage setImage = GrayImage.empty();
        setImage.setPixels(TestFiles.TINY_PIXELS_GRAY, 5);

        Assert.assertArrayEquals("Set pixels must correctly set the pixels of the image.",
                TestFiles.TINY_PIXELS_GRAY, setImage.pixels());
    }

    @Test
    public void TestToMask() {
        GrayImage gradient = new GrayImage(TestFiles.TINY_TEST);
        gradient.threshold();

        Assert.assertArrayEquals("Default mask must set all non-black pixels white.",
                TestFiles.TINY_PIXELS_DEFAULT_MASK, gradient.pixels());
    }

    @Test
    public void TestToMaskBinary() {
        GrayImage gradient = new GrayImage(TestFiles.TINY_TEST);

        final int threshold = 128;
        gradient.threshold(threshold);

        Assert.assertArrayEquals("Default mask must set all pixels above the threshold white.",
                TestFiles.TINY_PIXELS_128THRESH_MASK, gradient.pixels());
    }


}
