package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

/**
* @author cjmcentee
*/
final class HSV {

    public static int CHANNEL_SIZE = 256;
    public static final float CHANNEL_MIN = 0;
    public static final float CHANNEL_MAX = 255;


    private Mat image_hsv;

    HSV() {
        image_hsv = null;
    }

    Mat from(Mat image_bgr) {
        if (image_hsv != null)
            return image_hsv;

        else {
            image_hsv = new Mat();
            Imgproc.cvtColor(image_bgr, image_hsv, Imgproc.COLOR_BGR2HSV, 3);
            return image_hsv;
        }
    }
}
