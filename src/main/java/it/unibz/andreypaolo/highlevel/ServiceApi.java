package it.unibz.andreypaolo.highlevel;

import java.io.IOException;
import java.util.List;

public interface ServiceApi {
    String DATA = "data";
    String EVENT_DATE = "evend";
    String FROM = "From";

    String ITEMS_NODE_NAME = "Items";

    List<ValueObject> readDataFromService(String servicePart, String orderByFieldName) throws IOException, InterruptedException;
}
