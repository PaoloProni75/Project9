package it.unibz.andreypaolo.conf;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.andreypaolo.conf.datatypes.DataTypes;
import it.unibz.andreypaolo.conf.operators.IOperator;
import it.unibz.andreypaolo.conf.operators.OperatorFactory;

import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Filter {
    private String path;

    private DataTypes type;

    private String operator;

    private String fieldValue;

    private final Logger logger = Logger.getLogger(Filter.class.getName());
    private Level logLevel = Level.WARNING;

    public Filter() {
        type=DataTypes.STRING;
    }

    public Filter(String path, DataTypes type, String operatorStr, String fieldValue) {
        this.path = path;
        this.type = type;
        this.operator = operatorStr;
        this.fieldValue = fieldValue;
    }

    public boolean accept(JsonNode node) {
        return accept(node, null);
    }

    public boolean accept(JsonNode node, ParseFormats patterns) {
        JsonNode foundNode = node.at(path);
        if (foundNode.isMissingNode())
            return false;

        IOperator operatorObj = OperatorFactory.get(operator);
        try {
            return type.getComparator().compare(foundNode, fieldValue, patterns, operatorObj);
        }
        catch (ParseException | DateTimeParseException ex) {
            logger.log(logLevel, "A problem occurred parsing a node or the field value", ex);
            return false;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DataTypes getType() {
        return type;
    }

    public void setType(DataTypes type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public void hideLogs() {
        this.logLevel = Level.ALL;
    }
}