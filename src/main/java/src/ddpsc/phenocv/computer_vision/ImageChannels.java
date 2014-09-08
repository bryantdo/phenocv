package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cjmcentee
 */
public class ImageChannels {

    protected final List<Channel> channels;

    /// ======================================================================
    /// Constructors
    /// ======================================================================
    public ImageChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public ImageChannels(Channel channel) {
        channels = new ArrayList<Channel>();
        channels.add(channel);
    }

    public ImageChannels(Channel channel1, Channel channel2) {
        channels = new ArrayList<Channel>();
        channels.add(channel1);
        channels.add(channel2);
    }

    public ImageChannels(Channel channel1, Channel channel2, Channel channel3) {
        channels = new ArrayList<Channel>();
        channels.add(channel1);
        channels.add(channel2);
        channels.add(channel3);
    }

    /// ======================================================================
    /// Object Overrides
    /// ======================================================================
    @Override
    public String toString() {
        String str = "[";
        for (int i = 0; i < channels.size(); i++) {
            if (i != 0)
                str += ", ";
            str += channels.get(i).name();
        }
        str += "]";

        return str;
    }

    /// ======================================================================
    /// Properties
    /// ======================================================================
    List<Mat> convertedImage(ColorImage image) {
        // Note ColorImage#makeMatOf returns a single mat, and no matter the channels
        // this method returns a single mat in a list (because that's how openCV wants it)
        // The two lines merely determine whether the returned image has 1 or 2 channels
        List<Mat> convertedImage = this.channels.size() == 1
                ? Arrays.asList(image.makeMatOf(this.channels.get(0)))
                : Arrays.asList(image.makeMatOf(this.channels.get(0), this.channels.get(1)));

        return convertedImage;
    }

    MatOfInt channelIndices() {
        if (channels.size() == 1)
            return new MatOfInt(0);
        else
            return new MatOfInt(0, 1);
    }

    MatOfInt channelSizes() {
        if (channels.size() == 1)
            return new MatOfInt(256);
        else
            return new MatOfInt(256, 256);
    }

    MatOfFloat channelRanges() {
        if (channels.size() == 1)
            return new MatOfFloat(0, 255);
        else
            return new MatOfFloat(0, 255, 0, 255);
    }
}
