package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.andreypaolo.ItemDTO;
import it.unibz.andreypaolo.conf.ParseFormats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecimalComparatorTest {
    DecimalComparator comparator = null;

    @BeforeEach
    void init() {
        comparator = new DecimalComparator();
    }

    @Test
    void zeroBeforeOne() {
        int result = comparator.compare(BigDecimal.ZERO, BigDecimal.ONE);
        assertTrue(result < 0);
    }

    @Test
    void oneAfterZero() {
        int result = comparator.compare(BigDecimal.ONE, BigDecimal.ZERO);
        assertTrue(result > 0);
    }

    @Test
    void sameDecimalAreEquals() {
        int result = comparator.compare(BigDecimal.valueOf(1.234), BigDecimal.valueOf(1.234));
        assertEquals(0, result);
    }

    @Test
    void nullAtTheEnd() {
        assertTrue(comparator.compare(null, BigDecimal.ONE) > 0);
        assertTrue(comparator.compare(BigDecimal.ONE, null) < 0);
        assertEquals(0, comparator.compare(null, null));
    }

    @Test
    void compareItems() {
        final String DECIMAL_FORMAT = "#.#";
        final String KEY = "path";
        final String PATH = "/path";

        final ParseFormats pf = new ParseFormats();
        pf.setDecimal(DECIMAL_FORMAT);
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode firstNode = mapper.createObjectNode();
        final ObjectNode secondNode = mapper.createObjectNode();

        firstNode.put(KEY, "1.1");
        secondNode.put(KEY, "1.2");

        ItemDTO item1 = new ItemDTO.Builder().setParseFormats(pf).setQueryFieldPath(PATH).setQueryFieldType(DataTypes.DECIMAL).setOriginalItem(firstNode).build();
        ItemDTO item2 = new ItemDTO.Builder().setParseFormats(pf).setQueryFieldPath(PATH).setQueryFieldType(DataTypes.DECIMAL).setOriginalItem(secondNode).build();

        int result = comparator.compareItems(item1, item2);
        assertTrue(result < 0);

        firstNode.put(KEY, "a.b");
        comparator.hideLogs();
        assertEquals(0,  comparator.compareItems(item1, item2));

        pf.setDecimal(null);
        firstNode.put(KEY, "1.1");
        assertTrue(comparator.compareItems(item1, item2) < 0);
    }
}