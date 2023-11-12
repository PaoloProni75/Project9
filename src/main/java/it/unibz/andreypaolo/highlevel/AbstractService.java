package it.unibz.andreypaolo.highlevel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.unibz.andreypaolo.lowlevel.DataProviderApi;

import java.io.IOException;
import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;

abstract class AbstractService implements ServiceApi {
    private final String domain;

    private final DataProviderApi dataProvider;

    private final DateFormat dateFormat;

    public AbstractService(String domain, DataProviderApi dataProvider, DateFormat dateFormat) {
        this.domain = domain;
        this.dataProvider = dataProvider;
        this.dateFormat = dateFormat;
    }

    @Override
    public List<ValueObject> readDataFromService(String servicePart, String orderByFieldName) throws IOException, InterruptedException {
        LinkedList<ValueObject> resultList = new LinkedList<>();
        String completeUrlString = domain + servicePart;
        JsonNode rootNodes = dataProvider.readDataFromService(completeUrlString);
        JsonNode nodeLists = getJSonNode(rootNodes);
        if (nodeLists != null && !nodeLists.isNull()) {
            if (nodeLists.isArray()) {
                ArrayNode arrayNodes = (ArrayNode)nodeLists;
                for (JsonNode node : arrayNodes)
                    resultList.add(new ValueObject(orderByFieldName, node, dateFormat));
            }
            else
                resultList.add(new ValueObject(orderByFieldName, nodeLists, dateFormat));
        }

        return resultList;
    }

    protected abstract JsonNode getJSonNode(JsonNode rootNodes);
}