package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.andreypaolo.conf.Configuration;
import it.unibz.andreypaolo.conf.Filter;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.QueryOrderField;
import it.unibz.andreypaolo.conf.datatypes.DataTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainComponentTest {
    private final static String STANDARD_FILE = "orderedQueries_standard.yaml";
    private MainComponent mainComponent;
    private final Path workingDirectory= Path.of("", "src/test/resources");

    private final static String MOBILITY_URL = "https://mobility.api.opendatahub.com/v2/flat%2Cevent/%2A?limit=200&offset=0&shownull=false&distinct=true";
    private final static String TOURISM_URL = "https://tourism.opendatahub.com/v1/Event?pagenumber=1&removenullvalues=false";

    @BeforeEach
    void setup() {
        mainComponent = new MainComponent(DataProviderFactory.createProvider());
    }

    @Test
    void joinAndOrder() throws IOException, InterruptedException {
        Path path = workingDirectory.resolve(STANDARD_FILE);
        String result = mainComponent.executeQueries(path.toFile());
        assertFalse(result.isEmpty());

        // the filter in the configuration file specifies: evend = 2022-05-11 00:00:00.000+0000
        assertTrue(result.contains("mobility"));
        assertTrue(result.contains("2022-06-04 00:00:00.000+0000"));
        assertTrue(result.contains("\"evtransactiontime\" : \"2022-06-02 17:10:00.827+0000\""));

        assertTrue(result.contains("tourism"));
        // the filter in the configuration file specifies: from 2022-06-01T00:00:00 to 2022-07-01T00:00:00
        assertFalse(result.contains("\"DateBegin\": \"2022-08-05T00:00:00\""));
        assertTrue(result.contains("2022-06-01T00:00:00"));

        assertTrue(result.indexOf("tourism") < result.indexOf("mobility"));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result);
        assertTrue(root.isArray());
        ArrayNode arrayNode = (ArrayNode)root;
        assertEquals("tourism", arrayNode.get(0).at("/provider").textValue());
        assertEquals("mobility", arrayNode.get(arrayNode.size() -1 ).at("/provider").textValue());
//        System.out.println(result);
    }

    @Test
    void readEmptyData() throws IOException, InterruptedException {
        Configuration conf = new Configuration();
        final ObjectMapper mapper = new ObjectMapper();
        MainComponent main = new MainComponent((serviceUrl) ->  mapper.createObjectNode());
        List<ItemDTO> result = main.readDataFromService(conf);
        assertTrue(result.isEmpty());
    }

    @Test
    void validateConfiguration() {
        assertThrows(NullPointerException.class, () -> mainComponent.checkConfiguration(null));
        Configuration conf = new Configuration();
        ParseFormats pf = new ParseFormats();
        conf.setParseFormats(pf);

        LinkedList<Configuration> serviceList = new LinkedList<>();
        serviceList.add(conf);
        NullPointerException ex;

        ex = assertThrows(NullPointerException.class, () -> mainComponent.checkConfiguration(serviceList));
        assertEquals("The url field is missing in the service at position: 1", ex.getMessage());

        conf.setUrl("https://www.byteliberi.com/");
        ex = assertThrows(NullPointerException.class, () -> mainComponent.checkConfiguration(serviceList));
        assertEquals("The provider name is missing in the service at position: 1", ex.getMessage());

        conf.setProvider("Provider");
        ex = assertThrows(NullPointerException.class, () -> mainComponent.checkConfiguration(serviceList));
        assertEquals("The queryOrderField is missing in the service at position 1", ex.getMessage());

        QueryOrderField qof = new QueryOrderField();
        conf.setQueryOrderField(qof);
        ex = assertThrows(NullPointerException.class, () -> mainComponent.checkConfiguration(serviceList));
        assertEquals("The path field in a queryOrderField is missing in the service at position 1", ex.getMessage());
        conf.getQueryOrderField().setPath("/evend");

        IllegalArgumentException exIllegal;
        qof.setType(DataTypes.DATE);
        pf.setDate(null);
        exIllegal = assertThrows(IllegalArgumentException.class, () -> mainComponent.checkConfiguration(serviceList));
        assertEquals("The queryOrderField is a date but no date format has been specified at position 1", exIllegal.getMessage());

        // Decimal and time have default values for the format
    }

    @Test
    void readMobilityConfiguration() throws IOException {
        Path path = workingDirectory.resolve(STANDARD_FILE);
        List<Configuration> configurationList = mainComponent.readConfiguration(path.toFile());
        assertEquals(2, configurationList.size());
        Configuration configuration = configurationList.get(0);
        assertEquals(MOBILITY_URL, configuration.getUrl());
        assertEquals("data", configuration.getDataGroupPrefix().orElse(""));
        QueryOrderField queryOrderField = configuration.getQueryOrderField();
        assertEquals("/evend", queryOrderField.getPath());
        ParseFormats pf = configuration.getParseFormats().orElse(new ParseFormats());
        assertEquals("yyyy-MM-dd HH:mm:ss.SSSZ", pf.getDate().orElse(""));
        assertEquals(DataTypes.DATE, queryOrderField.getType());
        List<Filter> firstResponseFields = configuration.getFilters();
        assertEquals(1, firstResponseFields.size());
        Filter filter = firstResponseFields.get(0);
        assertEquals("/evend", filter.getPath());
        assertEquals(DataTypes.DATE, filter.getType());
        assertEquals("=", filter.getOperator());
        assertEquals("2022-06-04 00:00:00.000+0000", filter.getFieldValue());
    }

    @Test
    void readTourismConfiguration() throws IOException {
        Path path = workingDirectory.resolve(STANDARD_FILE);
        List<Configuration> configurationList = mainComponent.readConfiguration(path.toFile());
        assertEquals(2, configurationList.size());
        Configuration configuration = configurationList.get(1);
        assertEquals(TOURISM_URL, configuration.getUrl());
        assertEquals("Items", configuration.getDataGroupPrefix().orElse(""));
        QueryOrderField queryOrderField = configuration.getQueryOrderField();
        assertEquals("/DateBegin", queryOrderField.getPath());
        ParseFormats pf = configuration.getParseFormats().orElse(new ParseFormats());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", pf.getDate().orElse(""));
        assertEquals(DataTypes.DATE, queryOrderField.getType());
        List<Filter> filters = configuration.getFilters();
        assertEquals(2, filters.size());
        Filter filter0 = filters.get(0);
        assertEquals("/DateBegin", filter0.getPath());
        assertEquals(DataTypes.DATE, filter0.getType());
        assertEquals(">=", filter0.getOperator());
        assertEquals("2022-06-01T00:00:00", filter0.getFieldValue());
        Filter filter1 = filters.get(1);
        assertEquals("/DateBegin", filter1.getPath());
        assertEquals(DataTypes.DATE, filter1.getType());
        assertEquals("<=", filter1.getOperator());
        assertEquals("2022-07-01T00:00:00", filter1.getFieldValue());
    }

    @Test
    void createJSonResponse() {
        ArrayList<ItemDTO> items = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node1 = mapper.createObjectNode();
        node1.put("a", "b");
        ObjectNode node2 = mapper.createObjectNode();
        node2.put("c", "d");

        items.add(new ItemDTO.Builder()
                .setProvider("myProvider")
                .setQueryFieldPath("a")
                .setOriginalItem(node1)
                .setQueryFieldType(DataTypes.STRING)
                .build());

        items.add(new ItemDTO.Builder()
                .setProvider("myProvider")
                .setQueryFieldPath("a")
                .setOriginalItem(node2)
                .setQueryFieldType(DataTypes.STRING)
                .build());
        String actualJson = mainComponent.createJSonResponse(items);

        // expected
        ArrayNode expectedNode = mapper.createArrayNode();
        ObjectNode n0 = mapper.createObjectNode();
        n0.put("provider", "myProvider");
        ObjectNode originalItem0 = mapper.createObjectNode();
        originalItem0.put("a", "b");
        n0.set("originalItem", originalItem0);
        expectedNode.add(n0);
        ObjectNode n1 = mapper.createObjectNode();
        n1.put("provider", "myProvider");
        ObjectNode originalItem1 = mapper.createObjectNode();
        originalItem1.put("c", "d");
        n1.set("originalItem", originalItem1);
        expectedNode.add(n1);

        assertEquals(expectedNode.toPrettyString(), actualJson);
    }

    @Test
    void mobility() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setUrl(MOBILITY_URL);
        configuration.setDataGroupPrefix("data");
        ParseFormats pf = new ParseFormats();
        pf.setDate("yyyy-MM-dd HH:mm:ss.SSSZ");
        configuration.setParseFormats(pf);
        configuration.setQueryOrderField(new QueryOrderField("/evend",DataTypes.DATE));
        List<ItemDTO> items = mainComponent.readDataFromService(configuration);
        assertFalse(items.isEmpty());
        ItemDTO before = items.get(0);
        ItemDTO after = items.get(4);
        assertTrue( before.compareTo(after) < 0);
        assertNotEquals(before.hashCode(), after.hashCode());
        assertNotEquals(before, after);
    }

    @Test
    void tourism() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setUrl(TOURISM_URL);
        configuration.setDataGroupPrefix("Items");
        ParseFormats pf = new ParseFormats();
        pf.setDate("yyyy-MM-dd'T'HH:mm:ss");
        configuration.setParseFormats(pf);
        QueryOrderField queryOrderField = new QueryOrderField();
        queryOrderField.setPath("/EventDate/0/From");
        queryOrderField.setType(DataTypes.DATE);
        configuration.setQueryOrderField(queryOrderField);

        List<ItemDTO> items = mainComponent.readDataFromService(configuration);
        assertFalse(items.isEmpty());
        ItemDTO before = items.get(0);
        ItemDTO after = items.get(4);
        assertTrue(before.compareTo(after) < 0);
        assertNotEquals(before.hashCode(), after.hashCode());
        assertNotEquals(before, after);
    }

    @Test
    void filter() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        ArrayNode dataNode = root.putArray("data");

        ObjectNode eventNode1 = mapper.createObjectNode();
        eventNode1.put("evname", "125");
        dataNode.add(eventNode1);

        ObjectNode eventNode2 = mapper.createObjectNode();
        eventNode2.put("evname", "126");
        dataNode.add(eventNode2);

        List<Filter> conditions = new LinkedList<>();
        Filter filter = new Filter();
        filter.setOperator("=");
        filter.setType(DataTypes.STRING); // TODO put a numeric value
        filter.setFieldValue("126");
        filter.setPath("/evname");
        conditions.add(filter);


        Configuration conf = new Configuration();
        conf.setFilters(conditions);
        QueryOrderField queryOrderField = new QueryOrderField();
        queryOrderField.setType(DataTypes.STRING);
        queryOrderField.setPath("/A");
        conf.setQueryOrderField(queryOrderField);
        List<ItemDTO> response = mainComponent.filter(root.get("data"), conf);

/*
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
*/
        assertEquals(1, response.size());
        assertEquals("126", response.get(0).getOriginalItem().at("/evname").textValue());
    }
}
