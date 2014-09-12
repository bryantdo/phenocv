package src.ddpsc.phenocv.utility;

import src.ddpsc.phenocv.computer_vision.Channel;
import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.GrayImage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class Lists {

    public static List<Tuple<ColorImage, GrayImage>> loadMaskedImagePairs(List<Tuple<String, String>> pairedFiles) {

        List<Tuple<ColorImage, GrayImage>> pairs = new ArrayList<Tuple<ColorImage, GrayImage>>();
        for(Tuple<String, String> filenames : pairedFiles) {
            String imageFilename = filenames.item1;
            String maskFilename = filenames.item2;

            ColorImage image = new ColorImage(imageFilename);
            GrayImage mask = new GrayImage(maskFilename);

            pairs.add(new Tuple<ColorImage, GrayImage>(image, mask));
        }

        return pairs;
    }

    public static boolean sameChannels(List<Channel> channels1, List<Channel> channels2) {

        if (channels1 == null || channels2 == null)
            return false;

        if (channels1.size() != channels2.size())
            return false;

        for (int i = 0; i < channels1.size(); i++)
            if (channels1.get(i) != channels2.get(i))
                return false;

        return true;
    }
}
