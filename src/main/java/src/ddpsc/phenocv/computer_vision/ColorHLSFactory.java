package src.ddpsc.phenocv.computer_vision;

import org.opencv.imgproc.Imgproc;

/**
 * @author cjmcentee
 */
final class ColorHLSFactory extends ColorTypeFactory {

    ColorHLSFactory() {
        super();
    }

    @Override
    protected int cvConversionValue() {
        return Imgproc.COLOR_BGR2HLS;
    }
}