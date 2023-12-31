package it.unibz.andreypaolo.conf.datatypes;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractObjectComparator<T extends Comparable<T>> implements DataComparator, Comparator<T> {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    protected Level logLevel = Level.WARNING;
    @Override
    public int compare(T firstObject, T secondObject) {
        if (firstObject == secondObject)
            return 0;

        final boolean isFirstObjectNull = firstObject == null;
        final boolean isSecondObjectNull = secondObject == null;

        if (isFirstObjectNull)
            return 1;
        if (isSecondObjectNull)
            return -1;

        return firstObject.compareTo(secondObject);
    }

    void hideLogs() {
        logLevel = Level.ALL;
    }
}
