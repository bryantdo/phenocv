package test.ddpsc.phenocv.computer_vision_test;

import org.junit.Test;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.computer_vision.*;
import src.ddpsc.phenocv.debug.*;
import src.ddpsc.phenocv.debug.Readable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ShapeSuiteTest {

    @Test
    public void TestFromImage() {
        GrayImage shapesImage = new GrayImage(TestFiles.SHAPES);
        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);

        shapes.writeTo(TestFiles.TEST_RESULT_ROOT + "simple shape tight bounds.png");
    }

    @Test
    public void TestMask() {
        GrayImage shapesImage = new GrayImage(TestFiles.SHAPES);
        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);
        GrayImage mask = shapes.mask(shapesImage.size());

        mask.writeTo(TestFiles.TEST_RESULT_ROOT + "simple shape mask onto image.png");
    }

    @Test
    public void TestShapes() {
        GrayImage shapesImage = new GrayImage(TestFiles.SUBSHAPES);

        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);
        List<Shape> shapeList = shapes.shapes();
        for (int i = 0; i < shapeList.size(); i++) {
            Shape shape = shapeList.get(i);
            GrayImage shapeMask = shape.zeroedMask();
            shapeMask.writeTo(TestFiles.TEST_RESULT_ROOT + "shape tight bounds no. " + i + " from many shapes.png");

            GrayImage imageMask = shape.imageMask(shapesImage.size());
            imageMask.writeTo(TestFiles.TEST_RESULT_ROOT + "shape onto image no. " + i + " from many shapes.png");

            shapeMask.release();
        }
    }
}
