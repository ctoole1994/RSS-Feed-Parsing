import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import metabolon.RssChannel;
import metabolon.RssRoot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RssRootUnitTest {

    @Test
    public void testGetters() {

        PojoClass rssRoot = PojoClassFactory.getPojoClass(RssRoot.class);

        Validator validator = ValidatorBuilder
                .create()
                .with(new GetterMustExistRule())
                .with(new GetterTester())
                .build();

        validator.validate(rssRoot);
    }

    @Test
    public void testSetters() {

        PojoClass rssRoot = PojoClassFactory.getPojoClass(RssRoot.class);

        Validator validator = ValidatorBuilder
                .create()
                .with(new SetterMustExistRule())
                .with(new SetterTester())
                .build();

        validator.validate(rssRoot);
    }

    @Test
    public void testToString() {

        RssRoot rssRoot = new RssRoot();

        RssChannel rssChannel = new RssChannel();
        rssChannel.setLink("LINK");
        rssChannel.setTitle("TITLE");
        rssChannel.setDescription("DESCRIPTION");
        rssChannel.setLastBuildDate("DATE");

        rssRoot.setChannel(rssChannel);

        String testString = "RssRoot{" +
                "channel=" + rssChannel +
                '}';

        assertEquals(testString, rssRoot.toString());
    }
}
