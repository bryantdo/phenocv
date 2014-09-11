package src.ddpsc.phenocv.utility;

/**
 * @author cjmcentee
 */
public class Conversion {


    /**
     * Converts Java's formatted two's complement bytes into the expected
     * integers in the range [0, 256)
     *
     * http://en.wikipedia.org/wiki/Two%27s_complement
     *
     * @param bytes     byte array to convert
     * @return          input byte array as ints
     */
    public static int[] toInt(byte bytes[]) {
        int ints[] = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            ints[i] = bytes[i] & 0xff;
        return ints;
    }

}
