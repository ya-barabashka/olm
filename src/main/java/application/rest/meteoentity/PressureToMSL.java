package application.rest.meteoentity;

import java.util.Map;
import java.util.TreeMap;

public class PressureToMSL {

    private WeatherPK weatherPK;
    private Float pressureToMSL;
    public static Map<WeatherPK,Float> pressureToMSLMap = new TreeMap<WeatherPK,Float>();

    public PressureToMSL() {
    }

    public PressureToMSL(WeatherPK weatherPK, Float pressureToMSL) {
        this.weatherPK = weatherPK;
        this.pressureToMSL = pressureToMSL;
        if(pressureToMSLMap.isEmpty() || !pressureToMSLMap.containsKey(weatherPK)){
            pressureToMSLMap.put(weatherPK,pressureToMSL);
        }
        if(pressureToMSLMap.containsKey(weatherPK)){
            pressureToMSLMap.remove(weatherPK);
            pressureToMSLMap.put(weatherPK,pressureToMSL);
        }
    }

    public WeatherPK getWeatherPK() {
        return weatherPK;
    }

    public void setWeatherPK(WeatherPK weatherPK) {
        this.weatherPK = weatherPK;
    }

    public Float getPressureToMSL() {
        return pressureToMSL;
    }

    public void setPressureToMSL(Float pressureToMSL) {
        this.pressureToMSL = pressureToMSL;
    }

    public static Map<WeatherPK, Float> getPressureToMSLMap() {
        return pressureToMSLMap;
    }

    public static void setPressureToMSLMap(Map<WeatherPK, Float> pressureToMSLMap) {
        PressureToMSL.pressureToMSLMap = pressureToMSLMap;
    }

    public static Map<WeatherPK,Float> getPressureToMSLForObservationTime(Integer observation){
        Map<WeatherPK,Float>pressureToMSL = new TreeMap<WeatherPK,Float>();
        for(Map.Entry pressureToMSLEntry : Temperature.getTemperatureMap().entrySet()){
            WeatherPK key = (WeatherPK)pressureToMSLEntry.getKey();
            if(key.getObservation() == observation){
                pressureToMSL.put(key,(Float)pressureToMSLEntry.getValue());
            }
        }
        return pressureToMSL;
    }

    public static Map<WeatherPK,Float> getPressureToMSLForLevel(Double level){
        Map<WeatherPK,Float>pressureToMSL = new TreeMap<WeatherPK,Float>();
        for(Map.Entry pressureToMSLEntry : PressureToMSL.getPressureToMSLMap().entrySet()){
            WeatherPK key = (WeatherPK)pressureToMSLEntry.getKey();
            if(key.getLevel().equals(level)){
                pressureToMSL.put(key,(Float)pressureToMSLEntry.getValue());
            }
        }
        return pressureToMSL;
    }

    public static Float getValueByKey(WeatherPK weatherPK){
        for(Map.Entry pressureToMSLEntry : PressureToMSL.getPressureToMSLMap().entrySet()){
            WeatherPK key = (WeatherPK)pressureToMSLEntry.getKey();
            if(key.equals(weatherPK)){
                return (Float)pressureToMSLEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "PressureMSL{" +
                "weatherPK=" + weatherPK +
                ", pressureToMSL=" + pressureToMSL +
                '}';
    }
}
