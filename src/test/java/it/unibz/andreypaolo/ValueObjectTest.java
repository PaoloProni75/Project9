package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValueObjectTest {

    @Test
    void sortString() throws Exception {
        String jsonStringForA = "{\"id\":\"A\"}";
        String jsonStringForB = "{\"id\":\"B\"}";

        ObjectMapper mapper = Utility.getObjectMapper();
        JsonNode jsonForA = mapper.readTree(jsonStringForA);
        JsonNode jsonForB = mapper.readTree(jsonStringForB);

        List<ValueObject> myList = new ArrayList<>();
        final String queryField = "/id";
        myList.add(new ValueObject(queryField, jsonForB));
        myList.add(new ValueObject(queryField, jsonForA));

        Collections.sort(myList);

        String firstTextValue = myList.get(0).obtainOrderFieldAsTextValue();
        String secondTextValue = myList.get(1).obtainOrderFieldAsTextValue();
        assertEquals("A", firstTextValue);
        assertEquals("B", secondTextValue);
    }

    @Test
    void sortDate() throws Exception {
        String jsonStringForA = "{\"dta\":\"2023-12-12 00:00:00.000+0000\"}";
        String jsonStringForB = "{\"dta\":\"2023-12-13 00:00:00.000+0000\"}";

        ObjectMapper mapper = Utility.getObjectMapper();
        JsonNode jsonForA = mapper.readTree(jsonStringForA);
        JsonNode jsonForB = mapper.readTree(jsonStringForB);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

        List<ValueObject> myList = new ArrayList<>();
        final String queryField = "/dta";
        myList.add(new ValueObject(queryField, jsonForB));
        myList.add(new ValueObject(queryField, jsonForA));

        Collections.sort(myList);

        String firstTextValue = myList.get(0).obtainOrderFieldAsTextValue();
        String secondTextValue = myList.get(1).obtainOrderFieldAsTextValue();
        assertEquals("2023-12-12 00:00:00.000+0000", firstTextValue);
        assertEquals("2023-12-13 00:00:00.000+0000", secondTextValue);
    }
}
