package it.unibz.andreypaolo.highlevel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibz.andreypaolo.lowlevel.Utility;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValueObjectTest {

    @Test
    void sortTest() throws Exception {
        String jsonStringForA = "{\"id\":\"A\"}";
        String jsonStringForB = "{\"id\":\"B\"}";

        ObjectMapper mapper = Utility.getObjectMapper();
        JsonNode jsonForA = mapper.readTree(jsonStringForA);
        JsonNode jsonForB = mapper.readTree(jsonStringForB);
        SimpleDateFormat sdf = new SimpleDateFormat(Mobility.MOBILITY_DATE_FORMAT_STR);

        List<ValueObject> myList = new ArrayList<>();
        myList.add(new ValueObject("id", jsonForB, sdf));
        myList.add(new ValueObject("id", jsonForA, sdf));

        Collections.sort(myList);

        String firstTextValue = myList.get(0).obtainTextValue();
        String secondTextValue = myList.get(1).obtainTextValue();
        assertEquals("A", firstTextValue);
        assertEquals("B", secondTextValue);
    }
}
