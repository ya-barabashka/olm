package application.rest.meteoentity;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class CloudCover implements Comparable<CloudCover> {

    private WeatherPK weatherPK;
    private Float cloud;
    public static Map<WeatherPK,Float> cloudMap = new TreeMap<WeatherPK,Float>();

    public CloudCover() {
    }

    public CloudCover(WeatherPK weatherPK, Float cloud) {
        this.weatherPK = weatherPK;
        this.cloud = cloud;
        if(cloudMap.isEmpty() || !cloudMap.containsKey(weatherPK)){
            cloudMap.put(weatherPK,cloud);
        }
        if(cloudMap.containsKey(weatherPK)){
            cloudMap.remove(weatherPK);
            cloudMap.put(weatherPK,cloud);
        }
    }

    public WeatherPK getWeatherPK() {
        return weatherPK;
    }

    public void setWeatherPK(WeatherPK weatherPK) {
        this.weatherPK = weatherPK;
    }

    public Float getCloud() {
        return cloud;
    }

    public void setCloud(Float cloud) {
        this.cloud = cloud;
    }

    public static Map<WeatherPK, Float> getCloudMap() {
        return cloudMap;
    }

    public static Map<WeatherPK,Float> getCloudForObservationTime(Integer observation){
        Map<WeatherPK,Float> cloud = new TreeMap<WeatherPK,Float>();
        for(Map.Entry cloudEntry : CloudCover.getCloudMap().entrySet()){
            WeatherPK key = (WeatherPK)cloudEntry.getKey();
            if(key.getObservation() == observation){
                cloud.put(key,(Float)cloudEntry.getValue());
            }
        }
        return cloud;
    }

    public static Map<WeatherPK,Float> getCloudForLevel(Double level){
        Map<WeatherPK,Float> cloud = new TreeMap<WeatherPK,Float>();
        for(Map.Entry cloudEntry : CloudCover.getCloudMap().entrySet()){
            WeatherPK key = (WeatherPK)cloudEntry.getKey();
            if(key.getLevel().equals(level)){
                cloud.put(key,(Float)cloudEntry.getValue());
            }
        }
        return cloud;
    }

    public static Float getValueByKey(WeatherPK weatherPK){
        for(Map.Entry cloudEntry : CloudCover.getCloudMap().entrySet()){
            WeatherPK key = (WeatherPK)cloudEntry.getKey();
            if(key.equals(weatherPK)){
                return (Float)cloudEntry.getValue();
            }
        }
        return null;
    }

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
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.weatherPK);
        hash = 19 * hash + Objects.hashCode(this.cloud);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CloudCover other = (CloudCover) obj;
        if (!Objects.equals(this.weatherPK, other.weatherPK)) {
            return false;
        }
        if (!Objects.equals(this.cloud, other.cloud)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "windPK=" + weatherPK + ", "
                + "cloud=" + cloud;
    }

    @Override
    public int compareTo(CloudCover other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
