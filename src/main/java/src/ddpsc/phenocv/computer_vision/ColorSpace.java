package src.ddpsc.phenocv.computer_vision;

import org.opencv.imgproc.Imgproc;

/**
 * Represents the canonical and commonly used color spaces.
 *
 * BGR is good for general use.
 * HSV / HLS is good for separating colors from eachother
 * Lab is good for portioning images into visually distinct elements
 *
 * The rest are rather specialized and can be found online.
 *
 * @author cjmcentee
 */
public enum ColorSpace {
    HSV,
    BGR,
    Lab,
    YCrCb,
    Luv,
    XYZ,
    YUV,
    HLS;
}
