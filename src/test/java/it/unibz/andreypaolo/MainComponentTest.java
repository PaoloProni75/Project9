package it.unibz.andreypaolo;

import org.junit.jupiter.api.Test;

import java.io.IOException;

// These constants could stay in the test part only
import static it.unibz.andreypaolo.highlevel.ServiceApi.EVENT_DATE;
import static it.unibz.andreypaolo.highlevel.ServiceApi.FROM;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainComponentTest {
    @Test
    void joinAndOrderTest() throws IOException, InterruptedException {

        MainComponent mainComponent = new MainComponent(DataProviderFactory.createProvider());
        String result = mainComponent.read("v2/flat%2Cevent/%2A?limit=200&offset=0&shownull=false&distinct=true",
                            EVENT_DATE,
                            "v1/Event?pagenumber=1&removenullvalues=false",
                            FROM);

        assertFalse(result.isEmpty());
        // TODO add way more test
        assertTrue(result.contains("2022-05-11 00:00:00.000+0000"));
        assertTrue(result.contains("2022-06-01T00:00:00"));
        // System.out.println(result);
    }
}
