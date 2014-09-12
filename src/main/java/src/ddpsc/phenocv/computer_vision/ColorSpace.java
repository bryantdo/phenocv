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

    /**
     * Returns a convert code converting the origin type into the destination type
     *
     * Not all convert codes exist. This is only partially implemented. If anyone
     * ever needs to convert from XYZ to YUV, then it can be implemented. Yeesh.
     *
     * @param originSpace           origin color space
     * @param destinationSpace      destination color space
     * @return                      open cv code for color space conversion
     */
    public static int convertCode(ColorSpace originSpace, ColorSpace destinationSpace) {
        if (originSpace == destinationSpace)
            return -1;

        switch (originSpace) {
            case BGR:
                switch (destinationSpace) {
                    case HSV:       return Imgproc.COLOR_BGR2HSV;
                    default:        return -1;
                }
            case HSV:
                switch (destinationSpace) {
                    case BGR:       return Imgproc.COLOR_HSV2BGR;
                    default:        return -1;
                }
            default:
                return -1;
        }
    }
}
