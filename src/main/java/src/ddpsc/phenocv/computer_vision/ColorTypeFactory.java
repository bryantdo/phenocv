package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import src.ddpsc.phenocv.computer_vision.Releasable;
import src.ddpsc.phenocv.utility.ReversableObjectFactory;

/**
 * @author cjmcentee
 */
abstract class ColorTypeFactory extends ReversableObjectFactory<Mat, Mat> implements Releasable {

    protected ColorTypeFactory() {
        super();
    }

    @Override
    public void release() {
        if (calculatedObject != null)
            calculatedObject.release();
    }

    @Override
    protected Mat calculate(Mat seed) {
      System.out.println("is seed empty: " + seed.empty());
      calculatedObject = new Mat();
      Imgproc.cvtColor(seed, calculatedObject, cvConversionValue(), 3);
      return calculatedObject;
    }

    protected abstract int cvConversionValue();
}
