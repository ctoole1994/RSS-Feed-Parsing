package metabolon;

import javax.xml.bind.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RssDictionaryParser {

    private static Unmarshaller unmarshaller;

    public static void main(String[] args) {

        unmarshaller = createUnmarshaller();

        if (unmarshaller != null) {
            parseRssDictionaryForDays(getRssDictionary(), 3);
        }
    }


    public static Map<String, List<String>> getRssDictionary() {
        Map<String, List<String>> rssDictionary = new HashMap<>();
        rssDictionary.put("NPR", Collections.singletonList("https://dianerehm.org/rss/npr/dr_podcast.xml"));
        return rssDictionary;
    }

    public static void parseRssDictionaryForDays(Map<String, List<String>> rssDictionary, int days) {


        List<RssFeed> rssFeedList = new ArrayList<>();


        rssDictionary.forEach((company, rssUrlList) -> {

            List<URL> urlList = new ArrayList<>();

            rssUrlList.forEach(rssUrl -> {

                try {
                    URL url = new URL(rssUrl);
                    urlList.add(url);
                } catch (MalformedURLException e) {
                    System.out.println("URL: " + rssUrl + " is malformed.");
                    e.printStackTrace();
                }

                rssFeedList.addAll(createRssFeeds(company, urlList));
            });
        });


//        rssFeedList.forEach(rssFeed -> {
//            System.out.println("RSS FEED: " + rssFeed.toString());
//
//            try {
//                JAXBContext jc = JAXBContext.newInstance(RssFeed.class);
//
//                Marshaller marshaller = jc.createMarshaller();
//                try {
//                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//                    marshaller.marshal(rssFeed, System.out);
//                } catch (PropertyException e) {
//                    e.printStackTrace();
//                }
//
//            } catch (JAXBException e) {
//                e.printStackTrace();
//            }
//        });


        LocalDateTime updatedBeforeDate = LocalDateTime.now(ZoneOffset.UTC).minusDays(days);

        System.out.println("updatedBeforeDate is : " + updatedBeforeDate);

        List<RssFeed> validFeeds = new ArrayList<>();

        rssFeedList.forEach(rssFeed -> {

            if (rssFeed.getChannel().getLastBuildDate() != null) {

                LocalDateTime dateLastUpdated = LocalDateTime.parse(rssFeed.getChannel().getLastBuildDate(), DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC));

                System.out.println("dateLastUpdated is : " + dateLastUpdated);

                if (dateLastUpdated.isBefore(updatedBeforeDate)) {
                    validFeeds.add(rssFeed);
                }
            }
        });

        validFeeds.forEach(rssFeed -> System.out.println(rssFeed.toString()));
    }

    private static List<RssFeed> createRssFeeds(String company, List<URL> urlList) {

        List<RssFeed> rssFeedList = new ArrayList<>();

        urlList.forEach(url -> {
            try {
                RssFeed rssFeed = (RssFeed) unmarshaller.unmarshal(url);
                rssFeed.setCompany(company);
                rssFeedList.add(rssFeed);
            } catch (JAXBException e) {
                System.out.println("Error unmarshalling XML to RssFeed.class.");
                e.printStackTrace();
            }
        });

        return rssFeedList;
    }

    private static Unmarshaller createUnmarshaller() {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RssFeed.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            System.out.println("Error creating Unmarshaller for RssFeed.class.");
            e.printStackTrace();
        }

        return null;
    }
}
