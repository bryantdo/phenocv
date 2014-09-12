package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.utility.ReversableObjectFactory;

import java.util.List;

/**
 * @author cjmcentee
 */
public class ShapeCollectionImageFactory extends ReversableObjectFactory<Mat, ShapeCollection> implements Releasable{

    private ShapeCollection shapeCollection;

    ShapeCollectionImageFactory(ShapeCollection shapeCollection) {
        super();
        this.shapeCollection = shapeCollection;
    }

    @Override
    public void release() {
        calculatedObject.release();
    }

    @Override
    protected Mat calculate(ShapeCollection shapeCollection) {
        Rect boundingRect = shapeCollection.boundingBox();
        Point topLeft = boundingRect.tl();
        Point offset = new Point(-topLeft.x, -topLeft.y);

        List<Shape> shapes = shapeCollection.shapes(); // references field, do not release
        Mat shapesMatrix = new Mat(shapeCollection.boundingBox().size(), CvType.CV_8U);

        // Goes to shit if there are more than 254 shapes
        // Don't have more than 254 shapes, otherwise draws shapes at different non-black gray scales
        int grayStep = 254 / shapes.size();
        for (int i = 0; i < shapes.size(); i++) {
            Shape shape = shapes.get(i);
            Scalar grayShade = new Scalar(i*grayStep + 1);

            Imgproc.drawContours(shapesMatrix, shape.contourList(), ShapeImageFactory.DRAW_ALL, grayShade);
        }

        return shapesMatrix;
    }
}
