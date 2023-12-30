package it.unibz.andreypaolo.conf.datatypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.unibz.andreypaolo.ItemDTO;

public enum DataTypes {
    DATE(new DateComparator()),
    TIME(new TimeComparator()),
    DECIMAL(new DecimalComparator()),
    STRING(new StringComparator());

    private final DataComparator itemComparator;
    private DataTypes(DataComparator itemComparator) {
        this.itemComparator = itemComparator;
    }

    @JsonCreator
    public static DataTypes forValue(String value) {
        return DataTypes.valueOf(value.toUpperCase());
    }

    public int compareItems(ItemDTO item0, ItemDTO item1) {
        return itemComparator.compareItems(item0, item1);
    }

    public DataComparator getComparator() {
        return this.itemComparator;
    }
}
