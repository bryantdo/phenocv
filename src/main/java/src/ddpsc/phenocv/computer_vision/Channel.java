package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;

/**
 * @author cjmcentee
 */
public class Channel {

    private static final int DEFAULT_MAGNITUDE = 255;

    public final ColorSpace colorSpace;
    protected final int magnitude;

    public Channel(ColorSpace space) {
        this(space, DEFAULT_MAGNITUDE);
    }

    public Channel(ColorSpace space, int magnitude) {
        this.colorSpace = space;
        this.magnitude = magnitude;
    }

    @Override
    public String toString() {
        return colorSpace.name();
    }

    public MatOfInt sizes() {
        return new MatOfInt(magnitude, magnitude, magnitude);
    }

    public MatOfFloat ranges() {
        return new MatOfFloat(0, 255, 0, 255, 0, 255);
    }

    public MatOfInt indices() {
        return new MatOfInt(0, 1, 2);
    }

    public static Channel[] values(int magnitude) {
        ColorSpace spaces[] = ColorSpace.values();
        Channel channels[] = new Channel[spaces.length];

        for (int i = 0; i < spaces.length; i++)
            channels[i] = new Channel(spaces[i], magnitude);

        return channels;
    }

    public static Channel[] values() {
        return values(DEFAULT_MAGNITUDE);
    }
}
