package it.unibz.andreypaolo.highlevel;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.andreypaolo.lowlevel.DataProviderApi;

import java.text.SimpleDateFormat;

public class Mobility extends AbstractService {
    public final static String MOBILITY_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss.SSSZ";

    public Mobility(DataProviderApi dataProvider) {
        super("https://mobility.api.opendatahub.com/", dataProvider,
                new SimpleDateFormat(MOBILITY_DATE_FORMAT_STR));
    }

    @Override
    protected JsonNode getJSonNode(JsonNode rootNodes) {
        return rootNodes.get(DATA);
    }
}