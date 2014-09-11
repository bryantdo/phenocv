package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ShapeCollection implements Writable, Releasable {

    ShapeCollectionImageFactory shapesImage;
    ShapeListFactory shapesList;

    Mat hierarchy;
    List<MatOfPoint> contours;


    /// ======================================================================
    /// Constructors
    /// ======================================================================
    ShapeCollection(List<MatOfPoint> contours, Mat hierarchy) {
        this.contours = contours;
        this.hierarchy = hierarchy;
        shapesImage = new ShapeCollectionImageFactory(this);
        shapesList = new ShapeListFactory(this);
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
        if (contours.size() == 0)
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
        Mat subImage = image.submat(boundingBox()); // shallow copy, do not release
        Mat shape = toGrayMatrix(); // references factory field, do not release

        shape.copyTo(subImage); // subImage is shallow reference to image

        return new GrayImage(image);
    }


    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void release() {
        shapesImage.release();
        shapesList.release();
        hierarchy.release();
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
