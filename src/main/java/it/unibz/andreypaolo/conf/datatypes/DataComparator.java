package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.andreypaolo.ItemDTO;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.operators.IOperator;

import java.text.ParseException;

public interface DataComparator {
    /**
     * Compares two ItemDTO objects based on their queryFieldType.
     *
     * @param valueObject1 The first ItemDTO to compare.
     * @param valueObject2 The second ItemDTO to compare.
     * @return A negative integer if valueObject1 is less than valueObject2,
     *         zero if valueObject1 is equal to valueObject2,
     *         or a positive integer if valueObject1 is greater than valueObject2.
     */
    int compareItems(ItemDTO valueObject1, ItemDTO valueObject2);

    /**
     * Compare the value of a JSon node with a value, according to an operator that
     * defines the comparison rule.
     * The string pattern let convert the string value to a date, a number and so on.
     * For instance, if the operator is &gt; this method
     * return true if the node value is strictly greater than the fieldValue.
     * @param node Json node containing only the single value to be checked
     * @param fieldValue Value to be compared
     * @param patterns According to the data type, it is used to convert a String
     *                 into a specific Java type.
     * @param operator Relation operator (equals, greater, lesser etc...)
     * @return true when the comparison between the node value and the field value
     * respect the operator, for example if node is greater than fieldValue and the
     * operator checks for "greater", it will return true.
     * @throws ParseException This exception is risen when a problem occurred
     * parsing a value for the provided parsePattern.
     */
    boolean compare(JsonNode node, String fieldValue, ParseFormats patterns, IOperator operator) throws ParseException;
}
