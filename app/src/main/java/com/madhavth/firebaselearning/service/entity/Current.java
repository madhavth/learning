package com.madhavth.firebaselearning.service.entity;


import com.squareup.moshi.Json;

import java.util.List;

public class Current {

    @Json(name = "observation_time")
    private String observationTime;
    @Json(name = "temperature")
    private Integer temperature;
    @Json(name = "weather_code")
    private Integer weatherCode;
    @Json(name = "weather_icons")
    private List<String> weatherIcons = null;
    @Json(name = "weather_descriptions")
    private List<String> weatherDescriptions = null;
    @Json(name = "wind_speed")
    private Integer windSpeed;
    @Json(name = "wind_degree")
    private Integer windDegree;
    @Json(name = "wind_dir")
    private String windDir;
    @Json(name = "pressure")
    private Integer pressure;
    @Json(name = "precip")
    private Integer precip;
    @Json(name = "humidity")
    private Integer humidity;
    @Json(name = "cloudcover")
    private Integer cloudcover;
    @Json(name = "feelslike")
    private Integer feelslike;
    @Json(name = "uv_index")
    private Integer uvIndex;
    @Json(name = "visibility")
    private Integer visibility;
    @Json(name = "is_day")
    private String isDay;

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(Integer weatherCode) {
        this.weatherCode = weatherCode;
    }

    public List<String> getWeatherIcons() {
        return weatherIcons;
    }

    public void setWeatherIcons(List<String> weatherIcons) {
        this.weatherIcons = weatherIcons;
    }

    public List<String> getWeatherDescriptions() {
        return weatherDescriptions;
    }

    public void setWeatherDescriptions(List<String> weatherDescriptions) {
        this.weatherDescriptions = weatherDescriptions;
    }

    public Integer getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(Integer windDegree) {
        this.windDegree = windDegree;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getPrecip() {
        return precip;
    }

    public void setPrecip(Integer precip) {
        this.precip = precip;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getCloudcover() {
        return cloudcover;
    }

    public void setCloudcover(Integer cloudcover) {
        this.cloudcover = cloudcover;
    }

    public Integer getFeelslike() {
        return feelslike;
    }

    public void setFeelslike(Integer feelslike) {
        this.feelslike = feelslike;
    }

    public Integer getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(Integer uvIndex) {
        this.uvIndex = uvIndex;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public String getIsDay() {
        return isDay;
    }

    public void setIsDay(String isDay) {
        this.isDay = isDay;
    }
}


