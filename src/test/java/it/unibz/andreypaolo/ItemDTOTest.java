package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.andreypaolo.conf.datatypes.DataTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemDTOTest {
    @Test
    void nullOrderField() {
        ObjectMapper mapper = MapperSingleton.getInstance().getMapper();
        ObjectNode originalItem = mapper.createObjectNode();
        originalItem.put("a", "value a");
        ItemDTO item = new ItemDTO.Builder()
                .setQueryFieldPath("/b")
                .setOriginalItem(originalItem)
                .build();

        String orderFieldText = item.getOrderFieldText();
        assertEquals("", orderFieldText);
    }

    @Test
    void itemEqualsTheSameNullOrEmpty() {
        ItemDTO dateItem = new ItemDTO.Builder()
                .setQueryFieldType(DataTypes.DATE)
                .build();
        final boolean sameEquals = dateItem.equals(dateItem);
        assertTrue(sameEquals);
        assertFalse(dateItem.equals(null));
        assertFalse(dateItem.equals(""));
    }

    @Test
    void compareDifferentTypes() {
        ItemDTO dateItem = new ItemDTO.Builder()
                .setQueryFieldType(DataTypes.DATE)
                .build();
        ItemDTO stringItem = new ItemDTO.Builder()
                .setQueryFieldType(DataTypes.STRING)
                .build();

        assertThrows(IllegalArgumentException.class,() -> {
            dateItem.compareTo(stringItem);
        });
    }

    @Test
    void compareToNull() {
        ItemDTO item = new ItemDTO.Builder().build();
        assertEquals(1, item.compareTo(null));
    }
}