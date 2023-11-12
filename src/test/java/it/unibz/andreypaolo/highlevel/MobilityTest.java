package it.unibz.andreypaolo.highlevel;

import it.unibz.andreypaolo.DataProviderFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static it.unibz.andreypaolo.highlevel.ServiceApi.EVENT_DATE;
import static org.junit.jupiter.api.Assertions.*;

public class MobilityTest {
    @Test
    void valueObjectComparison() throws IOException, InterruptedException {
        Mobility mobility = new Mobility(DataProviderFactory.createProvider());
        List<ValueObject> items = mobility.readDataFromService(
                "v2/flat%2Cevent/%2A?limit=200&offset=0&shownull=false&distinct=true",
                EVENT_DATE);
        assertFalse(items.isEmpty());
        ValueObject before = items.get(0);
        ValueObject after = items.get(4);
        assertTrue( before.compareTo(after) < 0);
        assertNotEquals(before.hashCode(), after.hashCode());
        assertNotEquals(before, after);
    }
}