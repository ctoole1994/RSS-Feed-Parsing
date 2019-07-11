package metabolon;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "rss")
public class RssFeed {


    private RssChannel channel;

    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @XmlElement(name = "channel")
    public RssChannel getChannel() {
        return channel;
    }

    public void setChannel(RssChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "RssFeed{" +
                "company='" + company + '\'' +
                ", channel=" + channel +
                '}';
    }
}
