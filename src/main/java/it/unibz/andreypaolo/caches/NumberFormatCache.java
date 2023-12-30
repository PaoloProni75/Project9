package it.unibz.andreypaolo.caches;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class NumberFormatCache {
    private static NumberFormatCache instance = null;

    private final Map<String, DecimalFormat> numberFormats = new HashMap<>();

    private NumberFormatCache() {
    }

    public static synchronized NumberFormatCache getInstance() {
        if (instance == null) {
            instance = new NumberFormatCache();
        }
        return instance;
    }

    public DecimalFormat getNumberFormat(String pattern) {
        return numberFormats.computeIfAbsent(pattern, DecimalFormat::new);
    }
}
