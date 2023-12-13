package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        // TODO add way more test !
        assertTrue(result.contains("2022-05-11 00:00:00.000+0000"));
        assertTrue(result.contains("2022-06-01T00:00:00"));
//        System.out.println(result);
    }

    @Test
    void readMobilityConfiguration() throws IOException {
        Path path = workingDirectory.resolve(STANDARD_FILE);
        List<Configuration> configurationList = mainComponent.readConfiguration(path.toFile());
        assertEquals(2, configurationList.size());
        Configuration configuration = configurationList.get(0);
        assertEquals(MOBILITY_URL, configuration.getUrl());
        assertEquals("data", configuration.getDataGroupPrefix());
        QueryOrderField queryOrderField = configuration.getQueryOrderField();
        assertEquals("/evend", queryOrderField.getPath());
        assertEquals("yyyy-MM-dd HH:mm:ss.SSSZ", queryOrderField.getDateFormat());
        assertTrue(queryOrderField.isDateType());
        List<String> firstResponseFields = configuration.getResponseFields();
        assertEquals(4, firstResponseFields.size());
        int fieldIndex = -1;
        assertEquals("data/evend", firstResponseFields.get(++fieldIndex));
        assertEquals("data/evcategory", firstResponseFields.get(++fieldIndex));
        assertEquals("data/evmetadata", firstResponseFields.get(++fieldIndex));
        assertEquals("data/prlineage", firstResponseFields.get(++fieldIndex));
    }

    @Test
    void readTourismConfiguration() throws IOException {
        Path path = workingDirectory.resolve(STANDARD_FILE);
        List<Configuration> configurationList = mainComponent.readConfiguration(path.toFile());
        assertEquals(2, configurationList.size());
        Configuration configuration = configurationList.get(1);
        assertEquals(TOURISM_URL, configuration.getUrl());
        assertEquals("Items", configuration.getDataGroupPrefix());
        QueryOrderField queryOrderField = configuration.getQueryOrderField();
        assertEquals("/EventDate/0/From", queryOrderField.getPath());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", queryOrderField.getDateFormat());
        assertTrue(queryOrderField.isDateType());
        List<String> firstResponseFields = configuration.getResponseFields();
        assertEquals(3, firstResponseFields.size());
        int fieldIndex = -1;
        assertEquals("Items/EventDate/From", firstResponseFields.get(++fieldIndex));
        assertEquals("Items/EventDate/Begin", firstResponseFields.get(++fieldIndex));
        assertEquals("Items/EventDate/Cancelled", firstResponseFields.get(++fieldIndex));
    }

    @Test
    void createJSonResponse() {
        ArrayList<ValueObject> valueObjects = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node1 = mapper.createObjectNode();
        node1.put("a", "b");
        ObjectNode node2 = mapper.createObjectNode();
        node2.put("c", "d");
        final SimpleDateFormat sdf = new SimpleDateFormat();
        valueObjects.add(new ValueObject("a", node1, sdf));
        valueObjects.add(new ValueObject("a", node2, sdf));
        String actualJson = mainComponent.createJSonResponse(valueObjects);
        String expectedJson = '[' + System.lineSeparator() + "{\"a\":\"b\"}," + System.lineSeparator() + "{\"c\":\"d\"}"
                            + System.lineSeparator() + ']';

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void mobility() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setUrl(MOBILITY_URL);
        configuration.setDataGroupPrefix("data");
        QueryOrderField queryOrderField = new QueryOrderField();
        queryOrderField.setPath("/evend");
        queryOrderField.setDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        configuration.setQueryOrderField(queryOrderField);

        List<ValueObject> items = mainComponent.readDataFromService(configuration);
        assertFalse(items.isEmpty());
        ValueObject before = items.get(0);
        ValueObject after = items.get(4);
        assertTrue( before.compareTo(after) < 0);
        assertNotEquals(before.hashCode(), after.hashCode());
        assertNotEquals(before, after);
    }

    @Test
    void tourism() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setUrl(TOURISM_URL);
        configuration.setDataGroupPrefix("Items");
        QueryOrderField queryOrderField = new QueryOrderField();
        queryOrderField.setPath("/EventDate/0/From");
        queryOrderField.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        configuration.setQueryOrderField(queryOrderField);

        List<ValueObject> items = mainComponent.readDataFromService(configuration);
        assertFalse(items.isEmpty());
        ValueObject before = items.get(0);
        ValueObject after = items.get(4);
        assertTrue(before.compareTo(after) < 0);
        assertNotEquals(before.hashCode(), after.hashCode());
        assertNotEquals(before, after);
    }
}
