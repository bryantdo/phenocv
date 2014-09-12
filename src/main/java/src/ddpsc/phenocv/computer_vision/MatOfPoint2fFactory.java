package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import src.ddpsc.phenocv.utility.ReversableObjectFactory;

/**
* @author cjmcentee
*/
final class MatOfPoint2fFactory extends ReversableObjectFactory<MatOfPoint2f, MatOfPoint> implements Releasable{

    MatOfPoint2fFactory() {
        super();
    }

    @Override
    public void release() {
        calculatedObject.release();
    }

    @Override
    protected MatOfPoint2f calculate(MatOfPoint seed) {
        MatOfPoint2f contour = new MatOfPoint2f(seed);
        return contour;
    }
}
