package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibz.andreypaolo.ItemDTO;
import it.unibz.andreypaolo.MapperSingleton;
import it.unibz.andreypaolo.conf.ParseFormats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateComparatorTest {
    DateComparator comparator = null;

    final String DATE_FORMAT = "yyyy-MM-dd";
    final String DATE_FIELD = "date";
    final String QUERY_FIELD_PATH = "/date";
    @BeforeEach
    void init() {
        comparator = new DateComparator();
    }

    @Test
    void yesterdayBeforeToday() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        int result = comparator.compare(yesterday, today);
        assertTrue(result < 0);
    }

    @Test
    void tomorrowAfterToday() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();
        int result = comparator.compare(tomorrow, today);
        assertTrue(result > 0);
    }

    @Test
    void sameDatesAreEquals() {
        Date today1 = new Date();
        Date today2 = new Date();
        int result = comparator.compare(today1, today2);
        assertEquals(result, 0);
    }

    @Test
    void nullAtTheEnd() {
        Date today = new Date();
        assertTrue(comparator.compare(null, today) > 0);
        assertTrue(comparator.compare(today, null) < 0);
    }

    // A test to check the compareItems method when both dates are valid
    @Test
    void testCompareItemsWithValidDates() {
        ObjectMapper mapper = MapperSingleton.getInstance().getMapper();
        ParseFormats pf = new ParseFormats();
        pf.setDate(DATE_FORMAT);
        ItemDTO item2023 = new ItemDTO.Builder()
                .setQueryFieldType(DataTypes.DATE)
                .setOriginalItem(mapper.createObjectNode().put(DATE_FIELD, "2023-03-01"))
                .setQueryFieldPath(QUERY_FIELD_PATH)
                .setParseFormats(pf)
                .build();

        ItemDTO item2022 = new ItemDTO.Builder()
                .setQueryFieldType(DataTypes.DATE)
                .setOriginalItem(mapper.createObjectNode().put(DATE_FIELD, "2022-03-01"))
                .setQueryFieldPath(QUERY_FIELD_PATH)
                .setParseFormats(pf)
                .build();

        // 1 is expected because the date in item1 (2023-03-01) is after the date in item2 (2022-03-01)
        assertEquals(1, comparator.compareItems(item2023, item2022));
    }

    @Test
    void testWrongDateFormat() {
        ObjectMapper mapper = MapperSingleton.getInstance().getMapper();
        ParseFormats pf = new ParseFormats();
        pf.setDate(DATE_FORMAT);
        ItemDTO item2022 = new ItemDTO.Builder()
                .setQueryFieldType(DataTypes.DATE)
                .setOriginalItem(mapper.createObjectNode().put(DATE_FIELD, "2022-03-01"))
                .setQueryFieldPath(QUERY_FIELD_PATH)
                .setParseFormats(pf)
                .build();

        ItemDTO itemInvalidDate = new ItemDTO.Builder()
                .setQueryFieldType(DataTypes.DATE)
                .setOriginalItem(mapper.createObjectNode().put(DATE_FIELD, "2022/33/01"))
                .setQueryFieldPath(QUERY_FIELD_PATH)
                .setParseFormats(pf)
                .build();

        comparator.hideLogs();
        assertEquals(0, comparator.compareItems(itemInvalidDate, item2022));
    }
}