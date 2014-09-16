package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.utility.ReversableObjectFactory;

import java.util.List;

/**
 * @author cjmcentee
 */
class ShapeCollectionImageFactory extends ReversableObjectFactory<Mat, ShapeCollection> implements Releasable{

    ShapeCollectionImageFactory() {
        super();
    }

    @Override
    public void release() {
        calculatedObject.release();
        calculatedObject = null;
    }

    @Override
    protected Mat calculate(ShapeCollection shapeCollection) {
        Rect boundingRect = shapeCollection.boundingBox();
        Point topLeft = boundingRect.tl();
        Point offset = new Point(-topLeft.x, -topLeft.y);

        List<Shape> shapes = shapeCollection.shapes(); // references field, do not release
        Mat shapesImage = new Mat(boundingRect.size(), CvType.CV_8UC1);

        shapesImage.setTo(ShapeImageFactory.BLACK); // initialization isn't wholly black, it has noise
        // I know you think this line is unnecessary and wasteful but you're wrong, trust me. Don't delete it
        // Otherwise you'll get weird noise in the image for no apparent reason

        for (int i = 0; i < shapes.size(); i++) {
            Shape shape = shapes.get(i);

            Imgproc.drawContours(shapesImage,
                    shape.contours(),
                    ShapeImageFactory.DRAW_ALL,
                    ShapeImageFactory.WHITE,
                    ShapeImageFactory.FILL_AREA,
                    ShapeImageFactory.SOLID_PERIMETER,
                    new Mat(),
                    ShapeImageFactory.DRAW_ALL_CHILDREN,
                    offset);
        }

        return shapesImage;
    }
}
