package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG;

/**
 * @author cjmcentee
 */
public class BackgroundModel {

    BackgroundSubtractor model;

    public BackgroundModel() {
        model = new BackgroundSubtractorMOG();
    }

    /**
     * Adds another image to the background model's internal representation
     * of the background of the system.
     *
     * It is recommended that training be done with images of all one type.
     * Combining color and grayscale images will yield unexpected results.
     *
     * @param backgroundImage       image to train the model with
     */
    public void addBackgroundImageToModel(Image backgroundImage) {
        model.apply(backgroundImage.image, new Mat(), 10);
    }

    /**
     * Gets a mask representing the pixels that this model computes to belong
     * to the foreground of the input image.
     *
     * It is recommended that the image to get the mask from is of the same type
     * (Color vs Gray) as the training images.
     *
     * @param image     image to get foreground mask of
     * @return          foreground mask of the image
     */
    public GrayImage getForegroundMaskFromImage(Image image) {
        Mat foregroundMask = new Mat();
        model.apply(image.image, foregroundMask, 0); // 0 is no training
        return new GrayImage(foregroundMask);
    }

    /**
     * Masks the input image, leaving only what this model considers foreground
     * visible.
     *
     * Pixels considered part of the background are set to black.
     *
     * It is recommended that the image to mask is of the same type (Color vs Gray)
     * as the training images.
     *
     * @param image     image to remove background from
     */
    public void getForegroundOfImage(Image image) {
        GrayImage foregroundMask = getForegroundMaskFromImage(image);
        image.maskWith(foregroundMask);
    }
}
