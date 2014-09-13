package src.ddpsc.phenocv.utility;

/**
 * @author cjmcentee
 */
public class MathX {

    public static int min(int ... values) {

        int min = values[0];
        for (int val : values)
            min = Math.min(val, min);

        return min;
    }

    public static int max(int ... values) {

        int max = values[0];
        for (int val : values)
            max = Math.max(val, max);

        return max;
    }

    public static int average(int val1, int val2) {
        return (val1 + val2) / 2;
    }

}
