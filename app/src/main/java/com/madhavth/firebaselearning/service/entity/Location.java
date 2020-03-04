package com.madhavth.firebaselearning.service.entity;


import com.squareup.moshi.Json;

public class Location {

    @Json(name = "name")
    private String name;
    @Json(name = "country")
    private String country;
    @Json(name = "region")
    private String region;
    @Json(name = "lat")
    private String lat;
    @Json(name = "lon")
    private String lon;
    @Json(name = "timezone_id")
    private String timezoneId;
    @Json(name = "localtime")
    private String localtime;
    @Json(name = "localtime_epoch")
    private Integer localtimeEpoch;
    @Json(name = "utc_offset")
    private String utcOffset;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTimezoneId() {
        return timezoneId;
    }

    public void setTimezoneId(String timezoneId) {
        this.timezoneId = timezoneId;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public Integer getLocaltimeEpoch() {
        return localtimeEpoch;
    }

    public void setLocaltimeEpoch(Integer localtimeEpoch) {
        this.localtimeEpoch = localtimeEpoch;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }
}