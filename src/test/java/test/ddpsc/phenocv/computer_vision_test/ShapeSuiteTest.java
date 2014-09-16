package test.ddpsc.phenocv.computer_vision_test;

import org.junit.Test;
import org.opencv.core.Core;
import src.ddpsc.phenocv.computer_vision.*;
import src.ddpsc.phenocv.utility.OpenCV;

import java.util.List;

/**
 * @author cjmcentee
 */
public class ShapeSuiteTest {

    static {
        OpenCV.Load();
    }

    @Test
    public void TestFromImage() {
        GrayImage shapesImage = new GrayImage(TestFiles.SHAPE);
        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);

        shapes.writeTo(TestFiles.TEST_RESULT_ROOT + "simple_shape_tight_bounds.png");
    }

    @Test
    public void TestShapesDraw() {
        GrayImage shapesImage = new GrayImage(TestFiles.SUBSHAPES);
        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);

        GrayImage shapesMask = shapes.mask(shapesImage.size());
        shapesMask.writeTo(TestFiles.TEST_RESULT_ROOT + "all_shapes_onto_image.png");
    }

    @Test
    public void TestShapesMask() {
        GrayImage shapesImage = new GrayImage(TestFiles.SHAPE);
        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);
        GrayImage mask = shapes.mask(shapesImage.size());

        mask.writeTo(TestFiles.TEST_RESULT_ROOT + "simple_shape_mask_onto_image.png");
    }

    @Test
    public void TestShapesMaskAverageValues() {
        GrayImage shapesImage = new GrayImage(TestFiles.SUBSHAPES);
        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);
        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);

        ColorImage masked = shapes.maskWithAverageValues(gradient);
        masked.writeTo(TestFiles.TEST_RESULT_ROOT + "shapes_masked_with_average_values.png");
    }

    @Test
    public void TestShapeMask() {
        GrayImage shapesImage = new GrayImage(TestFiles.SUBSHAPES);

        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);
        List<Shape> shapeList = shapes.shapes();
        for (int i = 0; i < shapeList.size(); i++) {
            Shape shape = shapeList.get(i);
            GrayImage shapeMask = shape.zeroedMask();
            shapeMask.writeTo(TestFiles.TEST_RESULT_ROOT + "shape_tight_bounds_no._" + i + "_from_many_shapes.png");

            GrayImage imageMask = shape.imageMask(shapesImage.size());
            imageMask.writeTo(TestFiles.TEST_RESULT_ROOT + "shape_onto_image_no._" + i + "_from_many_shapes.png");

            shapeMask.release();
        }
    }

    @Test
    public void TestMaskWith() {
        GrayImage shapesImage = new GrayImage(TestFiles.SHAPE);
        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);
        Shape shape = shapes.shapes().get(0); // only 1 shape in SHAPE

        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);
        gradient.maskWith(shape);

        gradient.writeTo(TestFiles.TEST_RESULT_ROOT + "simple_shape_mask.png");
    }

    @Test
    public void TestAveragePixel() {
        GrayImage shapesImage = new GrayImage(TestFiles.SHAPE);
        ShapeCollection shapes = ShapeCollection.FromImage(shapesImage);
        Shape shape = shapes.shapes().get(0); // only 1 shape in SHAPE

        ColorImage gradient = new ColorImage(TestFiles.GRADIENT);
        ColorPixel average = shape.averagePixelOf(gradient);

        ColorImage averageColor = new ColorImage(shapesImage.size(), average);
        averageColor.maskWith(shape);

        averageColor.writeTo(TestFiles.TEST_RESULT_ROOT + "simple_shape_average_color_mask.png");
    }
}
