package src.ddpsc.phenocv.algorithms;

import src.ddpsc.phenocv.computer_vision.Channel;
import src.ddpsc.phenocv.computer_vision.*;
import src.ddpsc.phenocv.utility.Lists;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ColorIsolation implements Releasable {

    boolean trained = false;
    Channel channel;
    Histogram histogram;

    public ColorIsolation(Channel channel) {
        this.channel = channel;
        histogram = Histogram.blank(channel);
    }

    public void train(List<Tuple<ColorImage, GrayImage>> trainingImages) {
        if (trainingImages.size() == 0)
            return;

        for (Tuple<ColorImage, GrayImage> trainingPair : trainingImages) {
            ColorImage image = trainingPair.item1;
            GrayImage mask = trainingPair.item2;

            histogram.addImageData(channel, image, mask);
        }

        trained = true;
    }

    public void trainFiles(List<Tuple<String, String>> trainingFiles) {
        train(Lists.loadMaskedImagePairs(trainingFiles));
    }

    public ColorImage isolate(ColorImage image) {
        if (trained == false)
            return image;

        else {
            Tuple<GrayImage, ColorImage> isolation = __doIsolate(image);
            isolation.item1.release();
            return isolation.item2;
        }
    }

    public Tuple<GrayImage, ColorImage> detailedIsolation(ColorImage image) {
        if (trained == false)
            return new Tuple<GrayImage, ColorImage>(GrayImage.empty(), image);

        else {
            return __doIsolate(image);
        }
    }

    private Tuple<GrayImage, ColorImage> __doIsolate(ColorImage image) {

        GrayImage backProjection = histogram.backProjectionOf(image);
        GrayImage mask = (GrayImage) backProjection.copy();
        mask.threshold();

        mask.medianFilter(15);
        /*ShapeCollection shapes = ShapeCollection.FromImage(mask);
        List<Shape> allShapes = shapes.shapes();
        List<Shape> keepShapes = new ArrayList<Shape>();

        int i = 0;
        for (Shape shape : allShapes) {
            System.out.println(i++);
            ColorPixel averagePixel = shape.averagePixelOf(image);
            if (averagePixel.isGreen())
                keepShapes.add(shape);
        }

        ShapeCollection keptShapes = new ShapeCollection(keepShapes);
        GrayImage shapesMask = keptShapes.mask(image.size());*/

        ColorImage copy = (ColorImage) image.copy();
        copy.maskWith(mask);

        mask.release();
        //shapesMask.release();

        return new Tuple<GrayImage, ColorImage>(backProjection, copy);
    }


    /// ======================================================================
    /// Releasable
    /// ======================================================================
    @Override
    public void release() {
        histogram.release();
    }
}
