package application.rest.meteoentity;

import application.rest.geoentity.Region;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.TreeMap;

public class PressureToMSL extends Meteo {

    public static Map<WeatherPK,Float> pressureToMSLMap = new TreeMap<WeatherPK,Float>();

    public PressureToMSL(WeatherPK weatherPK, Float value, Region region) {
        super(weatherPK, value, region);
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

        for(Map.Entry entry : pressureToMSLMap.entrySet()){
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

        rootNode.set("pressureToMSLMap", meteo);
        rootNode.set("statistics", statistics);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
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
