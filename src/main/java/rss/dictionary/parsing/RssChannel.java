package rss.dictionary.parsing;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the RSS channel element for unmarshalling
 *
 * @author Connor Toole
 * @version 1.0
 * @since 2019-07-09
 */
@XmlRootElement(name = "channel")
public class RssChannel {

    private String title;
    private String link;
    private String description;
    private String lastBuildDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    @Override
    public String toString() {
        return "RssChannel{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", lastBuildDate='" + lastBuildDate + '\'' +
                '}';
    }
}
