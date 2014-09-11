package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import src.ddpsc.phenocv.utility.Tuple;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ReleaseContainer {

    public static void releaseAll(Collection<? extends Releasable> releaseAll) {
        for (Releasable releaseThis : releaseAll)
            releaseThis.release();
    }

    public static void releaseMatrices(Collection<? extends Mat> releaseAll) {
        Iterator<? extends Mat> releaseIter = releaseAll.iterator();
        while (releaseIter.hasNext())
            releaseIter.next().release();
    }

    public static void releaseBoth(Tuple<? extends Releasable, ? extends Releasable> releaseBoth) {
        releaseBoth.item1.release();
        releaseBoth.item2.release();
    }

    public static void release2(Tuple<?, ? extends Releasable> releaseSecond) {
        releaseSecond.item2.release();
    }

    public static void release1(Tuple<? extends Releasable, ?> releaseFirst) {
        releaseFirst.item1.release();
    }
}
