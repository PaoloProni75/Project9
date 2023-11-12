package it.unibz.andreypaolo.lowlevel;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class Utility {
    public final static ObjectMapper jsonMapper = new ObjectMapper();
    public static ObjectMapper getObjectMapper() {
        return jsonMapper;
    }
}
