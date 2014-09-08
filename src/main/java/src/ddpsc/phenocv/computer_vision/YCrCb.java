package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @author cjmcentee
 */
final class YCrCb {

    public static int CHANNEL_SIZE = 256;
    public static final float CHANNEL_MIN = 0;
    public static final float CHANNEL_MAX = 255;


    private Mat image_ycrcb;

    YCrCb() {
        image_ycrcb = null;
    }

    Mat from(Mat image_bgr) {
        if (image_ycrcb != null)
            return image_ycrcb;

        else {
            image_ycrcb = new Mat();
            Imgproc.cvtColor(image_bgr, image_ycrcb, Imgproc.COLOR_BGR2YCrCb, 3);
            return image_ycrcb;
        }
    }
}
