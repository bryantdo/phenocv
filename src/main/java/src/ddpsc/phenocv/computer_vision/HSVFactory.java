package src.ddpsc.phenocv.computer_vision;

import org.opencv.imgproc.Imgproc;

/**
* @author cjmcentee
*/
final class HSVFactory extends ColorTypeFactory {

    HSVFactory() {
        super();
    }

    @Override
    protected int cvConversionValue() {
        return Imgproc.COLOR_BGR2HSV;
    }
}
