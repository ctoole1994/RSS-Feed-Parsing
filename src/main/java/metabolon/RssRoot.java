package metabolon;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the rss element for unmarshalling
 *
 * @author Connor Toole
 * @version 1.0
 * @since 2019-07-09
 */
@XmlRootElement(name = "rss")
public class RssRoot {

    /**
     * The RssChannel object represents the channel element in the RSS spec. It is populated by unmarshalling
     * a XML resource.
     */
    private RssChannel channel;

    @XmlElement(name = "channel")
    public RssChannel getChannel() {
        return channel;
    }

    public void setChannel(RssChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "RssRoot{" +
                "channel=" + channel +
                '}';
    }
}
