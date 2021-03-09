package application.rest.meteoentity;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class VComponentOfWind extends Meteo implements Comparable<VComponentOfWind> {

    public static Map<WeatherPK,Float> vComponentMap = new TreeMap<WeatherPK,Float>();

    public VComponentOfWind(WeatherPK weatherPK, Float value) {
        super(weatherPK, value);
        if(vComponentMap.isEmpty() || !vComponentMap.containsKey(weatherPK)){
            vComponentMap.put(weatherPK,value);
        }
        if(vComponentMap.containsKey(weatherPK)){
            vComponentMap.remove(weatherPK);
            vComponentMap.put(weatherPK,value);
        }
    }

    @Override
    public void clear() {
        vComponentMap.clear();
    }

    @Override
    public DoubleSummaryStatistics getSummaryStatistics() {
        return vComponentMap.values().stream()
                .mapToDouble((x) -> x)
                .summaryStatistics();
    }

//    public static Map<WeatherPK,Float> getVComponentForObservationTime(Integer observation){
//        Map<WeatherPK,Float>vcomp = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry temperatureEntry : VComponentOfWind.getVComponentMap().entrySet()){
//            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
//            if(key.getObservation() == observation){
//                vcomp.put(key,(Float)temperatureEntry.getValue());
//            }
//        }
//        return vcomp;
//    }
//
//    public static Map<WeatherPK,Float> getVComponentForLevel(Double level){
//        Map<WeatherPK,Float>vcomp = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry temperatureEntry : VComponentOfWind.getVComponentMap().entrySet()){
//            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
//            if(key.getLevel().equals(level)){
//                vcomp.put(key,(Float)temperatureEntry.getValue());
//            }
//        }
//        return vcomp;
//    }
//
//    public static Float getValueByKey(WeatherPK weatherPK){
//        for(Map.Entry vComponentEntry : VComponentOfWind.getVComponentMap().entrySet()){
//            WeatherPK key = (WeatherPK)vComponentEntry.getKey();
//            if(key.equals(weatherPK)){
//                return (Float)vComponentEntry.getValue();
//            }
//        }
//        return null;
//    }

    @Override
    public int compareTo(VComponentOfWind other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
