package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import src.ddpsc.phenocv.utility.CalculatedObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
* @author cjmcentee
*/
final class ListMatOfPoint2fFactory extends CalculatedObjectFactory<List<MatOfPoint2f>, List<MatOfPoint>> implements Releasable{

    ListMatOfPoint2fFactory() {
        super();
    }

    @Override
    public void release() {
        ReleaseContainer.releaseMatrices(calculatedObject);
    }

    @Override
    protected List<MatOfPoint2f> calculate(List<MatOfPoint> seed) {
        List<MatOfPoint2f> contours = new ArrayList<MatOfPoint2f>(seed.size());
        for (MatOfPoint seedContour : seed)
            contours.add(new MatOfPoint2f(seedContour));
        return contours;
    }
}
