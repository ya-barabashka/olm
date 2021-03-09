package application.rest.meteoentity;

import application.rest.geoentity.Region;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class UComponentOfWind extends Meteo implements Comparable<UComponentOfWind>{

    public static Map<WeatherPK,Float> uComponentMap = new TreeMap<WeatherPK,Float>();

    public UComponentOfWind(WeatherPK weatherPK, Float value, Region region) {
        super(weatherPK, value, region);
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

    @Override
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        ObjectNode georegion = mapper.createObjectNode();
        georegion.put("name", region.getName());

        ObjectNode geocenter = mapper.createObjectNode();
        geocenter.put("latitude", region.getGeoCenterPoint().getLatitude());
        geocenter.put("longitude", region.getGeoCenterPoint().getLongitude());
        georegion.set("geocenter", geocenter);

        ObjectNode rectangularBoundaries = mapper.createObjectNode();
        rectangularBoundaries.put("latmax", region.getRectangularBoundaries().getLatMax());
        rectangularBoundaries.put("lonmin", region.getRectangularBoundaries().getLonMin());
        rectangularBoundaries.put("latmin", region.getRectangularBoundaries().getLatMin());
        rectangularBoundaries.put("lonmax", region.getRectangularBoundaries().getLonMax());
        georegion.set("rectangularBoundaries", rectangularBoundaries);

        ArrayNode arbitraryBoundaries = mapper.valueToTree(region.getArbitraryBoundaries());
        georegion.putArray("arbitraryBoundaries").addAll(arbitraryBoundaries);

        ObjectNode meteo = mapper.createObjectNode();

        for(Map.Entry entry : uComponentMap.entrySet()){
            ObjectNode weatherPK = mapper.createObjectNode();
            WeatherPK key = (WeatherPK)entry.getKey();
            weatherPK.put("latitude", key.getLatitude());
            weatherPK.put("longitude", key.getLongitude());
            weatherPK.put("level", key.getLevel());
            weatherPK.put("observation", key.getObservation());
            weatherPK.put("forecast", key.getForecast());

            meteo.put(weatherPK.toString(), ((Float)entry.getValue()).toString());
        }

        DoubleSummaryStatistics summaryStatistics = getSummaryStatistics();

        ObjectNode statistics = mapper.createObjectNode();
        statistics.put("count", summaryStatistics.getCount());
        statistics.put("sum", summaryStatistics.getSum());
        statistics.put("min", summaryStatistics.getMin());
        statistics.put("average", summaryStatistics.getAverage());
        statistics.put("max", summaryStatistics.getMax());

        rootNode.set("uComponentMap", meteo);
        rootNode.set("statistics", statistics);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
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
