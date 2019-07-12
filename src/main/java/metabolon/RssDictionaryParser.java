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

/**
 * The RssDictionaryParser program accepts a RSS dictionary keyed by company and valued by RSS URL, and the number of
 * days that the companies' RSS feeds do not have any activity. It then determines which companies have not had activity
 * for the given number of days.
 *
 * @author Connor Toole
 * @version 1.0
 * @since 2019-07-09
 */
public class RssDictionaryParser {

    private static Unmarshaller unmarshaller;

    public static void main(String[] args) {
        parseRssDictionaryForCompaniesWithoutActivity(getRssDictionary(), 3);
    }

    /**
     * This method returns a sample dictionary that would be coming from an external source
     *
     * @return The RSS dictionary for the function
     */
    public static Map<String, List<String>> getRssDictionary() {
        Map<String, List<String>> rssDictionary = new HashMap<>();
        List<String> testURLS = new ArrayList<>();
        testURLS.add("https://dianerehm.org/rss/npr/dr_podcast.xml");
        rssDictionary.put("NPR", testURLS);
        return rssDictionary;
    }

    /**
     * This method parses and prints the companies that meet the inactivity criteria
     *
     * @param rssDictionary The RSS Dictionary keyed by company and valued by RSS URL list in string form
     * @param days          The number of days that the companies' RSS feeds do not have any activity
     */
    public static void parseRssDictionaryForCompaniesWithoutActivity(Map<String, List<String>> rssDictionary, int days) {

        unmarshaller = createUnmarshaller();

        if (unmarshaller != null) {

            Map<String, List<RssRoot>> companyRssFeedMap = new HashMap<>();

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
    }

    /**
     * This method compares the dateLastUpdated in the RssChannel object to the specified number of days
     * without activity for a company. If all RSS feeds for a company do not have activity, the company does not
     * have any activity.
     *
     * @param companyRssFeedMap RSS dictionary keyed by company and valued by RssRoot object
     * @param days              The number of days that the companies' RSS feeds do not have any activity
     */
    private static void checkRssLastBuildDates(Map<String, List<RssRoot>> companyRssFeedMap, int days) {

        LocalDateTime withoutActivityDate = LocalDateTime.now(ZoneOffset.UTC).minusDays(days);

        List<String> companiesWithoutActivity = new ArrayList<>();

        companyRssFeedMap.forEach((company, rssFeedList) -> {

            int inactiveRssCounter = 0;

            for (RssRoot rssRoot : rssFeedList) {
                if (rssRoot.getChannel().getLastBuildDate() != null) {

                    LocalDateTime dateLastUpdated = LocalDateTime.parse(rssRoot.getChannel().getLastBuildDate(),
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

    /**
     * This method prints the list of companies that do not have any activity
     *
     * @param companies List of companies to print
     * @param days      The number of days that the companies' RSS feeds do not have any activity
     */
    private static void printCompanies(List<String> companies, int days) {
        System.out.println("The following companies have at least one RSS feed without activity for " + days + " days:");
        companies.forEach(System.out::println);
    }

    /**
     * This method creates RssRoot objects by unmarshalling the XML URLs
     *
     * @param company Name of company
     * @param urlList RSS feeds as URL objects
     * @return List of RssRoot objects which have been unmarshalled from the XML URLs and contain RSS tag values
     */
    private static List<RssRoot> createRssFeeds(String company, List<URL> urlList) {

        List<RssRoot> rssRootList = new ArrayList<>();

        urlList.forEach(url -> {
            try {
                RssRoot rssRoot = (RssRoot) unmarshaller.unmarshal(url);
                rssRootList.add(rssRoot);
            } catch (JAXBException e) {
                System.out.println("Error unmarshalling XML to RssRoot.class.");
                e.printStackTrace();
            }
        });

        return rssRootList;
    }

    /**
     * This method creates an Unmarshaller object used to parse the RSS XML
     *
     * @return Unmarshaller object created from RssRoot class
     */
    private static Unmarshaller createUnmarshaller() {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RssRoot.class);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            System.out.println("Error creating Unmarshaller for RssRoot.class.");
            e.printStackTrace();
        }

        return null;
    }
}
