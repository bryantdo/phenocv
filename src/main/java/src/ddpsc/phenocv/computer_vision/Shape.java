package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class Shape implements Writable, Releasable {

    static final int NEXT = 0;
    static final int PREVIOUS = 1;
    static final int FIRST_CHILD = 2;
    static final int PARENT = 3;


    ShapeImageFactory shapeImage;

    MatOfPoint outerContour;
    MatOfPoint2fFactory outerContourFloat;

    List<MatOfPoint> innerContours;
    ListMatOfPoint2fFactory innerContoursFloat;


    /// ======================================================================
    /// Constructors
    /// ======================================================================
    Shape(MatOfPoint outerContour) {
        this(outerContour, new ArrayList<MatOfPoint>());
    }

    Shape(MatOfPoint outerContour, List<MatOfPoint> innerContours) {
        shapeImage = new ShapeImageFactory(this);

        this.outerContour = outerContour;
        outerContourFloat = new MatOfPoint2fFactory();

        this.innerContours = innerContours;
        innerContoursFloat = new ListMatOfPoint2fFactory();
    }

    /// ======================================================================
    /// Properties
    /// ======================================================================
    public double area() {
        double outerArea = Imgproc.contourArea(outerContour);
        double area = outerArea;

        for (MatOfPoint innerContour : innerContours)
            outerArea -= Imgproc.contourArea(innerContour);

        return area;
    }

    public double perimeter() {
        return Imgproc.arcLength(outerContourFloat(), true);
    }

    public int numberPixels() {
        return Core.countNonZero(toBinaryMatrix());
    }

    public Mask zeroedMask() {
        return new Mask(toBinaryMatrix());
    }

    public Mask imageMask(Size imageSize) {
        Mat mask = new Mat(imageSize, CvType.CV_8UC1);
        Mat subMask = mask.submat(boundingBox()); // does not need release, shallow copy
        Mat shape = toBinaryMatrix(); // references factory-built field, do not release

        shape.copyTo(subMask); // subMask is shallow reference to mask

        return new Mask(mask);
    }

    public Rect boundingBox() {
        Rect boundingBox = Imgproc.boundingRect(outerContour);
        return boundingBox;
    }

    Mat hierarchy() {
        MatOfInt4 hierarchy = new MatOfInt4();
        // 4 entries per relation, entry values:
        //      next, previous, first child, parent
        int relations[] = new int[4*(innerContours.size() + 1)];

        // outer contour
        relations[0] = -1;
        relations[1] = -1;
        relations[2] =  1;
        relations[3] = -1;

        // inner contours
        for (int i = 0; i < innerContours.size(); i++) {
            // next
            if (i != innerContours.size() - 1)
                relations[4*i + 4] =  i + 2; // next child element
            else
                relations[4*i + 4] = -1; // no next for last child

            // previous
            if (i == 0)
                relations[4*i + 5] = -1; // no previous for first child
            else
                relations[4*i + 5] =  i; // previous child element

            relations[4*i + 6] = -1; // no children
            relations[4*i + 7] =  0; // parent is first row
        }

        hierarchy.alloc(relations.length);
        hierarchy.put(0, 0, relations);
        return hierarchy;
    }


    /// ======================================================================
    /// Writable
    /// ======================================================================
    @Override
    public void writeTo(String filename) {
        Highgui.imwrite(filename, toBinaryMatrix());
    }


    /// ======================================================================
    /// Releasable
    /// ======================================================================
    @Override
    public void release() {
        shapeImage.release();
        outerContour.release();
        outerContour.release();
        ReleaseContainer.releaseMatrices(innerContours);
        innerContoursFloat.release();
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

    List<MatOfPoint> contourList() {
        List<MatOfPoint> contourList = new ArrayList<MatOfPoint>();
        contourList.add(outerContour);
        contourList.addAll(innerContours);
        return contourList;
    }

    private Mat toBinaryMatrix() {
        return shapeImage.getFrom(this);
    }

    private MatOfPoint2f outerContourFloat() {
        return outerContourFloat.getFrom(outerContour);
    }

    private List<MatOfPoint2f> innerContoursFloat() {
        return innerContoursFloat.getFrom(innerContours);
    }
}


