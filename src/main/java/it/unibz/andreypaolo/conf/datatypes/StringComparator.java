package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.andreypaolo.ItemDTO;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.operators.IOperator;

import java.text.ParseException;

public class StringComparator extends AbstractObjectComparator<String> {
    @Override
    public int compareItems(ItemDTO item1, ItemDTO item2) {
        String text1 = item1.getOrderFieldText();
        String text2 = item2.getOrderFieldText();
        return compare(text1, text2);
    }

    @Override
    public boolean compare(JsonNode node, String fieldValue, ParseFormats patterns,
                           IOperator operator) throws ParseException {

        String nodeTextValue = node.textValue();
        int compareResult = compare(nodeTextValue, fieldValue);
        return operator.check(compareResult);
    }
}