package it.unibz.andreypaolo.highlevel;

import it.unibz.andreypaolo.DataProviderFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static it.unibz.andreypaolo.highlevel.ServiceApi.FROM;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class TourismTest {

    @Test
    void valueObjectComparison() throws IOException, InterruptedException {
        Tourism tourism = new Tourism(DataProviderFactory.createProvider());
        List<ValueObject> items = tourism.readDataFromService(
                "v1/Event?pagenumber=1&removenullvalues=false", FROM);
        assertFalse(items.isEmpty());

        ValueObject before = items.get(1);
        ValueObject after = items.get(0);

        assertTrue( before.compareTo(after) < 0);
        assertNotEquals(before.hashCode(), after.hashCode());
        assertNotEquals(before, after);
    }
}
