package com.inc1440068.app.model;

public class WeatherInfo {
    private String temperature;
    private String description;
    private String humidity;
    private String windSpeed;
    private String city;

    public String getTemperature() {
        return temperature;
    }

    public WeatherInfo setTemperature(String temperature) {
        this.temperature = temperature;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WeatherInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getHumidity() {
        return humidity;
    }

    public WeatherInfo setHumidity(String humidity) {
        this.humidity = humidity;
        return this;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public WeatherInfo setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
        return this;
    }

    public String getCity() {
        return city;
    }

    public WeatherInfo setCity(String city) {
        this.city = city;
        return this;
    }
} 