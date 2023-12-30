package it.unibz.andreypaolo.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.andreypaolo.conf.datatypes.DataTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
