package src.ddpsc.phenocv.algorithms;

import src.ddpsc.phenocv.computer_vision.HistogramPartition;
import src.ddpsc.phenocv.computer_vision.*;
import src.ddpsc.phenocv.utility.Lists;
import src.ddpsc.phenocv.utility.Tuple;

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

            histogram.addImageData(histogramPartition, image, mask);
        }

        trained = true;
    }

    public void trainFiles(List<Tuple<String, String>> trainingFiles) {
        train(Lists.loadMaskedImagePairs(trainingFiles));
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
    /// Releasable
    /// ======================================================================
    @Override
    public void release() {
        histogram.release();
        trained = false;
    }
}
