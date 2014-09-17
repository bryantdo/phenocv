package src.ddpsc.phenocv;

import src.ddpsc.phenocv.program_execution.CommandLineArgs;
import src.ddpsc.phenocv.program_execution.LoadedImages;
import src.ddpsc.phenocv.utility.OpenCV;

/**
 * User: bryantd
 * Date: 9/17/14
 * Time: 10:19 AM
 * Description: Application's main CLI entry point.
 */
public class main {

  static {
    OpenCV.Load();
  }

  public static void main(String[] args) {
    CommandLineArgs commandLineArgs = new CommandLineArgs(args);
    if(args.length < 6 || !commandLineArgs.verifiedOptions) {
      commandLineArgs.printHelp(commandLineArgs.constructOptions(), 80, "How to use phenocv:", "Copyright 2014", 4, 5, true, System.out);
    } else {
      String trainingDirectory = commandLineArgs.trainingDirectory;
      String processDirectory = commandLineArgs.processDirectory;
      String outputDirectory = commandLineArgs.outputDirectory;
      LoadedImages imageProcessor = new LoadedImages(trainingDirectory, processDirectory, outputDirectory);
    }
  }

}
