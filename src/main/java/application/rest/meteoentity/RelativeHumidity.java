package application.rest.meteoentity;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class RelativeHumidity extends Meteo implements Comparable<RelativeHumidity> {

    public static Map<WeatherPK,Float> humidityMap = new TreeMap<WeatherPK,Float>();

    public RelativeHumidity(WeatherPK weatherPK, Float value) {
        super(weatherPK, value);
        if(humidityMap.isEmpty() || !humidityMap.containsKey(weatherPK)){
            humidityMap.put(weatherPK,value);
        }
        if(humidityMap.containsKey(weatherPK)){
            humidityMap.remove(weatherPK);
            humidityMap.put(weatherPK,value);
        }
    }

    @Override
    public void clear() {
        humidityMap.clear();
    }

    @Override
    public DoubleSummaryStatistics getSummaryStatistics() {
        return humidityMap.values().stream()
                .mapToDouble((x) -> x)
                .summaryStatistics();
    }

//    public static Map<WeatherPK,Float> getHumidityForObservationTime(Integer observation){
//        Map<WeatherPK,Float>humidity = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry humidityEntry : RelativeHumidity.getHumidityMap().entrySet()){
//            WeatherPK key = (WeatherPK)humidityEntry.getKey();
//            if(key.getObservation() == observation){
//                humidity.put(key,(Float)humidityEntry.getValue());
//            }
//        }
//        return humidity;
//    }
//
//    public static Map<WeatherPK,Float> getHumidityForLevel(Double level){
//        Map<WeatherPK,Float>humidity = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry humidityEntry : RelativeHumidity.getHumidityMap().entrySet()){
//            WeatherPK key = (WeatherPK)humidityEntry.getKey();
//            if(key.getLevel().equals(level)){
//                humidity.put(key,(Float)humidityEntry.getValue());
//            }
//        }
//        return humidity;
//    }
//
//    public static Float getValueByKey(WeatherPK weatherPK){
//        for(Map.Entry humidityEntry : RelativeHumidity.getHumidityMap().entrySet()){
//            WeatherPK key = (WeatherPK)humidityEntry.getKey();
//            if(key.equals(weatherPK)){
//                return (Float)humidityEntry.getValue();
//            }
//        }
//        return null;
//    }

    @Override
    public int compareTo(RelativeHumidity other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
