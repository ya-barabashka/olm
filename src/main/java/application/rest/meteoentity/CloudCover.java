package application.rest.meteoentity;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class CloudCover extends Meteo implements Comparable<CloudCover> {

    public static Map<WeatherPK,Float> cloudMap = new TreeMap<WeatherPK,Float>();

    public CloudCover(WeatherPK weatherPK, Float value) {
        super(weatherPK, value);
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
