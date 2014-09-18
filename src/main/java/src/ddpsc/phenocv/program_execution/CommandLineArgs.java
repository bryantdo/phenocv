package src.ddpsc.phenocv.program_execution;

import org.apache.commons.cli.*;
import src.ddpsc.phenocv.utility.OptionComparator;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * User: bryantd
 * Date: 9/17/14
 * Time: 2:08 PM
 * Description:
 */
public class CommandLineArgs {

  public String applicationName;
  public String trainingDirectory;
  public String processDirectory;
  public String outputDirectory;
  public int numThreads;
  public boolean verifiedOptions;
  private Options options;

  public CommandLineArgs(String[] commandLineArgs) {
    applicationName = "phenocv";
    options = constructOptions();
    verifiedOptions = parseArguments(commandLineArgs);
  }

  /**
   * Construct and provide Posix-compatible Options.
   *
   * @return Options expected from command-line of Posix form.
   */
  public Options constructOptions() {
    final Options options = new Options();
    int cores = Runtime.getRuntime().availableProcessors();
    Option trainingDirectory = OptionBuilder.withArgName("trainingDirectory")
      .withLongOpt("trainingDirectory")
      .hasArg()
      .withType(String.class)
      .withDescription("Directory containing images + masks used to train classifier. Files should be named " +
      "in pairs like file.png file_mask.png")
      .create("t");
    Option processDirectory = OptionBuilder.withArgName("processDirectory")
      .withLongOpt("processDirectory")
      .hasArg()
      .withType(String.class)
      .withDescription("Directory containing images to process.")
      .create("p");
    Option outputDirectory = OptionBuilder.withArgName("outputDirectory")
      .withLongOpt("outputDirectory")
      .hasArg()
      .withType(String.class)
      .withDescription("Directory in which to output processed images.")
      .create("o");
    Option numThreads = OptionBuilder.withArgName("numThreads")
      .withLongOpt("numThreads")
      .hasArg()
      .withType(Number.class)
      .withDescription("Number of threads to use when processing images. Defaults to the number " +
        "of cores in your system = " + cores + ".")
      .create("n");
    options.addOption(trainingDirectory);
    options.addOption(processDirectory);
    options.addOption(outputDirectory);
    options.addOption(numThreads);
    return options;
  }

  private boolean parseArguments(final String[] commandLineArguments) {
    final PosixParser cmdLineParser = new PosixParser();
    final Options options = constructOptions();
    boolean verifiedParameters = false;
    CommandLine commandLine;
    try {
      commandLine = cmdLineParser.parse(options, commandLineArguments);
      boolean hasT = false, hasP = false, hasO = false;
      if(commandLine.hasOption("t")) {
        hasT = true;
        trainingDirectory = commandLine.getOptionValue("t");
      } else {
        System.out.println("Please specify training directory.");
      }
      if(commandLine.hasOption("p")) {
        hasP = true;
        processDirectory = commandLine.getOptionValue("p");
      } else {
        System.out.println("Please specify process directory.");
      }
      if(commandLine.hasOption("o")) {
        hasO = true;
        outputDirectory = commandLine.getOptionValue("o");
      } else {
        System.out.println("Please specify output directory.");
      }
      String threads = commandLine.getOptionValue("n", new Integer(Runtime.getRuntime().availableProcessors()).toString());
      numThreads = Integer.parseInt(threads);
      if(hasT && hasP && hasO) {
        verifiedParameters = true;
      }
    }
    catch (ParseException parseException) {
      System.err.println("Encountered exception while parsing command line arguments:\n"
        + parseException.getMessage() );
    }
    return verifiedParameters;
  }

  /**
   * Print usage information to provided OutputStream.
   *
   * @param applicationName Name of application to list in usage.
   * @param options Command-line options to be part of usage.
   * @param out OutputStream to which to write the usage information.
   */
  public void printUsage(final String applicationName,
                                 final Options options,
                                 final OutputStream out) {
    final PrintWriter writer = new PrintWriter(out);
    final HelpFormatter usageFormatter = new HelpFormatter();
    usageFormatter.printUsage(writer, 80, applicationName, options);
    writer.flush();
  }

  /**
   * Write "help" to the provided OutputStream.
   */
  public void printHelp(final Options options,
                                final int printedRowWidth,
                                final String header,
                                final String footer,
                                final int spacesBeforeOption,
                                final int spacesBeforeOptionDescription,
                                final boolean displayUsage,
                                final OutputStream out) {
    final String commandLineSyntax = "java -jar plantcv.jar";
    final PrintWriter writer = new PrintWriter(out);
    final HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.setOptionComparator(new OptionComparator());
    helpFormatter.printHelp(
      writer,
      printedRowWidth,
      commandLineSyntax,
      header,
      options,
      spacesBeforeOption,
      spacesBeforeOptionDescription,
      footer,
      displayUsage);
    writer.flush();
  }



}
