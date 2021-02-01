package application.rest.meteoentity;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class UComponentOfWind implements Comparable<UComponentOfWind>{

    private WeatherPK weatherPK;
    private Float uComponentOfWind;
    public static Map<WeatherPK,Float> uComponentMap = new TreeMap<WeatherPK,Float>();

    public UComponentOfWind() {
    }

    public UComponentOfWind(WeatherPK weatherPK, Float uComponentOfWind) {
        this.weatherPK = weatherPK;
        this.uComponentOfWind = uComponentOfWind;
        if(uComponentMap.isEmpty() || !uComponentMap.containsKey(weatherPK)){
            uComponentMap.put(weatherPK,uComponentOfWind);
        }
        if(uComponentMap.containsKey(weatherPK)){
            uComponentMap.remove(weatherPK);
            uComponentMap.put(weatherPK,uComponentOfWind);
        }
    }

    public WeatherPK getWeatherPK() {
        return weatherPK;
    }

    public void setWeatherPK(WeatherPK weatherPK) {
        this.weatherPK = weatherPK;
    }

    public Float getUComponentOfWind() {
        return uComponentOfWind;
    }

    public void setUComponentOfWind(Float uComponentOfWind) {
        this.uComponentOfWind = uComponentOfWind;
    }

    public static Map<WeatherPK, Float> getUComponentMap() {
        return uComponentMap;
    }

    public static Map<WeatherPK,Float> getUComponentForObservationTime(Integer observation){
        Map<WeatherPK,Float>ucomp = new TreeMap<WeatherPK,Float>();
        for(Map.Entry ucompEntry : UComponentOfWind.getUComponentMap().entrySet()){
            WeatherPK key = (WeatherPK)ucompEntry.getKey();
            if(key.getObservation() == observation){
                ucomp.put(key,(Float)ucompEntry.getValue());
            }
        }
        return ucomp;
    }

    public static Map<WeatherPK,Float> getUComponentForLevel(Double level){
        Map<WeatherPK,Float>ucomp = new TreeMap<WeatherPK,Float>();
        for(Map.Entry ucompEntry : UComponentOfWind.getUComponentMap().entrySet()){
            WeatherPK key = (WeatherPK)ucompEntry.getKey();
            if(key.getLevel().equals(level)){
                ucomp.put(key,(Float)ucompEntry.getValue());
            }
        }
        return ucomp;
    }

    public static Float getValueByKey(WeatherPK weatherPK){
        for(Map.Entry uComponentEntry : UComponentOfWind.getUComponentMap().entrySet()){
            WeatherPK key = (WeatherPK)uComponentEntry.getKey();
            if(key.equals(weatherPK)){
                return (Float)uComponentEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.weatherPK);
        hash = 37 * hash + Objects.hashCode(this.uComponentOfWind);
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
        final UComponentOfWind other = (UComponentOfWind) obj;
        if (!Objects.equals(this.weatherPK, other.weatherPK)) {
            return false;
        }
        if (!Objects.equals(this.uComponentOfWind, other.uComponentOfWind)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "windPK=" + weatherPK + ", "
                + "uComponentOfWind=" + uComponentOfWind;
    }

    @Override
    public int compareTo(UComponentOfWind other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
