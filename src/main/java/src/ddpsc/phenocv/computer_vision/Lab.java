package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @author cjmcentee
 */
final class Lab {

    public static int CHANNEL_SIZE = 256;
    public static final float CHANNEL_MIN = 0;
    public static final float CHANNEL_MAX = 255;


    private Mat image_lab;

    Lab() {
        image_lab = null;
    }

    Mat from(Mat image_bgr) {
        if (image_lab != null)
            return image_lab;

        else {
            image_lab = new Mat();
            Imgproc.cvtColor(image_bgr, image_lab, Imgproc.COLOR_BGR2Lab, 3);
            return image_lab;
        }
    }
}
