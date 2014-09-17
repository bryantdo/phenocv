package src.ddpsc.phenocv.program_execution;

import src.ddpsc.phenocv.algorithms.ColorIsolation;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.ColorSpace;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.computer_vision.HistogramPartition;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.List;


/**
 * User: bryantd
 * Date: 9/17/14
 * Time: 4:03 PM
 * Description:
 */
public class ImageProcessor {

  List<ColorImage> processImageSet;

  public ImageProcessor(LoadedImages loadedImages) {
    processImageSet = loadedImages.processImageSet;

    List<Tuple<ColorImage, GrayImage>> trainingImageSet = loadedImages.trainingImageSet;
    ColorIsolation colorIsolation = new ColorIsolation(new HistogramPartition(ColorSpace.HSV, 100));
    colorIsolation.train(trainingImageSet);
  }

  private boolean isolateImage(ColorImage image) {
    boolean success = false;
    

    return success;
  }


}
