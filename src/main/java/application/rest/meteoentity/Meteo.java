package application.rest.meteoentity;

import java.util.DoubleSummaryStatistics;
import java.util.List;

public abstract class Meteo {

    protected WeatherPK weatherPK;
    protected Float value;

    public Meteo(WeatherPK weatherPK, Float value) {
        this.weatherPK = weatherPK;
        this.value = value;
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

    public abstract void clear();

    public abstract DoubleSummaryStatistics getSummaryStatistics();

    @Override
    public String toString() {
        return "Meteo{" +
                "weatherPK=" + weatherPK +
                ", value=" + value +
                '}';
    }
}
