package it.unibz.andreypaolo;

public class QueryOrderField {
    private String path;

    private String dateFormat = null;

    public QueryOrderField() {
    }

    public boolean isDateType() {
        return dateFormat != null;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
