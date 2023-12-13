package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.unibz.andreypaolo.DataProviderApi;
import it.unibz.andreypaolo.OpenDataHubMock;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Check the integrity of test data and that mock code works
 */
class OpenDataHubMockTest {
    private final static String DATA = "data";
    private final static String EVENT_DATE = "evend";
    private final static String FROM = "From";
    private final static String ITEMS_NODE_NAME = "Items";

    @Test
    void mockTestMobility() throws IOException, InterruptedException {
        DataProviderApi dataProvider = new OpenDataHubMock();
        JsonNode nodes = dataProvider.readDataFromService(OpenDataHubMock.MOBILITY_REPRESENTATION_EVENTORIGINS_URL);
        ArrayNode arrayNode = (ArrayNode) nodes.get(DATA);
        assertEquals("2022-05-11 00:00:00.000+0000", arrayNode.get(0).findValue(EVENT_DATE).textValue());
        assertEquals("2022-05-12 00:00:00.000+0000", arrayNode.get(4).findValue(EVENT_DATE).textValue());
        assertEquals(200, arrayNode.size());
    }

    @Test
    public void mockTestTourism() throws IOException, InterruptedException {
        DataProviderApi dataProvider = new OpenDataHubMock();
        JsonNode nodes = dataProvider.readDataFromService(OpenDataHubMock.TOURISM_V1_EVENT);
        ArrayNode arrayNode = (ArrayNode) nodes.get(ITEMS_NODE_NAME);
        JsonNode firstNode = arrayNode.get(0);
        JsonNode node0FromElements = firstNode.findValue(FROM);
        assertEquals("2022-06-01T00:00:00", node0FromElements.textValue());
        JsonNode secondNode = arrayNode.get(1);
        JsonNode node1FromElements = secondNode.findValue(FROM);
        assertEquals("2017-05-26T00:00:00", node1FromElements.textValue());
    }
}
