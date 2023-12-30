package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class MapperSingleton {
    private final ObjectMapper yamlMapper;
    private static MapperSingleton instance = null;
    private MapperSingleton() {
        yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.registerModule(new Jdk8Module());
        /*
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.findAndRegisterModules(); // to handle Date fields
        */
    }

    public static synchronized MapperSingleton getInstance() {
        if (instance == null)
            instance = new MapperSingleton();

        return instance;
    }

    public ObjectMapper getMapper() {
        return yamlMapper;
    }
}