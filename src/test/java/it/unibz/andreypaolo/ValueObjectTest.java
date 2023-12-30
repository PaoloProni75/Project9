package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.datatypes.DataTypes;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValueObjectTest {

    @Test
    void sortString() throws Exception {
        ObjectMapper mapper = MapperSingleton.getInstance().getMapper();

        List<ItemDTO> myList = new ArrayList<>();
        final String queryField = "/id";
        myList.add(new ItemDTO.Builder()
                .setQueryFieldPath(queryField)
                .setOriginalItem(mapper.readTree("{\"id\":\"B\"}"))
                .build());

        myList.add(new ItemDTO.Builder()
                .setQueryFieldPath(queryField)
                .setOriginalItem(mapper.readTree("{\"id\":\"A\"}"))
                .build());

        Collections.sort(myList);

        assertEquals("A", myList.get(0).getOrderFieldText());
        assertEquals("B", myList.get(1).getOrderFieldText());
    }

    @Test
    void sortDate() throws Exception {
        String jsonStringForA = "{\"dta\":\"2023-12-12 00:00:00.000+0000\"}";
        String jsonStringForB = "{\"dta\":\"2023-12-13 00:00:00.000+0000\"}";

        ObjectMapper mapper = MapperSingleton.getInstance().getMapper();
        JsonNode jsonForA = mapper.readTree(jsonStringForA);
        JsonNode jsonForB = mapper.readTree(jsonStringForB);

        List<ItemDTO> myList = new ArrayList<>();
        final String queryField = "/dta";
        final String dateFormat = "yyyy-MM-dd HH:mm:ss.SSSZ";
        ParseFormats pf = new ParseFormats();
        pf.setDate(dateFormat);
        myList.add(new ItemDTO.Builder()
                .setQueryFieldPath(queryField)
                .setQueryFieldType(DataTypes.DATE)
                .setParseFormats(pf)
                .setProvider("A")
                .setOriginalItem(jsonForB)
                .build());

        myList.add(new ItemDTO.Builder()
                .setQueryFieldPath(queryField)
                .setQueryFieldType(DataTypes.DATE)
                .setParseFormats(pf)
                .setProvider("B")
                .setOriginalItem(jsonForA)
                .build());

        Collections.sort(myList);

        String firstTextValue = myList.get(0).getOrderFieldText();
        String secondTextValue = myList.get(1).getOrderFieldText();
        assertEquals("2023-12-12 00:00:00.000+0000", firstTextValue);
        assertEquals("2023-12-13 00:00:00.000+0000", secondTextValue);
    }
}
