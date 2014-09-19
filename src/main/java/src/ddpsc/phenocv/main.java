package src.ddpsc.phenocv;

import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.program_execution.CommandLineArgs;
import src.ddpsc.phenocv.program_execution.ImageProcessor;
import src.ddpsc.phenocv.program_execution.ImagesIO;
import src.ddpsc.phenocv.utility.OpenCV;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.List;

/**
 * User: bryantd
 * Date: 9/17/14
 * Time: 10:19 AM
 * Description: Application's main CLI entry point.
 */
public class main {

  public static void main(String[] args) throws InterruptedException {

    // Exits the program if the library fails to load
    OpenCV.load();

    CommandLineArgs commandLineArgs = new CommandLineArgs(args);
    if(args.length < 6 || !commandLineArgs.verifiedOptions) {
      commandLineArgs.printHelp(commandLineArgs.constructOptions(), 80, "", "Note: Allowed image " +
        "extensions include [jpg, jpeg, tiff, png]\nCopyright 2014", 4, 5, true, System.out);
    } else {
      String outputDirectory = commandLineArgs.outputDirectory;

      try {
        ImagesIO imagesIO = new ImagesIO(commandLineArgs.trainingDirectory,
          commandLineArgs.processDirectory,
          commandLineArgs.outputDirectory);
        ImageProcessor imageProcessor = new ImageProcessor(imagesIO, commandLineArgs.numThreads);
        List<Tuple<String, ColorImage>> resultsImagesTuples = imageProcessor.getProcessedImages();
        imagesIO.writeProcessedImages(resultsImagesTuples);
        System.out.println("Wrote " + resultsImagesTuples.size() + " images to " + outputDirectory);
      } catch(Exception e) {
        System.out.println("Unable to proceed: " + e.getMessage());
        System.exit(0);
      }
    }
  }
}
