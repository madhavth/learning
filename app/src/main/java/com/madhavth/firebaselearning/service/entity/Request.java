package com.madhavth.firebaselearning.service.entity;

import com.squareup.moshi.Json;

public class Request {

    @Json(name = "type")
    private String type;
    @Json(name = "query")
    private String query;
    @Json(name = "language")
    private String language;
    @Json(name = "unit")
    private String unit;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
