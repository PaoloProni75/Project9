package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import it.unibz.andreypaolo.conf.Configuration;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.QueryOrderField;
import it.unibz.andreypaolo.conf.datatypes.DataTypes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainComponent {

    private final DataProviderApi dataProvider;

    public MainComponent(DataProviderApi dataProvider) {
        // if dataProvider is null, it takes the OpenDataHub by default
        this.dataProvider = Objects.requireNonNullElseGet(dataProvider, OpenDataHub::new);
    }

    List<Configuration> readConfiguration(File configurationFile) throws IOException {
        ObjectMapper mapper = MapperSingleton.getInstance().getMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Configuration.class);
        return mapper.readValue(configurationFile, listType);
    }

    private boolean isJsonDataValid(JsonNode jsonData) {
        return jsonData != null && !jsonData.isNull() && !jsonData.isMissingNode() && !jsonData.isEmpty();
    }

    List<ItemDTO> readDataFromService(Configuration serviceConf) throws IOException, InterruptedException {
        JsonNode jsonData = dataProvider.readDataFromService(serviceConf.getUrl());
        if (!isJsonDataValid(jsonData))
            return List.of();

        JsonNode dataNode = serviceConf.getDataGroupPrefix()
                .map(jsonData::get)
                .orElse(jsonData);

        return filter(dataNode, serviceConf);
    }

    List<ItemDTO> filter(JsonNode dataNode, Configuration serviceConf) {
        List<ItemDTO> resultList = new ArrayList<>();
        if (dataNode != null && !dataNode.isNull()) {
            if (dataNode.isArray()) {
                ArrayNode arrayNodes = (ArrayNode) dataNode;
                arrayNodes.forEach(node -> addValueObjectIfAcceptedByFilters(node, resultList, serviceConf));
            } else {
                addValueObjectIfAcceptedByFilters(dataNode, resultList, serviceConf);
            }
        }
        return resultList;
    }

    private void addValueObjectIfAcceptedByFilters(JsonNode node, List<ItemDTO> resultList,
                                                   Configuration serviceConf) {
        ParseFormats pf = serviceConf.getParseFormats().orElse(new ParseFormats());
        if (serviceConf.getFilters().stream().allMatch(filter -> filter.accept(node, pf))) {
            ItemDTO item = new ItemDTO.Builder()
                    .setQueryFieldPath(serviceConf.getQueryFieldPath())
                    .setQueryFieldType(serviceConf.getQueryFieldType())
                    .setProvider(serviceConf.getProvider())
                    .setParseFormats(pf)
                    .setOriginalItem(node)
                    .build();
            resultList.add(item);
        }
    }

    String createJSonResponse(List<ItemDTO> valueObjectList) {
        assert valueObjectList != null;
        return valueObjectList.stream()
                 .map(ItemDTO::createResultItem)
                 .collect(Collectors.joining(", " ,"[ "," ]"));
    }

    void checkConfiguration(final List<Configuration> serviceList) {
        Objects.requireNonNull(serviceList, "serviceList parameter in checkConfiguration must not be null");
        int position = 1;
        for (Configuration conf : serviceList) {
            validateBasicConfiguration(conf, position);
            validateDataTypeConfiguration(conf, position);
            position++;
        }
    }

    private void validateBasicConfiguration(Configuration conf, int position) {
        Objects.requireNonNull(conf.getUrl(), "The url field is missing in the service at position: " + position);
        Objects.requireNonNull(conf.getProvider(), "The provider name is missing in the service at position: " + position);
        QueryOrderField orderField = conf.getQueryOrderField();
        Objects.requireNonNull(orderField,"The queryOrderField is missing in the service at position " + position);
        Objects.requireNonNull(orderField.getPath(), "The path field in a queryOrderField is missing in the service at position " + position);
    }

    private void validateDataTypeConfiguration(Configuration conf, int position) {
        QueryOrderField orderField = conf.getQueryOrderField();
        ParseFormats pf = conf.getParseFormats().orElse(new ParseFormats());
        String dateFormat = pf.getDate().orElse("");
        String timeFormat = pf.getTime().orElse("");
        String decimalFormat = pf.getDecimal().orElse("");

        validateDateField(orderField, dateFormat, position);
        // The parse defaults make the following code useless
        /*
        validateTimeField(orderField, timeFormat, position);
        validateDecimalField(orderField, decimalFormat, position);

        List<Filter> filters = conf.getFilters();
        if (filters != null)
            filters.forEach(filter -> {
                switch (filter.getType()) {
                    case DATE:
                        if (dateFormat.isEmpty())
                            throw new IllegalArgumentException("The filter is a date but no date format has been specified at position " + position);
                        break;
                    case TIME:
                        if (timeFormat.isEmpty())
                            throw new IllegalArgumentException("The filter is a time but no date format has been specified at position " + position);
                        break;
                    case DECIMAL:
                        if (decimalFormat.isEmpty())
                            throw new IllegalArgumentException("The filter is a decimal but no decimal format has been specified at position " + position);
                        break;
                }
            });
         */
    }

    private void validateTimeField(QueryOrderField orderField, String timeFormat, int position) {
        if (orderField.getType() == DataTypes.TIME && timeFormat.isEmpty())
            throw new IllegalArgumentException("The queryOrderField is a time but no time format has been specified at position " + position);
    }

    private void validateDecimalField(QueryOrderField orderField, String decimalFormat, int position) {
        if (orderField.getType() == DataTypes.DECIMAL && decimalFormat.isEmpty())
            throw new IllegalArgumentException("The queryOrderField is a decimal but no decimal format has been specified at position " + position);
    }

    private void validateDateField(QueryOrderField orderField, String dateFormat, int position) {
        if (orderField.getType() == DataTypes.DATE && dateFormat.isEmpty())
            throw new IllegalArgumentException("The queryOrderField is a date but no date format has been specified at position " + position);
    }

    public String executeQueries(File configurationFile) throws IOException, InterruptedException {
        // Loads the configuration and checks if something mandatory is missing
        List<Configuration> serviceList = readConfiguration(configurationFile);
        checkConfiguration(serviceList);
        ArrayList<ItemDTO> resultList = new ArrayList<>(500);
        // for each service, load its data and simply adds them to a non-sorted array
        for (Configuration service : serviceList) {
            resultList.addAll(readDataFromService(service));
        }
        // It sorts the data according to the compareTo logic in each ValueObject
        Collections.sort(resultList);
        return createJSonResponse(resultList);
    }
}