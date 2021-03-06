package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.debug.*;
import src.ddpsc.phenocv.debug.Readable;
import src.ddpsc.phenocv.utility.ReversableObjectFactory;

import java.util.Arrays;

/**
* @author cjmcentee
*/
class ShapeImageFactory extends ReversableObjectFactory<Mat, Shape> implements Releasable {

    // Display settings for drawing the shape onto a mask
    static final int DRAW_ALL = -1;
    static final int FILL_AREA = -1;
    static final int SOLID_PERIMETER = 8;
    static final int DRAW_ALL_CHILDREN = 2;
    static final Scalar BLACK = new Scalar(0);
    static final Scalar WHITE = new Scalar(255);

    private Shape shape;

    ShapeImageFactory(Shape shape) {
        super();
        this.shape = shape;
    }

    @Override
    public void release() {
       calculatedObject.release();
       calculatedObject = null;
    }

    @Override
    protected Mat calculate(Shape shape) {
        Rect boundingRect = shape.boundingBox();
        Point topLeft = boundingRect.tl();
        Point offset = new Point(-topLeft.x, -topLeft.y);

        Mat shapeImage = new Mat(shape.boundingBox().size(), CvType.CV_8UC1);

        shapeImage.setTo(BLACK); // initialization isn't wholly black, it has noise
        // I know you think this line is unnecessary and wasteful but you're wrong, trust me. Don't delete it
        // Otherwise you'll get weird noise in the image for no apparent reason

        Mat shapeHierarchy = shape.hierarchy();

        final int allContours = -1;
        Imgproc.drawContours(
                shapeImage,
                this.shape.contours(),
                allContours,
                WHITE,
                FILL_AREA,
                SOLID_PERIMETER,
                new Mat(),
                DRAW_ALL_CHILDREN,
                offset);

        shapeHierarchy.release();

        return shapeImage;
    }
}
