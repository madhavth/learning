package com.madhavth.firebaselearning.service;

import java.util.List;

import com.madhavth.firebaselearning.service.entity.Current;
import com.madhavth.firebaselearning.service.entity.Location;
import com.madhavth.firebaselearning.service.entity.Request;
import com.squareup.moshi.Json;

public class Weather2 {

    @Json(name = "request")
    private Request request;
    @Json(name = "location")
    private Location location;
    @Json(name = "current")
    private Current current;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

}