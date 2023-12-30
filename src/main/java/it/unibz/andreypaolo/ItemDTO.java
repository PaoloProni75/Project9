package it.unibz.andreypaolo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.andreypaolo.conf.ParseFormats;
import it.unibz.andreypaolo.conf.datatypes.DataTypes;

import java.util.Objects;

public class ItemDTO implements Comparable<ItemDTO> {
    private final String queryFieldPath;
    private final DataTypes queryFieldType;
    private final String provider;
    private final String dateFormat;
    private final String timeFormat;
    private final String decimalFormat;
    private final JsonNode originalItem;

    public ItemDTO(Builder builder) {
        this.queryFieldPath = builder.queryFieldPath;
        this.queryFieldType = builder.queryFieldType == null ? DataTypes.STRING : builder.queryFieldType;
        this.provider = builder.provider;
        ParseFormats parseFormats = builder.parseFormats == null ? new ParseFormats() : builder.parseFormats;
        this.dateFormat = parseFormats.getDate().orElse("");
        this.timeFormat = parseFormats.getTime().orElse("");
        this.decimalFormat = parseFormats.getDecimal().orElse("");
        this.originalItem = builder.originalItem;
    }

    public JsonNode getOriginalItem() {
        return originalItem;
    }

    public String getOrderFieldText() {
        JsonNode actualValues = originalItem.at(queryFieldPath);
        return actualValues.isMissingNode() ? "" : actualValues.asText();
    }

    public String createResultItem() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("provider", provider);
        objectNode.set("originalItem", originalItem);
        return objectNode.toPrettyString();
    }

    DataTypes getQueryFieldType() {
        return queryFieldType;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public String getDecimalFormat() {
        return decimalFormat;
    }

    @Override
    public int compareTo(ItemDTO other) {
        if (other != null) { // null values at the end
            if (other.getQueryFieldType() != queryFieldType)
                throw new IllegalArgumentException("Cannot compare different data types");
            // comparison is made on an internal field which has been defined in the configuration as queryOrderField
            return queryFieldType.compareItems(this, other);
        }

        return 1; // if other == null, it goes at the end
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ItemDTO that = (ItemDTO) other;
        return Objects.equals(getOrderFieldText(), that.getOrderFieldText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderFieldText());
    }

    public static class Builder {
        private String queryFieldPath;
        private DataTypes queryFieldType;
        private String provider;
        private ParseFormats parseFormats;
        private JsonNode originalItem;

        public Builder setQueryFieldPath(String queryFieldPath) {
            this.queryFieldPath = queryFieldPath;
            return this;
        }

        public Builder setQueryFieldType(DataTypes queryFieldType) {
            this.queryFieldType = queryFieldType;
            return this;
        }

        public Builder setProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder setParseFormats(ParseFormats parseFormats) {
            this.parseFormats = parseFormats;
            return this;
        }

        public Builder setOriginalItem(JsonNode originalItem) {
            this.originalItem = originalItem;
            return this;
        }

        public ItemDTO build() {
            return new ItemDTO(this);
        }
    }
}