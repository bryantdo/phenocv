package src.ddpsc.phenocv.program_execution;

import src.ddpsc.phenocv.algorithms.ColorIsolation;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.concurrent.Callable;

/**
 * User: bryantd
 * Date: 9/18/14
 * Time: 10:36 AM
 * Description:
 */
public class ImageProcessorCallable implements Callable <Tuple<String, ColorImage>> {

  private final ColorIsolation colorIsolation;
  private final String imageName;
  private final ColorImage colorImage;

  public ImageProcessorCallable(ColorIsolation colorIsolation, Tuple<String, ColorImage> imageTupleToIsolate) {
    this.colorIsolation = colorIsolation;
    this.imageName = imageTupleToIsolate.item1;
    this.colorImage = imageTupleToIsolate.item2;
  }

  @Override
  public Tuple<String, ColorImage> call() throws Exception {
    ColorImage imageCopy = (ColorImage)colorImage.copy();
    colorIsolation.fastIsolation(imageCopy);
    return new Tuple<String, ColorImage>(imageName, imageCopy);
  }
}
