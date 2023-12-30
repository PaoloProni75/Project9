package it.unibz.andreypaolo.conf;

import it.unibz.andreypaolo.conf.datatypes.DataTypes;

public class QueryOrderField {
    private String path;

    private DataTypes type = DataTypes.STRING;

    public QueryOrderField() {
    }

    public QueryOrderField(String path, DataTypes type) {
        this.path = path;
        this.type = type;
    }

    // We need setters because of the Jackson library

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DataTypes getType() {
        return type;
    }

    public void setType(DataTypes type) {
        this.type = type;
    }
}
