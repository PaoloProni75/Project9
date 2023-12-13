package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

public class ValueObject implements Comparable<ValueObject> {
    private final String orderByFieldName;
    private final JsonNode bodyNodes;
    private final DateFormat dateFormat;

    public ValueObject(String fieldName, JsonNode bodyNodes) {
        this(fieldName, bodyNodes, null);
    }

    public ValueObject(String fieldName, JsonNode bodyNodes, DateFormat dateFormat) {
        this.orderByFieldName = fieldName;
        this.bodyNodes = bodyNodes;
        this.dateFormat = dateFormat;
    }

    public JsonNode getBodyNodes() {
        return bodyNodes;
    }

    public String obtainOrderFieldAsTextValue() {
        JsonNode actualValues = bodyNodes.at(orderByFieldName);
        boolean isNotNull  = actualValues!= null && !actualValues.isNull();
        return isNotNull ? actualValues.textValue() : "";
    }

    public boolean isDate() {
        return dateFormat != null;
    }

    public Date obtainOrderFieldAsDateValue() {
        final String textValue = obtainOrderFieldAsTextValue();
        if (textValue != null && !textValue.isEmpty() && !textValue.isBlank()) {
            try {
                return dateFormat.parse(textValue);
            }
            catch (ParseException ex) {
                System.err.println(ex.getMessage());
                return null;
            }
        }
        else
            return null;
    }

    @Override
    public int compareTo(ValueObject other) {
        if (other != null) { // null values at the end
            if (isDate()) {
                Date dv = obtainOrderFieldAsDateValue();
                Date otherDV = other.obtainOrderFieldAsDateValue();
                if (otherDV != null)
                    return dv.compareTo(otherDV);
            }
            else {
                String localText = obtainOrderFieldAsTextValue();
                if (localText != null) {
                    String otherText = other.obtainOrderFieldAsTextValue();
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
        return Objects.equals(obtainOrderFieldAsTextValue(), that.obtainOrderFieldAsTextValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(obtainOrderFieldAsTextValue());
    }
}