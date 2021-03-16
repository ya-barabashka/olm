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

public class RelativeHumidity extends Meteo implements Comparable<RelativeHumidity> {

    public static Map<WeatherPK,Float> humidityMap = new TreeMap<WeatherPK,Float>();

    public RelativeHumidity(WeatherPK weatherPK, Float value, Region region) {
        super(weatherPK, value, region);
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

        ArrayNode temperatureArray = mapper.createArrayNode();

        for(Map.Entry entry : humidityMap.entrySet()){
            ObjectNode temperature = mapper.createObjectNode();

            ObjectNode weatherPK = mapper.createObjectNode();
            WeatherPK key = (WeatherPK)entry.getKey();
            weatherPK.put("latitude", key.getLatitude());
            weatherPK.put("longitude", key.getLongitude());
            weatherPK.put("level", key.getLevel());
            weatherPK.put("observation", key.getObservation());
            weatherPK.put("forecast", key.getForecast());
            temperature.set("weatherPK", weatherPK);

            Float value = (Float)entry.getValue();
            temperature.put("value", value);

            temperatureArray.add(temperature);
        }

        DoubleSummaryStatistics summaryStatistics = getSummaryStatistics();

        ObjectNode statistics = mapper.createObjectNode();
        statistics.put("count", summaryStatistics.getCount());
        statistics.put("sum", summaryStatistics.getSum());
        statistics.put("min", summaryStatistics.getMin());
        statistics.put("average", summaryStatistics.getAverage());
        statistics.put("max", summaryStatistics.getMax());

        ObjectNode meteo = mapper.createObjectNode();

        meteo.set("georegion", georegion);
        meteo.set("meteo", temperatureArray);
        meteo.set("statistics", statistics);

        rootNode.set("humidity", meteo);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

    @Override
    public int compareTo(RelativeHumidity other) {
        return weatherPK.compareTo(other.weatherPK);
    }

}
