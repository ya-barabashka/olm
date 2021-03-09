package application.rest.meteoentity;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.TreeMap;

public class PressureToMSL extends Meteo {

    public static Map<WeatherPK,Float> pressureToMSLMap = new TreeMap<WeatherPK,Float>();

    public PressureToMSL(WeatherPK weatherPK, Float value) {
        super(weatherPK, value);
        if(pressureToMSLMap.isEmpty() || !pressureToMSLMap.containsKey(weatherPK)){
            pressureToMSLMap.put(weatherPK,value);
        }
        if(pressureToMSLMap.containsKey(weatherPK)){
            pressureToMSLMap.remove(weatherPK);
            pressureToMSLMap.put(weatherPK,value);
        }
    }

    @Override
    public void clear() {
        pressureToMSLMap.clear();
    }

    @Override
    public DoubleSummaryStatistics getSummaryStatistics() {
        return pressureToMSLMap.values().stream()
                .mapToDouble((x) -> x)
                .summaryStatistics();
    }

//    public static Map<WeatherPK,Float> getPressureToMSLForObservationTime(Integer observation){
//        Map<WeatherPK,Float>pressureToMSL = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry pressureToMSLEntry : Temperature.getTemperatureMap().entrySet()){
//            WeatherPK key = (WeatherPK)pressureToMSLEntry.getKey();
//            if(key.getObservation() == observation){
//                pressureToMSL.put(key,(Float)pressureToMSLEntry.getValue());
//            }
//        }
//        return pressureToMSL;
//    }
//
//    public static Map<WeatherPK,Float> getPressureToMSLForLevel(Double level){
//        Map<WeatherPK,Float>pressureToMSL = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry pressureToMSLEntry : PressureToMSL.getPressureToMSLMap().entrySet()){
//            WeatherPK key = (WeatherPK)pressureToMSLEntry.getKey();
//            if(key.getLevel().equals(level)){
//                pressureToMSL.put(key,(Float)pressureToMSLEntry.getValue());
//            }
//        }
//        return pressureToMSL;
//    }
//
//    public static Float getValueByKey(WeatherPK weatherPK){
//        for(Map.Entry pressureToMSLEntry : PressureToMSL.getPressureToMSLMap().entrySet()){
//            WeatherPK key = (WeatherPK)pressureToMSLEntry.getKey();
//            if(key.equals(weatherPK)){
//                return (Float)pressureToMSLEntry.getValue();
//            }
//        }
//        return null;
//    }

}
