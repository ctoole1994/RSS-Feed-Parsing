package metabolon;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RssDictionaryParser {

    private static Unmarshaller unmarshaller;

    public static void main(String[] args) {

        unmarshaller = createUnmarshaller();

        if (unmarshaller != null) {
            parseRssDictionaryForCompaniesWithoutActivity(getRssDictionary(), 3);
        }
    }


    public static Map<String, List<String>> getRssDictionary() {
        Map<String, List<String>> rssDictionary = new HashMap<>();
        List<String> testURLS = new ArrayList<>();
        testURLS.add("https://dianerehm.org/rss/npr/dr_podcast.xml");
        rssDictionary.put("NPR", testURLS);
        return rssDictionary;
    }

    public static void parseRssDictionaryForCompaniesWithoutActivity(Map<String, List<String>> rssDictionary, int days) {

        Map<String, List<RssFeed>> companyRssFeedMap = new HashMap<>();

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

                companyRssFeedMap.put(company, createRssFeeds(company, urlList));
            });
        });

        checkRssLastBuildDates(companyRssFeedMap, days);
    }

    private static void checkRssLastBuildDates(Map<String, List<RssFeed>> companyRssFeedMap, int days) {

        LocalDateTime withoutActivityDate = LocalDateTime.now(ZoneOffset.UTC).minusDays(days);

        List<String> companiesWithoutActivity = new ArrayList<>();

        companyRssFeedMap.forEach((company, rssFeedList) -> {

            int inactiveRssCounter = 0;

            for (RssFeed rssFeed : rssFeedList) {
                if (rssFeed.getChannel().getLastBuildDate() != null) {

                    LocalDateTime dateLastUpdated = LocalDateTime.parse(rssFeed.getChannel().getLastBuildDate(),
                            DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC));

                    if (dateLastUpdated.isBefore(withoutActivityDate)) {
                        inactiveRssCounter++;
                    }
                }
            }

            if (rssFeedList.size() == inactiveRssCounter) {
                companiesWithoutActivity.add(company);
            }
        });

        printCompanies(companiesWithoutActivity, days);
    }

    private static void printCompanies(List<String> companies, int days) {
        System.out.println("The following companies have at least one RSS feed without activity for " + days + " days:");
        companies.forEach(System.out::println);
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
