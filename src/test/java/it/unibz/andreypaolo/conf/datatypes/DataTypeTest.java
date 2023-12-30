package it.unibz.andreypaolo.conf.datatypes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataTypeTest {

    @Test
    void presentValues() {
        assertSame(DataTypes.DATE, DataTypes.forValue("date"));
        assertSame(DataTypes.STRING, DataTypes.forValue("string"));
    }

    @Test
    void missingValue() {
        assertThrows(IllegalArgumentException.class,
                    () -> DataTypes.forValue("DUMMY"));
    }
}
