package it.unibz.andreypaolo.conf;

import it.unibz.andreypaolo.conf.datatypes.DataTypes;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Configuration {
    private String url;
    private String provider;
    private String dataGroupPrefix;
    private ParseFormats parseFormats;
    private QueryOrderField queryOrderField;
    private List<Filter> filters = List.of();
    public Configuration() {
    }

    public String getQueryFieldPath() {
        return queryOrderField.getPath() ;
    }

    public DataTypes getQueryFieldType() {
        return queryOrderField.getType();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Optional<ParseFormats> getParseFormats() {
        return Optional.ofNullable(parseFormats);
    }

    public void setParseFormats(ParseFormats parseFormats) {
        this.parseFormats = parseFormats;
    }

    public Optional<String> getDataGroupPrefix() {
        return Optional.ofNullable(dataGroupPrefix);
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

    public List<Filter> getFilters() {
        if (filters.isEmpty())
            return List.of();

        return Collections.unmodifiableList(filters);
    }

    public void setFilters(List<Filter> filters) {
        this.filters = Objects.requireNonNullElseGet(filters, List::of);
    }
}
