package src.ddpsc.phenocv.utility;

import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.Mask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class Lists {

    public static List<Tuple<ColorImage, Mask>> loadMaskedImagePairs(List<Tuple<String, String>> pairedFiles) {

        List<Tuple<ColorImage, Mask>> pairs = new ArrayList<Tuple<ColorImage, Mask>>();
        for(Tuple<String, String> filenames : pairedFiles) {
            String imageFilename = filenames.item1;
            String maskFilename = filenames.item2;

            ColorImage image = new ColorImage(imageFilename);
            Mask mask = new Mask(maskFilename);

            pairs.add(new Tuple<ColorImage, Mask>(image, mask));
        }

        return pairs;
    }

}
