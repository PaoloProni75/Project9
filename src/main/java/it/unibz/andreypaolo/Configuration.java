package it.unibz.andreypaolo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Configuration {
    private String url;
    private String dataGroupPrefix;
    private QueryOrderField queryOrderField;
    private List<String> responseFields;

    public Configuration() {
    }

    public String getQueryFieldPath() {
        assert queryOrderField != null;
        return queryOrderField.getPath() ;
    }

    public DateFormat getDateFormat() {
        assert queryOrderField != null;
        final String dateFormatStr = queryOrderField.getDateFormat();
        return (dateFormatStr != null) ? new SimpleDateFormat(dateFormatStr) : null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataGroupPrefix() {
        return dataGroupPrefix;
    }

    public void setDataGroupPrefix(String dataGroupPrefix) {
        this.dataGroupPrefix = dataGroupPrefix;
    }

    public QueryOrderField getQueryOrderField() {
        return queryOrderField;
    }

    public void setQueryOrderField(QueryOrderField queryOrderField) {
        this.queryOrderField = queryOrderField;
    }

    public List<String> getResponseFields() {
        return responseFields;
    }

    public void setResponseFields(List<String> responseFields) {
        this.responseFields = responseFields;
    }
}
