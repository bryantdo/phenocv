package src.ddpsc.phenocv.utility;

import org.opencv.core.Mat;

/**
 * @author cjmcentee
 */
public class Copy {

    public static Mat matrix(Mat m) {
        Mat copyMatrix = new Mat();
        m.copyTo(copyMatrix);
        return copyMatrix;
    }
}
