package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a collection of shapes.
 *
 * Due to the need for clean, unambiguous shapes, shapes can only be taken from a
 * {@link GrayImage}. And even then it is preferred that they be taken from thresholded
 * {@link GrayImage}.
 *
 * @author cjmcentee
 */
public class ShapeCollection implements Writable, Releasable {

    ShapeCollectionImageFactory shapesImage;
    ShapeListFactory shapesList;

    // Used to build shapes list, otherwise not-necessary, can be null
    Mat hierarchy;
    List<MatOfPoint> contours;


    /// ======================================================================
    /// Constructors
    /// ======================================================================
    public ShapeCollection(List<Shape> shapes) {
        shapesList = new ShapeListFactory();
        shapesImage = new ShapeCollectionImageFactory();

        shapesList.setFrom(shapes);

        hierarchy = null;
        contours = null;
    }

    ShapeCollection(List<MatOfPoint> contours, Mat hierarchy) {
        this.contours = contours;
        this.hierarchy = hierarchy;
        shapesImage = new ShapeCollectionImageFactory();
        shapesList = new ShapeListFactory();
    }

    public static ShapeCollection FromImage(GrayImage image) {

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(image.image, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        return new ShapeCollection(contours, hierarchy);
    }


    /// ======================================================================
    /// Properties
    /// ======================================================================
    public List<Shape> shapes() {
        return shapesList.getFrom(this);
    }

    public Rect boundingBox() {
        if (contours == null) {
            if (! shapesList.calculated() || shapes().size() == 0)
                return new Rect();
        }
        else if (contours.size() == 0)
            return new Rect();

        List<Shape> shapes = shapes(); // references factory-field, do not release
        Point bottomRight = shapes.get(0).boundingBox().br();
        Point topLeft = shapes.get(0).boundingBox().tl();

        for (Shape shape : shapes) {
            Rect shapeBox = shape.boundingBox();
            bottomRight = new Point(
                    Math.max(shapeBox.br().x, bottomRight.x),
                    Math.max(shapeBox.br().y, bottomRight.y));
            topLeft = new Point(
                    Math.min(shapeBox.br().x, topLeft.x),
                    Math.min(shapeBox.br().y, topLeft.y));
        }

        return new Rect(bottomRight, topLeft);
    }

    public GrayImage zeroedGrayImage() {
        return new GrayImage(toGrayMatrix());
    }

    public GrayImage grayImage(Size imageSize) {
        Mat image = new Mat(imageSize, CvType.CV_8UC1);
        image.setTo(ShapeImageFactory.BLACK); // initialization isn't wholly black, it has noise
        // I know you think this line is unnecessary and wasteful but you're wrong, trust me. Don't delete it
        // Otherwise you'll get weird noise in the image for no apparent reason

        Mat subImage = image.submat(boundingBox()); // shallow copy, do not release
        Mat shape = toGrayMatrix(); // references factory field, do not release

        shape.copyTo(subImage); // subImage is shallow reference to image

        return new GrayImage(image);
    }

    public GrayImage mask(Size imageSize) {
        GrayImage image = grayImage(imageSize);
        image.threshold();

        return image;
    }


    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void release() {
        shapesImage.release();
        shapesList.release();
        if (hierarchy != null)
            hierarchy.release();
        if (contours != null)
            ReleaseContainer.releaseMatrices(contours);
    }


    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void writeTo(String filename) {
        Highgui.imwrite(filename, toGrayMatrix());
    }


    /// ======================================================================
    /// Helper Methods
    /// ======================================================================
    private Point bottomRight() {
        return boundingBox().br();
    }

    private Point topLeft() {
        return boundingBox().tl();
    }

    private Mat toGrayMatrix() {
        return shapesImage.getFrom(this);
    }
}
