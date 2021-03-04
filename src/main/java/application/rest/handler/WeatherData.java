package application.rest.handler;

import application.rest.meteoentity.WeatherPK;

public class WeatherData {

    private WeatherPK weatherPK;
    private Float temperature;
    private Float uComponentOfWind;
    private Float vComponentOfWind;
    private Float humidity;
    private Float cloudness;

    public WeatherData() {
    }

    public WeatherData(WeatherPK weatherPK, Float temperature, Float uComponentOfWind, Float vComponentOfWind, Float humidity, Float cloudness) {
        this.weatherPK = weatherPK;
        this.temperature = temperature;
        this.uComponentOfWind = uComponentOfWind;
        this.vComponentOfWind = vComponentOfWind;
        this.humidity = humidity;
        this.cloudness = cloudness;
    }

    public WeatherData(WeatherPK weatherPK, Float temperature, Float uComponentOfWind, Float vComponentOfWind, Float humidity/*, Float cloudness*/) {
        this.weatherPK = weatherPK;
        this.temperature = temperature;
        this.uComponentOfWind = uComponentOfWind;
        this.vComponentOfWind = vComponentOfWind;
        this.humidity = humidity;
    }

    public WeatherData(WeatherPK weatherPK, Float cloudness) {
        this.cloudness = cloudness;
    }

    public WeatherPK getWeatherPK() {
        return weatherPK;
    }

    public void setWeatherPK(WeatherPK weatherPK) {
        this.weatherPK = weatherPK;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getUComponentOfWind() {
        return uComponentOfWind;
    }

    public void setUComponentOfWind(Float uComponentOfWind) {
        this.uComponentOfWind = uComponentOfWind;
    }

    public Float getVComponentOfWind() {
        return vComponentOfWind;
    }

    public void setVComponentOfWind(Float vComponentOfWind) {
        this.vComponentOfWind = vComponentOfWind;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getCloudness() {
        return cloudness;
    }

    public void setCloudness(Float cloudness) {
        this.cloudness = cloudness;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "weatherPK=" + weatherPK +
                ", temperature=" + temperature +
                ", uComponentOfWind=" + uComponentOfWind +
                ", vComponentOfWind=" + vComponentOfWind +
                ", humidity=" + humidity +
                ", cloudness=" + cloudness +
                '}';
    }
}

