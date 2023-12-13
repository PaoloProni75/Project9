package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainComponent {

    private final DataProviderApi dataProvider;

    public MainComponent(DataProviderApi dataProvider) {
        // if dataProvider is null, it takes the OpenDataHub by default
        this.dataProvider = Objects.requireNonNullElseGet(dataProvider, OpenDataHub::new);
    }

    List<Configuration> readConfiguration(File configurationFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.findAndRegisterModules(); // to handle Date fields
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Configuration.class);
        return mapper.readValue(configurationFile, listType);
    }

    List<ValueObject> readDataFromService(Configuration service) throws IOException, InterruptedException {
        LinkedList<ValueObject> resultList = new LinkedList<>();
        JsonNode nodeList = dataProvider.readDataFromService(service.getUrl());
        if (nodeList != null && !nodeList.isNull()) {
            JsonNode dataNodeList = nodeList.get(service.getDataGroupPrefix());
            if (dataNodeList.isArray() && dataNodeList instanceof ArrayNode arrayNodes) { // pattern variable, comfortable Java :-)
                for (JsonNode node : arrayNodes)
                    resultList.add(new ValueObject(service.getQueryFieldPath(), node, service.getDateFormat()));
            }
            else
                resultList.add(new ValueObject(service.getQueryFieldPath(), dataNodeList, service.getDateFormat()));
        }

        return resultList;
    }

     String createJSonResponse(List<ValueObject> valueObjectList) {
        assert valueObjectList != null;
        return valueObjectList.stream()
                 .map(vo -> vo.getBodyNodes().toString())
                 .collect(Collectors.joining("," + System.lineSeparator(),
                         "[" + System.lineSeparator(),
                         System.lineSeparator() + "]"));
    }

    void checkConfiguration(final List<Configuration> serviceList) throws ServiceConfigurationError {
        if (serviceList == null)
            throw new IllegalArgumentException("serviceList parameter in checkConfiguration must not be null");

        IntStream.range(0, serviceList.size()) // we avoid an annoying warning about a replaced local variable
                 .forEach((i) -> {
                     final int position = i + 1;
                     Configuration conf = serviceList.get(i);
                     if (conf.getUrl() == null)
                         throw new ServiceConfigurationError("The url field is missing in the service at position: " + position);

                     // dataGroupPrefix can be null, so the output will simply create an unnamed array
                     QueryOrderField orderField = conf.getQueryOrderField();
                     if (orderField == null)
                         throw new ServiceConfigurationError("The queryOrderField is missing in the service at position " + position);
                     else
                         if (orderField.getPath() == null)
                             throw new ServiceConfigurationError("The path field in a queryOrderField is missing in the service at position " + position);
                         // orderField.getDateFormat can be null (all non date field have this property set to null)

                     // responseFields can be null when all the nodes are extracted
                 });
    }

    public String executeQueries(File configurationFile) throws IOException, ServiceConfigurationError, InterruptedException {
        // Loads the configuration and checks if something important is missing
        List<Configuration> serviceList = readConfiguration(configurationFile);
        checkConfiguration(serviceList);
        ArrayList<ValueObject> resultList = new ArrayList<>(500);
        // for each service, load its data and simply adds them to a non-sorted array
        for (Configuration service : serviceList) {
            resultList.addAll(readDataFromService(service));
        }
        // It sorts the data according to the compareTo logic in each ValueObject
        Collections.sort(resultList);
        return createJSonResponse(resultList);
    }
}