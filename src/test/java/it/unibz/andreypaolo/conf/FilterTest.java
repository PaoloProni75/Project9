package it.unibz.andreypaolo.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.andreypaolo.conf.datatypes.DataTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FilterTest {
    @Test
    void filterStrings() {
        final String keyPath = "/A";
        final String aValue = "A value";
        final String bValue = "B value";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("A",aValue);

        Filter filterEquals = new Filter(keyPath, DataTypes.STRING, "=", aValue);
        assertTrue(filterEquals.accept(node));
        Filter filterNotEquals = new Filter(keyPath, DataTypes.STRING, "=", bValue);
        assertFalse(filterNotEquals.accept(node));

        Filter filterDifferent = new Filter(keyPath, DataTypes.STRING, "!=", bValue);
        assertTrue(filterDifferent.accept(node));
        Filter filterNotDifferent = new Filter(keyPath, DataTypes.STRING, "!=", aValue);
        assertFalse(filterNotDifferent.accept(node));

        Filter filterGreater = new Filter(keyPath, DataTypes.STRING, ">", bValue);
        assertFalse(filterGreater.accept(node));
        Filter filterNotGreater = new Filter(keyPath, DataTypes.STRING, ">", aValue);
        assertFalse(filterNotGreater.accept(node));

        Filter filterSmaller = new Filter(keyPath, DataTypes.STRING, "<", bValue);
        assertTrue(filterSmaller.accept(node));
        Filter filterNotSmaller = new Filter(keyPath, DataTypes.STRING, "<", aValue);
        assertFalse(filterNotSmaller.accept(node));

        Filter filterGreaterOrEqual = new Filter(keyPath, DataTypes.STRING, ">=", aValue);
        assertTrue(filterGreaterOrEqual.accept(node));
        Filter filterNotGreaterOrEqual = new Filter(keyPath, DataTypes.STRING, ">=", bValue);
        assertFalse(filterNotGreaterOrEqual.accept(node));

        Filter filterSmallerOrEqual = new Filter(keyPath, DataTypes.STRING, "<=", aValue);
        assertTrue(filterSmallerOrEqual.accept(node));
        Filter filterSmallerOrEqualBValue = new Filter(keyPath, DataTypes.STRING, "<=", bValue);
        assertTrue(filterSmallerOrEqualBValue.accept(node));
    }

    @Test
    void filterDates() {
        final String keyPath = "/A";
        final String aValue = "2023-12-30";
        final String bValue = "2023-12-31";
        ParseFormats pf = new ParseFormats();
        pf.setDate("yyyy-MM-dd");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("A",aValue);

        Filter filterEquals = new Filter(keyPath, DataTypes.DATE, "=", aValue);
        assertTrue(filterEquals.accept(node, pf));
        Filter filterNotEquals = new Filter(keyPath, DataTypes.DATE, "=", bValue);
        assertFalse(filterNotEquals.accept(node, pf));

        Filter filterDifferent = new Filter(keyPath, DataTypes.DATE, "!=", bValue);
        assertTrue(filterDifferent.accept(node, pf));
        Filter filterNotDifferent = new Filter(keyPath, DataTypes.DATE, "!=", aValue);
        assertFalse(filterNotDifferent.accept(node, pf));

        Filter filterGreater = new Filter(keyPath, DataTypes.DATE, ">", bValue);
        assertFalse(filterGreater.accept(node, pf));
        Filter filterNotGreater = new Filter(keyPath, DataTypes.DATE, ">", aValue);
        assertFalse(filterNotGreater.accept(node, pf));

        Filter filterSmaller = new Filter(keyPath, DataTypes.DATE, "<", bValue);
        assertTrue(filterSmaller.accept(node, pf));
        Filter filterNotSmaller = new Filter(keyPath, DataTypes.DATE, "<", aValue);
        assertFalse(filterNotSmaller.accept(node, pf));

        Filter filterGreaterOrEqual = new Filter(keyPath, DataTypes.DATE, ">=", aValue);
        assertTrue(filterGreaterOrEqual.accept(node, pf));
        Filter filterNotGreaterOrEqual = new Filter(keyPath, DataTypes.DATE, ">=", bValue);
        assertFalse(filterNotGreaterOrEqual.accept(node, pf));

        Filter filterSmallerOrEqual = new Filter(keyPath, DataTypes.DATE, "<=", aValue);
        assertTrue(filterSmallerOrEqual.accept(node, pf));
        Filter filterSmallerOrEqualBValue = new Filter(keyPath, DataTypes.DATE, "<=", bValue);
        assertTrue(filterSmallerOrEqualBValue.accept(node, pf));

        node.put("A", "2023/99/55");
        Filter filterEqualsWrongDateFormat = new Filter(keyPath, DataTypes.DATE, "=", bValue);
        filterEqualsWrongDateFormat.hideLogs();
        assertFalse(filterEqualsWrongDateFormat.accept(node, pf));

        pf.setDate(null);
        assertThrows(IllegalArgumentException.class, () -> filterSmallerOrEqualBValue.accept(node, pf));
    }

    @Test
    void filterTimes() {
        final String keyPath = "/A";
        final String aValue = "14:30:00";
        final String bValue = "14:31:01";
        ParseFormats pf = new ParseFormats();
        pf.setTime("HH:mm:ss");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("A",aValue);

        Filter filterEquals = new Filter(keyPath, DataTypes.TIME, "=", aValue);
        assertTrue(filterEquals.accept(node, pf));
        Filter filterNotEquals = new Filter(keyPath, DataTypes.TIME, "=", bValue);
        assertFalse(filterNotEquals.accept(node, pf));

        Filter filterDifferent = new Filter(keyPath, DataTypes.TIME, "!=", bValue);
        assertTrue(filterDifferent.accept(node, pf));
        Filter filterNotDifferent = new Filter(keyPath, DataTypes.TIME, "!=", aValue);
        assertFalse(filterNotDifferent.accept(node, pf));

        Filter filterGreater = new Filter(keyPath, DataTypes.TIME, ">", bValue);
        assertFalse(filterGreater.accept(node, pf));
        Filter filterNotGreater = new Filter(keyPath, DataTypes.TIME, ">", aValue);
        assertFalse(filterNotGreater.accept(node, pf));

        Filter filterSmaller = new Filter(keyPath, DataTypes.TIME, "<", bValue);
        assertTrue(filterSmaller.accept(node, pf));
        Filter filterNotSmaller = new Filter(keyPath, DataTypes.TIME, "<", aValue);
        assertFalse(filterNotSmaller.accept(node, pf));

        Filter filterGreaterOrEqual = new Filter(keyPath, DataTypes.TIME, ">=", aValue);
        assertTrue(filterGreaterOrEqual.accept(node, pf));
        Filter filterNotGreaterOrEqual = new Filter(keyPath, DataTypes.TIME, ">=", bValue);
        assertFalse(filterNotGreaterOrEqual.accept(node, pf));

        Filter filterSmallerOrEqual = new Filter(keyPath, DataTypes.TIME, "<=", aValue);
        assertTrue(filterSmallerOrEqual.accept(node, pf));
        Filter filterSmallerOrEqualBValue = new Filter(keyPath, DataTypes.TIME, "<=", bValue);
        assertTrue(filterSmallerOrEqualBValue.accept(node, pf));

        node.put("A", "55;77");
        Filter filterEqualsWrongFormat = new Filter(keyPath, DataTypes.TIME, "=", aValue);
        filterEqualsWrongFormat.hideLogs();
        assertFalse(filterEqualsWrongFormat.accept(node, pf));

        node.put("A",aValue);
        pf.setTime(null);
        assertTrue(filterSmallerOrEqualBValue.accept(node, pf)); // Time pattern default is ok for this
    }

    @Test
    void filterDecimals() {
        final String keyPath = "/A";
        final String aValue = "46.71789";
        final String bValue = "50.01";
        ParseFormats pf = new ParseFormats();
        pf.setDecimal("#.#");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("A",aValue);

        Filter filterEquals = new Filter(keyPath, DataTypes.DECIMAL, "=", aValue);
        assertTrue(filterEquals.accept(node, pf));
        Filter filterNotEquals = new Filter(keyPath, DataTypes.DECIMAL, "=", bValue);
        assertFalse(filterNotEquals.accept(node, pf));

        Filter filterDifferent = new Filter(keyPath, DataTypes.DECIMAL, "!=", bValue);
        assertTrue(filterDifferent.accept(node, pf));
        Filter filterNotDifferent = new Filter(keyPath, DataTypes.DECIMAL, "!=", aValue);
        assertFalse(filterNotDifferent.accept(node, pf));

        Filter filterGreater = new Filter(keyPath, DataTypes.DECIMAL, ">", bValue);
        assertFalse(filterGreater.accept(node, pf));
        Filter filterNotGreater = new Filter(keyPath, DataTypes.DECIMAL, ">", aValue);
        assertFalse(filterNotGreater.accept(node, pf));

        Filter filterSmaller = new Filter(keyPath, DataTypes.DECIMAL, "<", bValue);
        assertTrue(filterSmaller.accept(node, pf));
        Filter filterNotSmaller = new Filter(keyPath, DataTypes.DECIMAL, "<", aValue);
        assertFalse(filterNotSmaller.accept(node, pf));

        Filter filterGreaterOrEqual = new Filter(keyPath, DataTypes.DECIMAL, ">=", aValue);
        assertTrue(filterGreaterOrEqual.accept(node, pf));
        Filter filterNotGreaterOrEqual = new Filter(keyPath, DataTypes.DECIMAL, ">=", bValue);
        assertFalse(filterNotGreaterOrEqual.accept(node, pf));

        Filter filterSmallerOrEqual = new Filter(keyPath, DataTypes.DECIMAL, "<=", aValue);
        assertTrue(filterSmallerOrEqual.accept(node, pf));
        Filter filterSmallerOrEqualBValue = new Filter(keyPath, DataTypes.DECIMAL, "<=", bValue);
        assertTrue(filterSmallerOrEqualBValue.accept(node, pf));

        node.put("A", "1;3");
        Filter filterEqualsWrongFormat = new Filter(keyPath, DataTypes.DECIMAL, "=", aValue);
        filterEqualsWrongFormat.hideLogs();
        assertFalse(filterEqualsWrongFormat.accept(node, pf));

        node.put("A", aValue);
        pf.setDecimal(null);
        assertTrue(filterSmallerOrEqualBValue.accept(node, pf)); // Time pattern default is ok for this
    }

    @Test
    void missingNode() {
        final String keyPath = "/A";
        final String aValue = "46.71789";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("B",aValue);
        ParseFormats pf = new ParseFormats();
        pf.setDecimal("#.#");

        Filter filterEquals = new Filter(keyPath, DataTypes.DECIMAL, "=", aValue);
        // It is going to look for A but there is B in the node
        assertFalse(filterEquals.accept(node, pf));
    }


}