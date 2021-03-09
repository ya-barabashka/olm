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

public class VComponentOfWind extends Meteo implements Comparable<VComponentOfWind> {

    public static Map<WeatherPK,Float> vComponentMap = new TreeMap<WeatherPK,Float>();

    public VComponentOfWind(WeatherPK weatherPK, Float value, Region region) {
        super(weatherPK, value, region);
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

        for(Map.Entry entry : vComponentMap.entrySet()){
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

        rootNode.set("vComponentMap", meteo);
        rootNode.set("statistics", statistics);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
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
