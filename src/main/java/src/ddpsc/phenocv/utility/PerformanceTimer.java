package src.ddpsc.phenocv.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public class PerformanceTimer {

    private static int errors = 0;

    private static List<Long> intervals;

    private static long startTime;
    private static boolean started = false;

    private static long cumulativeTime;
    private static boolean paused = false;

    public static void reset() {
        intervals = new ArrayList<Long>();
        errors = 0;
        started = false;
        cumulativeTime = 0;
    }

    public static void start() {
        if (intervals == null)
            intervals = new ArrayList<Long>();

        if (started || paused)
            errors++;

        startTime = System.currentTimeMillis();
        cumulativeTime = 0;

        started = true;
        paused = false;
    }

    public static void end() {
        if (intervals == null)
            intervals = new ArrayList<Long>();

        if ( ! started)
            errors++;

        intervals.add(System.currentTimeMillis() - startTime);
        cumulativeTime = 0;

        started = false;
        paused = false;
    }

    public static void pause() {
        if (intervals == null)
            intervals = new ArrayList<Long>();

        if ( ! started)
            errors++;

        cumulativeTime += System.currentTimeMillis() - startTime;

        started = true;
        paused = true;
    }

    public static void resume() {
        if (intervals == null)
            intervals = new ArrayList<Long>();

        if ( ! started || ! paused)
            errors++;

        startTime = System.currentTimeMillis();

        started = true;
        paused = false;
    }

    public static String readout() {
        if (intervals == null)
            intervals = new ArrayList<Long>();

        String readout = "Timing Intervals"
                + "\nTotal Time: " + total()
                + "\nIntervals: " + intervals.size()
                + "\nAverage Interval: " + average()
                + "\nErrors: " + errors;
        return readout;
    }

    private static long average() {
        if (intervals == null || intervals.size() == 0)
            return 0;

        return total() / intervals.size();
    }

    private static long total() {
        if (intervals == null)
            return 0;

        long total = 0;
        for (Long interval : intervals)
            total += interval;

        return total;
    }
}
