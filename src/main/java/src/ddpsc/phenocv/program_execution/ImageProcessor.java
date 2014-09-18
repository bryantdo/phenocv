package src.ddpsc.phenocv.program_execution;

import src.ddpsc.phenocv.algorithms.ColorIsolation;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.ColorSpace;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.computer_vision.HistogramPartition;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.Collections.synchronizedList;


public class ImageProcessor {
  private final List<Tuple<String, ColorImage>> imagesToProcess;
  private final List<Tuple<String, ColorImage>> resultsImagesTuples;
  private final ColorIsolation colorIsolation;
  private final int numThreads;

  public ImageProcessor(ImagesIO loadedImages, int numThreads) {
    this.numThreads = numThreads;
    imagesToProcess = synchronizedList(loadedImages.processImageSet);
    resultsImagesTuples = synchronizedList(new ArrayList<Tuple<String, ColorImage>>());
    List<Tuple<ColorImage, GrayImage>> trainingImageSet = loadedImages.trainingImageSet;
    colorIsolation = new ColorIsolation(new HistogramPartition(ColorSpace.HSV, 100));
    colorIsolation.train(trainingImageSet);
  }

  public List<Tuple<String, ColorImage>> getProcessedImages() throws Exception {
    if(imagesToProcess.size() != resultsImagesTuples.size()) {
      ExecutorService pool = Executors.newFixedThreadPool(numThreads);
      List<Future<Tuple<String, ColorImage>>> results = new ArrayList<Future<Tuple<String, ColorImage>>>();
      for(Tuple<String, ColorImage> imageTuple : imagesToProcess) {
        Callable<Tuple<String, ColorImage>> callable = new ImageProcessorCallable(colorIsolation, imageTuple);
        Future<Tuple<String, ColorImage>> futureImageTuple = pool.submit(callable);
        results.add(futureImageTuple);
      }
      for(Future<Tuple<String, ColorImage>> result : results) {
        addImageToResults(result.get());
      }
      pool.shutdown();
      try { pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); }
      catch(Exception e) { throw new Exception(e); }
    }
    return resultsImagesTuples;
  }

  private void addImageToResults(Tuple<String, ColorImage> imageTuple) {
    synchronized(resultsImagesTuples) {
      resultsImagesTuples.add(imageTuple);
      resultsImagesTuples.notifyAll();
    }
  }
}
