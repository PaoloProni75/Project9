package it.unibz.andreypaolo.lowlevel;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class OpenDataHubMock implements DataProviderApi {
    public static final String TOURISM_V1_EVENT="https://tourism.opendatahub.com/v1/Event?pagenumber=1&removenullvalues=false";
    public static final String MOBILITY_REPRESENTATION_EVENTORIGINS_URL ="https://mobility.api.opendatahub.com/v2/flat%2Cevent/%2A?limit=200&offset=0&shownull=false&distinct=true";
    private final Path workingDirectory= Path.of("", "src/test/resources");

    @Override
    public JsonNode readDataFromService(String serviceUrl) throws IOException {
        String jsonFileName = switch (serviceUrl) {
            case MOBILITY_REPRESENTATION_EVENTORIGINS_URL -> "mobilityV2RepresentationEventorigins.json";
            case TOURISM_V1_EVENT -> "tourismV1Event.json";
            default -> throw new IOException();
        };
        // URL constants and file names for testing could be grouped in enum

        String str = Files.readString(workingDirectory.resolve(jsonFileName));

        return Utility.getObjectMapper().readTree(str);
    }
}
