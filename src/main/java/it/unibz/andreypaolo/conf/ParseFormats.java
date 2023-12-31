package it.unibz.andreypaolo.conf;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Optional;

public class ParseFormats {
    private String date;
    private String time;
    private String decimal = "#.#";

    public ParseFormats() {
    }

    public Optional<String> getDate() {
        return Optional.ofNullable(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Optional<String> getTime() {
        return Optional.ofNullable(time);
    }

    @JsonSetter
    public void setTime(String time) {
        this.time = time != null ? time : "HH:mm:ss";
    }

    public Optional<String> getDecimal() {
        return Optional.ofNullable(decimal);
    }
    @JsonSetter
    public void setDecimal(String decimal) {
        this.decimal = decimal != null ? decimal : "#.#";
    }
}
