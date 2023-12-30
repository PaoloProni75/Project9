package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.andreypaolo.ItemDTO;
import it.unibz.andreypaolo.caches.TimeFormatCache;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.operators.IOperator;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeComparator extends AbstractObjectComparator<LocalTime> {

    @Override
    public int compareItems(ItemDTO item1, ItemDTO item2) {
        try {
            return compare(parseTime(item1), parseTime(item2));
        } catch (ParseException ex) {
            logger.log(logLevel, ex.getLocalizedMessage());
            return 0;
        }
    }

    @Override
    public boolean compare(JsonNode node, String fieldValue, ParseFormats patterns, IOperator operator) throws ParseException {
        DateTimeFormatter timeFormatter = TimeFormatCache.getInstance()
                                                         .getTimeFormat(patterns.getTime().orElse("HH:mm:ss"));

        LocalTime firstTime = LocalTime.parse(node.textValue(), timeFormatter);
        LocalTime secondTime = LocalTime.parse(fieldValue, timeFormatter);

        int compareResult = compare(firstTime, secondTime);
        return operator.check(compareResult);
    }

    private LocalTime parseTime(ItemDTO item) throws ParseException {
        String timeText = item.getOrderFieldText();
        String timeFormat = item.getTimeFormat();
        DateTimeFormatter timeFormatter = TimeFormatCache.getInstance().getTimeFormat(timeFormat);
        return LocalTime.parse(timeText, timeFormatter);
    }
}