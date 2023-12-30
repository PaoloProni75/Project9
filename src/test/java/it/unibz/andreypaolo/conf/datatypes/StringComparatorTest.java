package it.unibz.andreypaolo.conf.datatypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringComparatorTest {
    StringComparator comparator = null;

    final String QUERY_FIELD_PATH = "/key";

    @BeforeEach
    void init() {
        comparator = new StringComparator();
    }

    @Test
    void aBeforeB() {
        int result = comparator.compare("a", "b");
        assertTrue(result < 0);
    }

    @Test
    void bAfterA() {
        int result = comparator.compare("b", "a");
        assertTrue(result > 0);
    }

    @Test
    void sameStringAreEquals() {
        final String first = "A";
        final String second = "A";
        int result = comparator.compare(first, second);
        assertEquals(0, result);
    }

    @Test
    void nullAtTheEnd() {
        assertTrue(comparator.compare(null, "a") > 0);
        assertTrue(comparator.compare("a", null) < 0);
        final String a = null;
        final String b = null;
        assertEquals(0, comparator.compare(a, b));
    }


}
