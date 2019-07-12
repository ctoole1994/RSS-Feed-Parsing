import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import rss.dictionary.parsing.RssChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RssChannelUnitTest {

    @Test
    public void testGetters() {

        PojoClass rssChannel = PojoClassFactory.getPojoClass(RssChannel.class);

        Validator validator = ValidatorBuilder
                .create()
                .with(new GetterMustExistRule())
                .with(new GetterTester())
                .build();

        validator.validate(rssChannel);
    }

    @Test
    public void testSetters() {

        PojoClass rssChannel = PojoClassFactory.getPojoClass(RssChannel.class);

        Validator validator = ValidatorBuilder
                .create()
                .with(new SetterMustExistRule())
                .with(new SetterTester())
                .build();

        validator.validate(rssChannel);
    }

    @Test
    public void testToString() {

        RssChannel rssChannel = new RssChannel();
        rssChannel.setLink("LINK");
        rssChannel.setTitle("TITLE");
        rssChannel.setDescription("DESCRIPTION");
        rssChannel.setLastBuildDate("DATE");

        String testString = "RssChannel{" +
                "title='" + "TITLE" + '\'' +
                ", link='" + "LINK" + '\'' +
                ", description='" + "DESCRIPTION" + '\'' +
                ", lastBuildDate='" + "DATE" + '\'' +
                '}';

        assertEquals(testString, rssChannel.toString());
    }
}
