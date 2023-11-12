package it.unibz.andreypaolo.highlevel;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

public class ValueObject implements Comparable<ValueObject> {
    private final String orderByFieldName;
    private final JsonNode bodyNodes;
    private final DateFormat dateFormat;

    public ValueObject(String fieldName, JsonNode bodyNodes, DateFormat dateFormat) {
        this.orderByFieldName = fieldName;
        this.bodyNodes = bodyNodes;
        this.dateFormat = dateFormat;
    }

    public JsonNode getBodyNodes() {
        return bodyNodes;
    }

    public String obtainTextValue() {
        JsonNode actualValues = bodyNodes.findValue(orderByFieldName);
        boolean isNotNull  = actualValues!= null && !actualValues.isNull();
        return isNotNull ? actualValues.textValue() : "";
    }

    public Date obtainDateValue() {
        final String textValue = obtainTextValue();
        if (textValue == null)
            return null;
        else {
            try {
                return dateFormat.parse(textValue);
            } catch (ParseException ex) {
                return null;
            }
        }
    }


    @Override
    public int compareTo(ValueObject other) {
        if (other != null) { // null values at the end
            Date dv = obtainDateValue();
            if (dv != null) { // It is a Date
                Date otherDV = other.obtainDateValue();
                if (otherDV != null)
                    return dv.compareTo(otherDV);

            }
            else {
                String localText = obtainTextValue();
                if (localText != null) {
                    String otherText = other.obtainTextValue();
                    return localText.compareTo(otherText);
                }
            }
        }

        return 1;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ValueObject that = (ValueObject) other;
        return Objects.equals(obtainTextValue(), that.obtainTextValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(obtainTextValue());
    }
}