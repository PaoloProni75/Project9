package it.unibz.andreypaolo.caches;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class SimpleDateFormatCache {
    private static SimpleDateFormatCache instance = null;

    private final Map<String, SimpleDateFormat> dateFormats = new HashMap<>();

    private SimpleDateFormatCache() {
    }

    public static synchronized SimpleDateFormatCache getInstance() {
        if (instance == null)
            instance = new SimpleDateFormatCache();

        return instance;
    }

    public SimpleDateFormat getDateFormat(String pattern) {
        return dateFormats.computeIfAbsent(pattern, SimpleDateFormat::new);
    }
}
