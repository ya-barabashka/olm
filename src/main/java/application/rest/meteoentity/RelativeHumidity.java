package application.rest.meteoentity;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class RelativeHumidity implements Comparable<RelativeHumidity> {

    private WeatherPK weatherPK;
    private Float humidity;
    public static Map<WeatherPK,Float> humidityMap = new TreeMap<WeatherPK,Float>();

    public RelativeHumidity() {
    }

    public RelativeHumidity(WeatherPK weatherPK, Float humidity) {
        this.weatherPK = weatherPK;
        this.humidity = humidity;
        if(humidityMap.isEmpty() || !humidityMap.containsKey(weatherPK)){
            humidityMap.put(weatherPK,humidity);
        }
        if(humidityMap.containsKey(weatherPK)){
            humidityMap.remove(weatherPK);
            humidityMap.put(weatherPK,humidity);
        }
    }

    public WeatherPK getWeatherPK() {
        return weatherPK;
    }

    public void setWeatherPK(WeatherPK weatherPK) {
        this.weatherPK = weatherPK;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public static Map<WeatherPK, Float> getHumidityMap() {
        return humidityMap;
    }

    public static Map<WeatherPK,Float> getHumidityForObservationTime(Integer observation){
        Map<WeatherPK,Float>humidity = new TreeMap<WeatherPK,Float>();
        for(Map.Entry humidityEntry : RelativeHumidity.getHumidityMap().entrySet()){
            WeatherPK key = (WeatherPK)humidityEntry.getKey();
            if(key.getObservation() == observation){
                humidity.put(key,(Float)humidityEntry.getValue());
            }
        }
        return humidity;
    }

    public static Map<WeatherPK,Float> getHumidityForLevel(Double level){
        Map<WeatherPK,Float>humidity = new TreeMap<WeatherPK,Float>();
        for(Map.Entry humidityEntry : RelativeHumidity.getHumidityMap().entrySet()){
            WeatherPK key = (WeatherPK)humidityEntry.getKey();
            if(key.getLevel().equals(level)){
                humidity.put(key,(Float)humidityEntry.getValue());
            }
        }
        return humidity;
    }

    public static Float getValueByKey(WeatherPK weatherPK){
        for(Map.Entry humidityEntry : RelativeHumidity.getHumidityMap().entrySet()){
            WeatherPK key = (WeatherPK)humidityEntry.getKey();
            if(key.equals(weatherPK)){
                return (Float)humidityEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.weatherPK);
        hash = 37 * hash + Objects.hashCode(this.humidity);
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
        final RelativeHumidity other = (RelativeHumidity) obj;
        if (!Objects.equals(this.weatherPK, other.weatherPK)) {
            return false;
        }
        if (!Objects.equals(this.humidity, other.humidity)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "windPK=" + weatherPK + ", "
                + "humidity=" + humidity;
    }

    @Override
    public int compareTo(RelativeHumidity other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
