package it.unibz.andreypaolo.caches;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TimeFormatCache {
    private static TimeFormatCache instance = null;

    private final Map<String, DateTimeFormatter> timeFormats = new HashMap<>();

    private TimeFormatCache() {
    }

    public static synchronized TimeFormatCache getInstance() {
        if (instance == null)
            instance = new TimeFormatCache();

        return instance;
    }

    public DateTimeFormatter getTimeFormat(String pattern) {
        // If timeFormat does not contain pattern, a new parser is created
        // for the pattern and added to the map, finally the parser is returned
        return timeFormats.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
     }
}