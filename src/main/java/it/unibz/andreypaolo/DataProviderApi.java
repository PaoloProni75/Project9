package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Common interface for real a mock data providers
 */
public interface DataProviderApi {
    JsonNode readDataFromService(String serviceUrl) throws IOException, InterruptedException;
}
