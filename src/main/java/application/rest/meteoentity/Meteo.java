package application.rest.meteoentity;

import application.rest.geoentity.Region;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public abstract class Meteo {

    protected WeatherPK weatherPK;
    protected Float value;
    protected Region region;

    public Meteo(WeatherPK weatherPK, Float value, Region region) {
        this.weatherPK = weatherPK;
        this.value = value;
        this.region = region;
    }

    public WeatherPK getWeatherPK() {
        return weatherPK;
    }

    public void setWeatherPK(WeatherPK weatherPK) {
        this.weatherPK = weatherPK;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public abstract void clear();

    public abstract DoubleSummaryStatistics getSummaryStatistics();

    public abstract String toJson() throws JsonProcessingException;

    @Override
    public String toString() {
        return "Meteo{" +
                "weatherPK=" + weatherPK +
                ", value=" + value +
                ", region=" + region +
                '}';
    }
}
