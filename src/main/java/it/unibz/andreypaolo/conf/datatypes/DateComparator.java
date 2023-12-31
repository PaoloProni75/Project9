package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.andreypaolo.ItemDTO;
import it.unibz.andreypaolo.caches.SimpleDateFormatCache;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.operators.IOperator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateComparator extends AbstractObjectComparator<Date> {
    @Override
    public int compareItems(ItemDTO item1, ItemDTO item2) {
        try {
            return compare(parseDate(item1), parseDate(item2));
        } catch (ParseException ex) {
            logger.log(logLevel, ex.getMessage());
            return 0;
        }
    }

    @Override
    public boolean compare(JsonNode node, String fieldValue, ParseFormats patterns, IOperator operator) throws ParseException {
        final String datePattern = patterns.getDate().orElseThrow(() -> new IllegalArgumentException("Date parse format not specified"));
        final SimpleDateFormat sdf = SimpleDateFormatCache.getInstance().getDateFormat(datePattern);

        Date firstDate = sdf.parse(node.textValue());
        Date secondDate = sdf.parse(fieldValue);

        int compareResult = compare(firstDate, secondDate);
        return operator.check(compareResult);
    }

    private Date parseDate(ItemDTO item) throws ParseException {
        String dateText = item.getOrderFieldText();
        String dateFormat = item.getDateFormat();
        return SimpleDateFormatCache.getInstance()
                .getDateFormat(dateFormat)
                .parse(dateText);
    }
}
