package src.ddpsc.phenocv.computer_vision;

import org.opencv.imgproc.Imgproc;

/**
 * A morphological filter is a non-linear modifcation to a mask-type image
 * with only two color values (white vs black). It operates by moving the
 * center of a mask template block of pixels through every pixel in an image.
 * (Typically templates are in the range of 5x5 in size and are always masks.)
 * Pixels are then set to either white or black depending on whether the
 * template perfectly overlaps with the image, partially overlaps, or doesn't
 * overlap at all.
 *
 * Therefore the general parameters of any given morphological filter is
 * the size and values of the template and how pixels are changed when the template
 * matches the portion of the image it is iterating through.
 *
 * Types of supported morphological filters for this API:
 *
 * N.B. As always applied to white / black mask image
 *
 * Erode        shrinks white space
 * Dilate       grows white space
 * Open         removes edges of white space, removes white dots
 * Close        connects corners on white space, closes black dots
 * Gradient     difference between dilation and erosion (outline-ish)
 * Top Hat      difference between input and open
 *
 * @author cjmcentee
 */
public enum MorphType {
    ERODE       (Imgproc.MORPH_ERODE),
    DILATE      (Imgproc.MORPH_DILATE),
    OPEN        (Imgproc.MORPH_OPEN),
    CLOSE       (Imgproc.MORPH_CLOSE),
    GRADIENT    (Imgproc.MORPH_GRADIENT),
    TOP_HAT     (Imgproc.MORPH_TOPHAT),
    BLACK_HAT   (Imgproc.MORPH_BLACKHAT);

    private int morphType;

    MorphType(int morphType) {
        this.morphType = morphType;
    }

    public int type() {
        return morphType;
    }
}
