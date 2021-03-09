package application.rest.meteoentity;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Temperature extends Meteo implements Comparable<Temperature> {

    public static Map<WeatherPK,Float> temperatureMap = new TreeMap<WeatherPK,Float>();

    public Temperature(WeatherPK weatherPK, Float value) {
        super(weatherPK, value);
        if(temperatureMap.isEmpty() || !temperatureMap.containsKey(weatherPK)){
            temperatureMap.put(weatherPK,value);
        }
        if(temperatureMap.containsKey(weatherPK)){
            temperatureMap.remove(weatherPK);
            temperatureMap.put(weatherPK,value);
        }
    }

    @Override
    public void clear() {
        temperatureMap.clear();
    }

    @Override
    public DoubleSummaryStatistics getSummaryStatistics() {
        return temperatureMap.values().stream()
                .mapToDouble((x) -> x)
                .summaryStatistics();
    }

//    public static Map<WeatherPK,Float> getTemperatureForObservationTime(Integer observation){
//        Map<WeatherPK,Float>temperature = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry temperatureEntry : Temperature.getTemperatureMap().entrySet()){
//            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
//            if(key.getObservation() == observation){
//                temperature.put(key,(Float)temperatureEntry.getValue());
//            }
//        }
//        return temperature;
//    }
//
//    public static Map<WeatherPK,Float> getTemperatureForLevel(Double level){
//        Map<WeatherPK,Float>temperature = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry temperatureEntry : Temperature.getTemperatureMap().entrySet()){
//            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
//            if(key.getLevel().equals(level)){
//                temperature.put(key,(Float)temperatureEntry.getValue());
//            }
//        }
//        return temperature;
//    }
//
//    public static Float getValueByKey(WeatherPK weatherPK){
//        for(Map.Entry temperatureEntry : Temperature.getTemperatureMap().entrySet()){
//            WeatherPK key = (WeatherPK)temperatureEntry.getKey();
//            if(key.equals(weatherPK)){
//                return (Float)temperatureEntry.getValue();
//            }
//        }
//        return null;
//    }

    @Override
    public int compareTo(Temperature other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
