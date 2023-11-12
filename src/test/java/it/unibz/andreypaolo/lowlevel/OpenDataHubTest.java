package it.unibz.andreypaolo.lowlevel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.unibz.andreypaolo.DataProviderFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static it.unibz.andreypaolo.highlevel.Mobility.MOBILITY_DATE_FORMAT_STR;
import static it.unibz.andreypaolo.highlevel.ServiceApi.*;
import static it.unibz.andreypaolo.highlevel.Tourism.TOURISM_DATE_FORMAT_STR;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * Unit test.
 */
public class OpenDataHubTest {
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