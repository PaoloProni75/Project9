package it.unibz.andreypaolo.highlevel;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.andreypaolo.lowlevel.DataProviderApi;

import java.text.SimpleDateFormat;

public class Tourism extends AbstractService {
    public final static String TOURISM_DATE_FORMAT_STR = "yyyy-MM-dd'T'HH:mm:ss";

    public Tourism(DataProviderApi dataProvider) {
        super("https://tourism.opendatahub.com/", dataProvider, new SimpleDateFormat(TOURISM_DATE_FORMAT_STR));
    }

    @Override
    protected JsonNode getJSonNode(JsonNode rootNodes) {
        return rootNodes.get(ITEMS_NODE_NAME);
    }
}
