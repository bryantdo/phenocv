package src.ddpsc.phenocv.algorithms;

import src.ddpsc.phenocv.computer_vision.HistogramPartition;
import src.ddpsc.phenocv.computer_vision.*;
import src.ddpsc.phenocv.utility.Lists;
import src.ddpsc.phenocv.utility.Tuple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ColorIsolation implements Releasable {

    private static final int MEDIAN_STRENGTH = 5;

    boolean trained = false;
    HistogramPartition histogramPartition;
    Histogram histogram;

    public ColorIsolation(HistogramPartition histogramPartition) {
        this.histogramPartition = histogramPartition;
        histogram = Histogram.blank(histogramPartition);
    }

    public void train(List<Tuple<ColorImage, GrayImage>> trainingImages) {
        if (trainingImages.size() == 0)
            return;

        for (Tuple<ColorImage, GrayImage> trainingPair : trainingImages) {
            ColorImage image = trainingPair.item1;
            GrayImage mask = trainingPair.item2;

            histogram.addImageData(image, mask);
        }

        trained = true;
    }

    public void trainByFiles(List<Tuple<String, String>> trainingFiles) {

        if (trainingFiles.size() == 0)
            return;

        for (Tuple<String, String> pair : trainingFiles) {
            ColorImage image = new ColorImage(pair.item1);
            GrayImage mask = new GrayImage(pair.item2);

            histogram.addImageData(image, mask);

            image.release();
            mask.release();
        }
    }

    public void fastIsolation(ColorImage image) {
        if (trained == false)
            return;

        else {
            GrayImage backProjection = histogram.backProjectionOf(image);
            GrayImage mask = (GrayImage) backProjection.copy();

            mask.threshold();
            mask.medianFilter(MEDIAN_STRENGTH);

            ShapeCollection shapes = ShapeCollection.FromImage(mask);
            List<Shape> allShapes = shapes.shapes();
            List<Shape> keepShapes = new ArrayList<Shape>();

            for (Shape shape : allShapes) {
                ColorPixel averagePixel = shape.averagePixelOf(image);

                if (shape.numberPixels() > 250 && averagePixel.isGreen())
                    keepShapes.add(shape);
            }

            ShapeCollection keptShapes = new ShapeCollection(keepShapes);
            GrayImage shapesMask = keptShapes.mask(image.size());

            image.maskWith(shapesMask);

            backProjection.release();
            mask.release();
            shapesMask.release();
        }
    }

    public Tuple<Image, ColorImage> debugIsolation(ColorImage image) {
        if (trained == false)
            return new Tuple<Image, ColorImage>(GrayImage.empty(), image);

        else {
            GrayImage backProjection = histogram.backProjectionOf(image);
            GrayImage mask = (GrayImage) backProjection.copy();
            mask.threshold();

            mask.medianFilter(MEDIAN_STRENGTH);
            ShapeCollection shapes = ShapeCollection.FromImage(mask);
            List<Shape> allShapes = shapes.shapes();
            List<Shape> keepShapes = new ArrayList<Shape>();

            for (Shape shape : allShapes) {
                ColorPixel averagePixel = shape.averagePixelOf(image);

                if (shape.numberPixels() > 250 && averagePixel.isGreen())
                    keepShapes.add(shape);
            }

            ShapeCollection keptShapes = new ShapeCollection(keepShapes);
            GrayImage shapesMask = keptShapes.mask(image.size());

            ColorImage copy = (ColorImage) image.copy();
            copy.maskWith(shapesMask);

            mask.release();
            shapesMask.release();
            backProjection.release();

            return new Tuple<Image, ColorImage>(shapes.maskWithAverageValues(image), copy);
        }
    }


    /// ======================================================================
    /// Execution Entry Point
    /// ======================================================================

    /**
     * The one method call entry point to this algorithm.
     *
     * trainFiles should be a list of paired filenames, the first filename being the unmodified test
     * image, the second file being a mask to determine which pixels of that test image are to be
     * trained on.
     *
     * The two images should be of the same dimensions. The non-black pixels of the mask indicate
     * the mask is to include the corresponding pixels of the test image in trianing, all black pixels
     * indicate their corresponding pixels aren't to be trained on.
     *
     * See: {project dir}/resources/images/color_isolation_training
     *
     * The process files are the files to work on and isolate based on the training images.
     *
     * @param trainFiles        tuple-list of training images
     * @param processFiles      list of files to run the algorithm on
     * @param outputDirectory   directory to output processed files to
     */
    public static void Execute(List<Tuple<String, String>> trainFiles, List<String> processFiles, String outputDirectory) {
        // 100 bins out of 180 possible values, compressing the color space makes for better results
        // as observed by testing
        HistogramPartition halfSizedHSVHistogram = new HistogramPartition(ColorSpace.HSV, 100);
        ColorIsolation isolator = new ColorIsolation(halfSizedHSVHistogram);

        isolator.trainByFiles(trainFiles);

        for (String filename : processFiles) {
            File file = new File(filename);

            if (file.exists() && ! file.isDirectory()) {

                ColorImage image = new ColorImage(filename);
                isolator.fastIsolation(image);

                String name = file.getName();
                image.writeTo(outputDirectory + "/" + name);
                image.release();
            }
        }
    }


    /// ======================================================================
    /// Releasable
    /// ======================================================================
    @Override
    public void release() {
        histogram.release();
        trained = false;
    }
}
