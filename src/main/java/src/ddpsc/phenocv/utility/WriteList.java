package src.ddpsc.phenocv.utility;

import src.ddpsc.phenocv.computer_vision.Writable;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.List;

/**
 * @author cjmcentee
 */
public class WriteList {

    public static void writeTo(List<Tuple<? extends Writable, String>> writeObjects) {
        for (Tuple<? extends Writable, String> imageWrite : writeObjects) {
            String filename = imageWrite.item2;

            imageWrite.item1.writeTo(filename);
        }
    }

}
