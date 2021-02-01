package application.rest.meteoentity;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class VComponentOfWind implements Comparable<VComponentOfWind> {

    private WeatherPK weatherPK;
    private Float vComponentOfWind;
    public static Map<WeatherPK,Float> vComponentMap = new TreeMap<WeatherPK,Float>();

    public VComponentOfWind() {
    }

    public VComponentOfWind(WeatherPK weatherPK, Float vComponentOfWind) {
        this.weatherPK = weatherPK;
        this.vComponentOfWind = vComponentOfWind;
        if(vComponentMap.isEmpty() || !vComponentMap.containsKey(weatherPK)){
            vComponentMap.put(weatherPK,vComponentOfWind);
        }
        if(vComponentMap.containsKey(weatherPK)){
            vComponentMap.remove(weatherPK);
            vComponentMap.put(weatherPK,vComponentOfWind);
        }
    }

    public WeatherPK getWeatherPK() {
        return weatherPK;
    }

    public void setWeatherPK(WeatherPK weatherPK) {
        this.weatherPK = weatherPK;
    }

    public Float getVComponentOfWind() {
        return vComponentOfWind;
    }

    public void setVComponentOfWind(Float vComponentOfWind) {
        this.vComponentOfWind = vComponentOfWind;
    }

    public static Map<WeatherPK, Float> getVComponentMap() {
        return vComponentMap;
    }

    public static Map<WeatherPK,Float> getVComponentForObservationTime(Integer observation){
        Map<WeatherPK,Float>vcomp = new TreeMap<WeatherPK,Float>();
        for(Map.Entry temperatureEntry : VComponentOfWind.getVComponentMap().entrySet()){
            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
            if(key.getObservation() == observation){
                vcomp.put(key,(Float)temperatureEntry.getValue());
            }
        }
        return vcomp;
    }

    public static Map<WeatherPK,Float> getVComponentForLevel(Double level){
        Map<WeatherPK,Float>vcomp = new TreeMap<WeatherPK,Float>();
        for(Map.Entry temperatureEntry : VComponentOfWind.getVComponentMap().entrySet()){
            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
            if(key.getLevel().equals(level)){
                vcomp.put(key,(Float)temperatureEntry.getValue());
            }
        }
        return vcomp;
    }

    public static Float getValueByKey(WeatherPK weatherPK){
        for(Map.Entry vComponentEntry : VComponentOfWind.getVComponentMap().entrySet()){
            WeatherPK key = (WeatherPK)vComponentEntry.getKey();
            if(key.equals(weatherPK)){
                return (Float)vComponentEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "windPK=" + weatherPK + ", "
                + "vComponentOfWind=" + vComponentOfWind;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.weatherPK);
        hash = 89 * hash + Objects.hashCode(this.vComponentOfWind);
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
        final VComponentOfWind other = (VComponentOfWind) obj;
        if (!Objects.equals(this.weatherPK, other.weatherPK)) {
            return false;
        }
        if (!Objects.equals(this.vComponentOfWind, other.vComponentOfWind)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(VComponentOfWind other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
