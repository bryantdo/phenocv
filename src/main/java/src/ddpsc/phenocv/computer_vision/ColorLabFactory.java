package src.ddpsc.phenocv.computer_vision;

import org.opencv.imgproc.Imgproc;

/**
 * @author cjmcentee
 */
final class ColorLabFactory extends ColorTypeFactory {

    ColorLabFactory() {
        super();
    }

    @Override
    protected int cvConversionValue() {
        return Imgproc.COLOR_BGR2Lab;
    }
}
