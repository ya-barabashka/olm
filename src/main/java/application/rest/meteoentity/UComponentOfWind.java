package application.rest.meteoentity;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class UComponentOfWind extends Meteo implements Comparable<UComponentOfWind>{

    public static Map<WeatherPK,Float> uComponentMap = new TreeMap<WeatherPK,Float>();

    public UComponentOfWind(WeatherPK weatherPK, Float value) {
        super(weatherPK, value);
        if(uComponentMap.isEmpty() || !uComponentMap.containsKey(weatherPK)){
            uComponentMap.put(weatherPK,value);
        }
        if(uComponentMap.containsKey(weatherPK)){
            uComponentMap.remove(weatherPK);
            uComponentMap.put(weatherPK,value);
        }
    }

    @Override
    public void clear() {
        uComponentMap.clear();
    }

    @Override
    public DoubleSummaryStatistics getSummaryStatistics() {
        return uComponentMap.values().stream()
                .mapToDouble((x) -> x)
                .summaryStatistics();
    }

//    public static Map<WeatherPK,Float> getUComponentForObservationTime(Integer observation){
//        Map<WeatherPK,Float>ucomp = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry ucompEntry : UComponentOfWind.getUComponentMap().entrySet()){
//            WeatherPK key = (WeatherPK)ucompEntry.getKey();
//            if(key.getObservation() == observation){
//                ucomp.put(key,(Float)ucompEntry.getValue());
//            }
//        }
//        return ucomp;
//    }
//
//    public static Map<WeatherPK,Float> getUComponentForLevel(Double level){
//        Map<WeatherPK,Float>ucomp = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry ucompEntry : UComponentOfWind.getUComponentMap().entrySet()){
//            WeatherPK key = (WeatherPK)ucompEntry.getKey();
//            if(key.getLevel().equals(level)){
//                ucomp.put(key,(Float)ucompEntry.getValue());
//            }
//        }
//        return ucomp;
//    }
//
//    public static Float getValueByKey(WeatherPK weatherPK){
//        for(Map.Entry uComponentEntry : UComponentOfWind.getUComponentMap().entrySet()){
//            WeatherPK key = (WeatherPK)uComponentEntry.getKey();
//            if(key.equals(weatherPK)){
//                return (Float)uComponentEntry.getValue();
//            }
//        }
//        return null;
//    }

    @Override
    public int compareTo(UComponentOfWind other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
