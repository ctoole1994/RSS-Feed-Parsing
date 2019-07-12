import metabolon.RssDictionaryParser;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RssDictionaryParserUnitTest {

    @Test (expected = IllegalArgumentException.class)
    public void testParseWithNegativeDays() {
        Map<String, List<String>> rssDictionary = new HashMap<>();
        List<String> testURLS = new ArrayList<>();
        testURLS.add("URL");
        rssDictionary.put("NPR", testURLS);
        RssDictionaryParser.parseRssDictionaryForCompaniesWithoutActivity(rssDictionary,-5);
    }

    @Test
    public void testParseWithoutActivity() throws MalformedURLException {

        Map<String, List<String>> rssDictionary = new HashMap<>();
        List<String> testURLS = new ArrayList<>();
        testURLS.add(new File("./src/test/resources/wsjRssTech3Jul19.xml").toURI().toURL().toString());
        rssDictionary.put("WSJ", testURLS);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));



        RssDictionaryParser.parseRssDictionaryForCompaniesWithoutActivity(rssDictionary,3);
        String testString = "The following companies' RSS feeds have not had activity in 3 days:"
                + System.lineSeparator() + "WSJ" + System.lineSeparator();
        assertEquals(testString, outContent.toString());
    }

    @Test
    public void testParseWithSomeActivity() throws MalformedURLException {

        Map<String, List<String>> rssDictionary = new HashMap<>();
        List<String> testURLS = new ArrayList<>();
        testURLS.add(new File("./src/test/resources/wsjRssTech3Jul19.xml").toURI().toURL().toString());
        testURLS.add(new File("./src/test/resources/wsjRssTech12Jul19.xml").toURI().toURL().toString());
        rssDictionary.put("WSJ", testURLS);

        List<String> urls = new ArrayList<>();
        urls.add(new File("./src/test/resources/nyTimesTechnologyRss8Jul2019.xml").toURI().toURL().toString());
        rssDictionary.put("NYT", urls);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        RssDictionaryParser.parseRssDictionaryForCompaniesWithoutActivity(rssDictionary,3);

        String testString = "The following companies' RSS feeds have not had activity in 3 days:"
                + System.lineSeparator() + "NYT" + System.lineSeparator();
        assertEquals(testString, outContent.toString());
    }

    @Test
    public void testParseWithActivity() throws MalformedURLException {

        Map<String, List<String>> rssDictionary = new HashMap<>();
        List<String> testURLS = new ArrayList<>();
        testURLS.add(new File("./src/test/resources/wsjRssTech3Jul19.xml").toURI().toURL().toString());
        testURLS.add(new File("./src/test/resources/wsjRssTech12Jul19.xml").toURI().toURL().toString());
        rssDictionary.put("WSJ", testURLS);
        rssDictionary.put("NYT", Collections.singletonList(new File("./src/test/resources/nyTimesTechnologyRss12Jul2019.xml").toURI().toURL().toString()));
        rssDictionary.put("Joe Rogan's Podcast", Collections.singletonList(new File("./src/test/resources/joeRoganRss12Jul2019.xml").toURI().toURL().toString()));


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        RssDictionaryParser.parseRssDictionaryForCompaniesWithoutActivity(rssDictionary,2);

        String testString = "The following companies' RSS feeds have not had activity in 2 days:"
                + System.lineSeparator();
        assertEquals(testString, outContent.toString());
    }

    @Test
    public void testParseWithMalformedURLs() {
        Map<String, List<String>> rssDictionary = new HashMap<>();
        rssDictionary.put("NYT", Collections.singletonList("THIS ISN'T A VALID URL"));

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        RssDictionaryParser.parseRssDictionaryForCompaniesWithoutActivity(rssDictionary,3);

        String testString = "The following companies' RSS feeds have not had activity in 3 days:"
                + System.lineSeparator();
        assertEquals(testString, outContent.toString());
        assertTrue(errContent.toString().contains("java.net.MalformedURLException: no protocol: THIS ISN'T A VALID URL"));
    }

    @Test
    public void testParseWithoutLastBuildDate() throws MalformedURLException{

        Map<String, List<String>> rssDictionary = new HashMap<>();
        rssDictionary.put("Planet Money", Collections.singletonList(new File("./src/test/resources/planetMoneyRssWithoutLastBuildDate.xml").toURI().toURL().toString()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        RssDictionaryParser.parseRssDictionaryForCompaniesWithoutActivity(rssDictionary,3);
        String testString = "The following companies' RSS feeds have not had activity in 3 days:"
                + System.lineSeparator();
        assertEquals(testString, outContent.toString());

    }

}
