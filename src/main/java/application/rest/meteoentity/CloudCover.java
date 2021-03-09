package application.rest.meteoentity;

import application.rest.geoentity.Region;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.StringWriter;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class CloudCover extends Meteo implements Comparable<CloudCover> {

    public static Map<WeatherPK,Float> cloudMap = new TreeMap<WeatherPK,Float>();

    public CloudCover(WeatherPK weatherPK, Float value, Region region) {
        super(weatherPK, value, region);
        if(cloudMap.isEmpty() || !cloudMap.containsKey(weatherPK)){
            cloudMap.put(weatherPK,value);
        }
        if(cloudMap.containsKey(weatherPK)){
            cloudMap.remove(weatherPK);
            cloudMap.put(weatherPK,value);
        }
    }

    @Override
    public void clear() {
        cloudMap.clear();
    }

    @Override
    public DoubleSummaryStatistics getSummaryStatistics() {
        return cloudMap.values().stream()
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

        for(Map.Entry entry : cloudMap.entrySet()){
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

        rootNode.set("cloudMap", meteo);
        rootNode.set("statistics", statistics);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }


//    public static Map<WeatherPK,Float> getCloudForObservationTime(Integer observation){
//        Map<WeatherPK,Float> cloud = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry cloudEntry : CloudCover.getCloudMap().entrySet()){
//            WeatherPK key = (WeatherPK)cloudEntry.getKey();
//            if(key.getObservation() == observation){
//                cloud.put(key,(Float)cloudEntry.getValue());
//            }
//        }
//        return cloud;
//    }
//
//    public static Map<WeatherPK,Float> getCloudForLevel(Double level){
//        Map<WeatherPK,Float> cloud = new TreeMap<WeatherPK,Float>();
//        for(Map.Entry cloudEntry : CloudCover.getCloudMap().entrySet()){
//            WeatherPK key = (WeatherPK)cloudEntry.getKey();
//            if(key.getLevel().equals(level)){
//                cloud.put(key,(Float)cloudEntry.getValue());
//            }
//        }
//        return cloud;
//    }
//
//    public static Float getValueByKey(WeatherPK weatherPK){
//        for(Map.Entry cloudEntry : CloudCover.getCloudMap().entrySet()){
//            WeatherPK key = (WeatherPK)cloudEntry.getKey();
//            if(key.equals(weatherPK)){
//                return (Float)cloudEntry.getValue();
//            }
//        }
//        return null;
//    }

//    public static void update(){
//        List<WindPK> keyList = new ArrayList<WindPK>(CloudCover.getCloudMap().keySet());
//        for(int i = 0; i < CloudCover.getCloudMap().size(); i++) {
//            WindPK key = keyList.get(i);
//            Float value = CloudCover.getCloudMap().get(key);
//            Integer lon = (int)(key.getLongitude()*100);
//            Integer longitude = Math.floorMod(lon,100);
//            if(longitude.equals(25) || longitude.equals(75)){
//                if(i > 0 || i < CloudCover.getCloudMap().size()-1){
//                    WindPK key_prev = keyList.get(i-1);
//                    WindPK key_next = keyList.get(i+1);
//                    if(key_prev.getObservation().equals(key_next.getObservation())){
//                        Float value_prev = CloudCover.getCloudMap().get(key_prev);
//                        Float value_next = CloudCover.getCloudMap().get(key_next);
//                        System.out.println(key + " && " + key_prev + " " + value_next + " " + value_prev + " " + value_next + " -> " + (value_prev + value_next)/2.0f);
//                        cloudMap.remove(key);
//                        cloudMap.put(key,(value_prev + value_next)/2.0f);
//                    }
//                }
//            }
//        }
//    }

    @Override
    public int compareTo(CloudCover other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
