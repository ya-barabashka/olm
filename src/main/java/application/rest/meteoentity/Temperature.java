package application.rest.meteoentity;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Temperature implements Comparable<Temperature> {

    private WeatherPK weatherPK;
    private Float temperature;
    public static Map<WeatherPK,Float> temperatureMap = new TreeMap<WeatherPK,Float>();

    public Temperature() {
    }

    public Temperature(WeatherPK weatherPK, Float temperature) {
        this.weatherPK = weatherPK;
        this.temperature = temperature;
        if(temperatureMap.isEmpty() || !temperatureMap.containsKey(weatherPK)){
            temperatureMap.put(weatherPK,temperature);
        }
        if(temperatureMap.containsKey(weatherPK)){
            temperatureMap.remove(weatherPK);
            temperatureMap.put(weatherPK,temperature);
        }
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

    public static Map<WeatherPK, Float> getTemperatureMap() {
        return temperatureMap;
    }

    public static Map<WeatherPK,Float> getTemperatureForObservationTime(Integer observation){
        Map<WeatherPK,Float>temperature = new TreeMap<WeatherPK,Float>();
        for(Map.Entry temperatureEntry : Temperature.getTemperatureMap().entrySet()){
            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
            if(key.getObservation() == observation){
                temperature.put(key,(Float)temperatureEntry.getValue());
            }
        }
        return temperature;
    }

    public static Map<WeatherPK,Float> getTemperatureForLevel(Double level){
        Map<WeatherPK,Float>temperature = new TreeMap<WeatherPK,Float>();
        for(Map.Entry temperatureEntry : Temperature.getTemperatureMap().entrySet()){
            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
            if(key.getLevel().equals(level)){
                temperature.put(key,(Float)temperatureEntry.getValue());
            }
        }
        return temperature;
    }

    public static Float getValueByKey(WeatherPK weatherPK){
        for(Map.Entry temperatureEntry : Temperature.getTemperatureMap().entrySet()){
            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
            if(key.equals(weatherPK)){
                return (Float)temperatureEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "windPK=" + weatherPK + ", "
                + "temperature=" + temperature;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.weatherPK);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Temperature other = (Temperature) obj;
        if (!Objects.equals(this.weatherPK, other.weatherPK)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Temperature other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
