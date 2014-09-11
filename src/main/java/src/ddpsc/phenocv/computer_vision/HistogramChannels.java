package src.ddpsc.phenocv.computer_vision;

import src.ddpsc.phenocv.utility.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
public final class HistogramChannels extends ImageChannels {


    /// ======================================================================
    /// Constructors
    /// ======================================================================
    public HistogramChannels(HistogramChannels channels) {
        super(channels.channels);
    }

    public HistogramChannels(Channel channel) {
        super(channel);
    }

    public HistogramChannels(Channel channel1, Channel channel2) {
        super(channel1, channel2);
    }

    public static List<HistogramChannels> allCombinations() {

        List<HistogramChannels> all = new ArrayList<HistogramChannels>();

        Channel allChannels[] = Channel.values();

        // Single channels
        for (Channel channel : allChannels)
            all.add(new HistogramChannels(channel));

        // Double channels
        for (int i = 0; i < allChannels.length; i++) {
            Channel channel1 = allChannels[i];
            for (int j = i + 1; j < allChannels.length; j++) {
                Channel channel2 = allChannels[j];
                all.add(new HistogramChannels(channel1, channel2));
            }
        }

        return all;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || ! (obj instanceof HistogramChannels))
            return false;
        else
            return equals((HistogramChannels)obj);
    }

    public boolean equals(HistogramChannels channels) {
        return Lists.sameChannels(this.channels, channels.channels);
    }

    @Override
    public int hashCode() {
        return channels.hashCode();
    }
}
