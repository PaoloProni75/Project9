package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenDataHubTest {
    private final static String DATA = "data";
    private final static String EVENT_DATE = "evend";
    private final static String FROM = "From";
    private final static String ITEMS_NODE_NAME = "Items";
    private final static String MOBILITY_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss.SSSZ";
    private final static String TOURISM_DATE_FORMAT_STR = "yyyy-MM-dd'T'HH:mm:ss";

    @Test
    void testMobility() throws IOException, ParseException, InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat(MOBILITY_DATE_FORMAT_STR);
        dateTest(OpenDataHubMock.MOBILITY_REPRESENTATION_EVENTORIGINS_URL, DATA, 0, 4, EVENT_DATE, sdf);
    }

    @Test
    void testTourism() throws IOException, ParseException, InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat(TOURISM_DATE_FORMAT_STR);
        dateTest(OpenDataHubMock.TOURISM_V1_EVENT, ITEMS_NODE_NAME, 1, 0, FROM, sdf);
    }

    private void dateTest(String serviceUrl, String arrayNodeName, int beforeIndex, int afterIndex, String dateNodeName,
                          DateFormat dateFormat) throws IOException, ParseException, InterruptedException {

        DataProviderApi dataProvider = DataProviderFactory.createProvider();
        JsonNode nodes = dataProvider.readDataFromService(serviceUrl);
        JsonNode arrayCandidates = nodes.findValue(arrayNodeName);
        assertTrue(arrayCandidates != null && arrayCandidates.isArray());

        ArrayNode arrayNode = (ArrayNode)arrayCandidates;
        String before = arrayNode.get(beforeIndex).findValue(dateNodeName).textValue();
        String after = arrayNode.get(afterIndex).findValue(dateNodeName).textValue();
        Date beforeDate = dateFormat.parse(before);
        Date afterDate = dateFormat.parse(after);
        assertTrue(beforeDate.compareTo(afterDate) < 0);
    }
}