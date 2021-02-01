package application.rest.meteoentity;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class WeatherPK implements Comparable<WeatherPK> {

    private Double latitude;
    private Double longitude;
    private Double level;
    private Integer observation;
    private Integer forecast;

    private static Set<WeatherPK> keys = new TreeSet<WeatherPK>();
    private static Set<Double> levels = new TreeSet<Double>();
    private static Set<Integer> observations = new TreeSet<Integer>();
    private static Set<Integer> forecasts = new TreeSet<Integer>();

    public WeatherPK() {
    }

    public WeatherPK(Double latitude, Double longitude, Double level, Integer observation, Integer forecast) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.level = level;
        this.observation = observation;
        this.forecast = forecast;
        levels.add(level);
        observations.add(observation);
        forecasts.add(forecast);
        keys.add(this);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public Integer getObservation() {
        return observation;
    }

    public void setObservation(Integer observation) {
        this.observation = observation;
    }

    public static Set<WeatherPK> getKeys() {
        return keys;
    }

    public static void setKeys(Set<WeatherPK> keys) {
        WeatherPK.keys = keys;
    }

    public static Set<Double> getLevels() {
        return levels;
    }

    public static void setLevels(Set<Double> levels) {
        WeatherPK.levels = levels;
    }

    public Integer getForecast() {
        return forecast;
    }

    public void setForecast(Integer forecast) {
        this.forecast = forecast;
    }

    public static Set<Integer> getObservations() {
        return observations;
    }

    public static void setObservations(Set<Integer> observations) {
        WeatherPK.observations = observations;
    }

    public static Set<Integer> getForecasts() {
        return forecasts;
    }

    public static void setForecasts(Set<Integer> forecasts) {
        WeatherPK.forecasts = forecasts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherPK)) return false;
        WeatherPK weatherPK = (WeatherPK) o;
        return Objects.equals(getLatitude(), weatherPK.getLatitude()) &&
                Objects.equals(getLongitude(), weatherPK.getLongitude()) &&
                Objects.equals(getLevel(), weatherPK.getLevel()) &&
                Objects.equals(getObservation(), weatherPK.getObservation()) &&
                Objects.equals(getForecast(), weatherPK.getForecast());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude(), getLevel(), getObservation(), getForecast());
    }

    @Override
    public int compareTo(WeatherPK other) {

        int result = level.compareTo(other.level);
        if(result!=0){
            return result;
        }

        result = latitude.compareTo(other.latitude);
        if(result!=0){
            return result;
        }

        result = longitude.compareTo(other.longitude);
        if(result!=0){
            return result;
        }

        result = observation.compareTo(other.observation);
        if(result!=0){
            return result;
        }

        result = forecast.compareTo(other.forecast);
        if(result!=0){
            return result;
        }

        return 0;
    }

    @Override
    public String toString() {
        return "WeatherPK{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", level=" + level +
                ", observation=" + observation +
                ", forecast=" + forecast +
                '}';
    }
}


