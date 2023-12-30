package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.andreypaolo.ItemDTO;
import it.unibz.andreypaolo.caches.NumberFormatCache;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.operators.IOperator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class DecimalComparator extends AbstractObjectComparator<BigDecimal> {

    @Override
    public int compareItems(ItemDTO item1, ItemDTO item2) {
        try {
            return compare(parseNumber(item1), parseNumber(item2));
        } catch (ParseException ex) {
            logger.log(logLevel, ex.getLocalizedMessage());
            return 0;
        }
    }

    @Override
    public boolean compare(JsonNode node, String fieldValue, ParseFormats patterns, IOperator operator) throws ParseException {
        final String decimalFormat = patterns.getTime().orElse("#.#");
        DecimalFormat decimalFormatter = NumberFormatCache.getInstance().getNumberFormat(decimalFormat);

        BigDecimal firstBigDecimal = new BigDecimal(decimalFormatter.parse(node.textValue()).toString());
        BigDecimal secondBigDecimal = new BigDecimal(decimalFormatter.parse(fieldValue).toString());

        int compareResult = compare(firstBigDecimal, secondBigDecimal);
        return operator.check(compareResult);
    }

    private BigDecimal parseNumber(ItemDTO item) throws ParseException {
        String decimalText = item.getOrderFieldText();
        String decimalFormat = item.getDecimalFormat();
        DecimalFormat decimalFormatter = NumberFormatCache.getInstance().getNumberFormat(decimalFormat);
        Number number = decimalFormatter.parse(item.getOrderFieldText());
        return new BigDecimal(number.toString());
    }
}
