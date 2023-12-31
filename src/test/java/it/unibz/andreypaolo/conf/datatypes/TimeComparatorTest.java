package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.andreypaolo.ItemDTO;
import it.unibz.andreypaolo.conf.ParseFormats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class TimeComparatorTest {
    TimeComparator comparator = null;

    private final static LocalTime one = LocalTime.of(1, 0);
    private final static LocalTime two = LocalTime.of(2, 0);

    @BeforeEach
    void init() {
        comparator = new TimeComparator();
    }

    @Test
    void oneBeforeTwo() {
        int result = comparator.compare(one, two);
        assertTrue(result < 0);
    }

    @Test
    void twoAfterOne() {
        int result = comparator.compare(two, one);
        assertTrue(result > 0);
    }

    @Test
    void sameTimesAreEquals() {
        final LocalTime firstTime = LocalTime.of(3, 0);
        final LocalTime secondTime = LocalTime.of(3, 0);
        int result = comparator.compare(firstTime, secondTime);
        assertEquals(0, result);
    }

    @Test
    void nullAtTheEnd() {
        assertTrue(comparator.compare(null, one) > 0);
        assertTrue(comparator.compare(one, null) < 0);
        assertEquals(0, comparator.compare(null, null));
    }

    @Test
    void compareItems() {
        final String TIME_FORMAT = "HH:mm:ss";
        final String KEY = "path";
        final String PATH = "/path";

        final ParseFormats pf = new ParseFormats();
        pf.setTime(TIME_FORMAT);
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode firstNode = mapper.createObjectNode();
        final ObjectNode secondNode = mapper.createObjectNode();

        firstNode.put(KEY, "10:00:00");
        secondNode.put(KEY, "11:00:00");

        ItemDTO item1 = new ItemDTO.Builder().setParseFormats(pf).setQueryFieldPath(PATH).setQueryFieldType(DataTypes.TIME).setOriginalItem(firstNode).build();
        ItemDTO item2 = new ItemDTO.Builder().setParseFormats(pf).setQueryFieldPath(PATH).setQueryFieldType(DataTypes.TIME).setOriginalItem(secondNode).build();

        int result = comparator.compareItems(item1, item2);
        assertTrue(result < 0);

        firstNode.put(KEY, "a:b:c");
        comparator.hideLogs();
        assertEquals(0,  comparator.compareItems(item1, item2));

        pf.setTime(null);
        firstNode.put(KEY, "10:00:00");
        assertTrue(comparator.compareItems(item1, item2) < 0);
    }
}