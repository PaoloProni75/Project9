package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class Utility {
    public final static ObjectMapper jsonMapper = new ObjectMapper();
    public static ObjectMapper getObjectMapper() {
        return jsonMapper;
    }
}
