package application.rest.handler;

import application.rest.geoentity.RegionPK;
import application.rest.location.Coordinate;
import application.rest.location.Location;
import application.rest.meteoentity.WeatherPK;
import application.rest.service.MeteoForecastService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ucar.ma2.InvalidRangeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LocationHandler {

    private final static String GEOCENTER_INPUT_FILE_ROOT = "src/main/resources/static/boundaries/geocenter.csv";
    private final static String BOUNDARIES_INPUT_FILES_DIR_ROOT = "src/main/resources/static/boundaries/all_regions/";
    //    private final static String GRIB2_INPUT_FILES_DIR_ROOT = "src/main/resources/static/grib2/";
    private Map<String, Weather> regions = new HashMap<>();
    public static Map<RegionPK, WeatherSummaryStatistics> regionalStatistics = new HashMap<>();
    public static Map<RegionPK, List<Weather>> regionalWindDataList = new HashMap<>();
    private MeteoForecastService meteoForecastService;
    public static Map<String, Coordinate> geocenter = new HashMap<>();

    static{
        geocenter = initGeoCenterCollection();
    }

    public LocationHandler(String observationHour, String validationHour) throws IOException, InterruptedException, InvalidRangeException {
        this.meteoForecastService = new MeteoForecastService(observationHour, validationHour);
        this.meteoForecastService.init();
    }

    public LocationHandler(MeteoForecastService meteoForecastService) throws IOException, InterruptedException, InvalidRangeException {
        this.meteoForecastService = meteoForecastService;
        this.meteoForecastService.init();
    }

    private List<WeatherData> getWeatherData(Integer time, Double level) {
        return meteoForecastService.getWindDataListByObservationTimeAndLevel(time, level);
    }

    private List<File> getFilesList(File folder){
        File[] folderEntries = folder.listFiles();
        List<File> files = new ArrayList<>();
        for (File entry : folderEntries) {
            if (entry.isDirectory()) {
                List<File> subFolderEntries = getFilesList(entry);
                subFolderEntries.stream().filter((subEntry) -> (subEntry.isFile())).forEachOrdered((subEntry) -> {
                    files.add(subEntry);
                });
            }
            if(entry.isFile() && entry.getName().endsWith("json")){
                files.add(entry);
            }
        }
        return files;
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        else{
            try {
                double d = Double.parseDouble(strNum);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        return true;
    }

    private static Map<String, Coordinate> initGeoCenterCollection() {
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(GEOCENTER_INPUT_FILE_ROOT));
            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] data = line.split(",");
                String region = data[0];
                if(isNumeric(data[1]) && isNumeric(data[2])) {
                    Double latitude = Double.valueOf(data[1]);
                    Double longitude = Double.valueOf(data[2]);
                    geocenter.put(region, new Coordinate(latitude, longitude));
                }
            }
            csvReader.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return geocenter;
    }

    private Coordinate getGeoCenterCoord(String regionName){
        for (Map.Entry<String, Coordinate> entry : geocenter.entrySet()) {
            String key = entry.getKey();
            Coordinate value = entry.getValue();
            if(key.equals(regionName)){
                return value;
            }
        }
        return null;
    }

    private List<Coordinate> getBoundaries(File file){
        List<Coordinate> coords = new ArrayList<>();
//        String jsonArray = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode point = new ObjectMapper().readValue(file, JsonNode.class);
            point.forEach(array -> {
                coords.add(new Coordinate(array.at("/0").asDouble(), array.at("/1").asDouble()));
            });
//            jsonArray = mapper.writeValueAsString(coords);
        } catch(IOException ex){
            ex.printStackTrace();
        }
//        return jsonArray;
        return coords;
    }

    public void initBoundariesFor(Integer time, Double level) {
        List<File> boundariesFiles = getFilesList(new File(BOUNDARIES_INPUT_FILES_DIR_ROOT));
        for(File boundaryFile : boundariesFiles){
            initRegionalCollectionsFor(time, level, boundaryFile);
        }
        // File boundaryFile = new File("src/main/resources/static/boundaries/all_regions/cherkasy_oblast.json");
    }

    private void initRegionalCollectionsFor(Integer time, Double level, File boundaryFile) {
        List<WeatherData> weatherDataList = getWeatherData(time, level);
//        System.out.println("// ------------- // " + boundaryFile.getName() + " // ------------- //");
        List<Coordinate> boundaries = getBoundaries(boundaryFile);
//        System.out.println("boundaries: " + boundaries.size());
//        System.out.println("geocenter: " + geocenter.size());
        for (String regionName : geocenter.keySet()) {
            if (boundaryFile.getName().contains(regionName)) {
//                System.out.println(boundaryFile.getName() + " " + regionName);
                List<WeatherData> innerDataList = new ArrayList<>();
                for (WeatherData data : weatherDataList) {
                    Coordinate point = new Coordinate(data.getWeatherPK().getLatitude(), data.getWeatherPK().getLongitude());
                    if (isInnerPoint(point, boundaries)) {
                        innerDataList.add(data);
                    }
                    setRegionalData(regionName, innerDataList);
                }
            }
        }
    }

    private List<Weather> getWindDataUnitByForecast(List<WeatherData> weatherDataList, Integer observation, Integer forecast, Double level){
        List<Weather> weather = new ArrayList<>();
        for(WeatherData data : weatherDataList){
            if(data.getWeatherPK().getObservation().equals(observation) &&
                    data.getWeatherPK().getForecast().equals(forecast) &&
                    data.getWeatherPK().getLevel().equals(level)){
                WeatherPK pk = data.getWeatherPK();
                Float temperature = data.getTemperature();
                Float humidity = data.getHumidity();
                Float cloudness = data.getCloudness();
                Wind wind = new Wind(data.getUComponentOfWind(), data.getVComponentOfWind());
                Integer windSpeed = wind.getWindSpeed();
                Double angle = wind.getMeteorologicalAngle();
                weather.add(new Weather(pk, temperature, humidity, cloudness, windSpeed, angle));
            }
        }
        return weather;
    }

    private void initRegionalCollections(String region, List<WeatherData> data){
//        regionalStatistics.put(new RegionPK(region, observation, forecast, level),
//                                    new WeatherSummaryStatistics(temperatureStats, humidityStats, cloudnessStats, windSpeedStats));
//        regionalWindDataList.put(new RegionPK(region, observation, forecast, level), weatherList);
//        if(!data.isEmpty()){
//            for(Integer observation : WeatherPK.getObservations()){
//                for(Integer forecast : WeatherPK.getForecasts()) {
//                    for(Double level : WeatherPK.getLevels()){
//                        List<Weather> weatherList = getWindDataUnitByForecast(data, observation, forecast, level);
//                        DoubleSummaryStatistics temperatureStats = getTemperatureStatistics(weatherList);
//                        DoubleSummaryStatistics humidityStats = getHumidityStatistics(weatherList);
//                        DoubleSummaryStatistics cloudnessStats = getCloudnessStatistics(weatherList);
//                        DoubleSummaryStatistics windSpeedStats = getWindSpeedStatistics(weatherList);
//                        if(!weatherList.isEmpty()){
//                            regionalStatistics.put(new RegionPK(region, observation, forecast, level),
//                                    new WeatherSummaryStatistics(temperatureStats, humidityStats, cloudnessStats, windSpeedStats));
//                            regionalWindDataList.put(new RegionPK(region, observation, forecast, level), weatherList);
//                        }
//                    }
//                }
//            }
//        }
    }

    private void setRegionalData(String region, List<WeatherData> innerDataList){
        switch (region) {
            case "cherkasy": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "chernihiv": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "chernivtsi": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "dnipropetrovsk": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "donetsk": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "ivanofrankivsk": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "kharkiv": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "kherson": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "khmelnytskyi": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "kiev": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "kirovohrad": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "luhansk": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "lviv": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "odessa": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "poltava": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "rivne": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "sumy": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "ternopil": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "vinnytsia": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "volyn": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "zakarpattia": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "zaporizhia": {
                initRegionalCollections(region, innerDataList);
                break;
            }
            case "zhytomyr": {
                initRegionalCollections(region, innerDataList);
                break;
            }
        }
    }

    private static DoubleSummaryStatistics getTemperatureStatistics(List<Weather>region){
        DoubleSummaryStatistics stats = region.stream()
                .mapToDouble((x) -> x.getTemperature())
                .summaryStatistics();
        return stats;
    }

    private static DoubleSummaryStatistics getHumidityStatistics(List<Weather>region){
        DoubleSummaryStatistics stats = region.stream()
                .mapToDouble((x) -> x.getHumidity())
                .summaryStatistics();
        return stats;
    }

    private static DoubleSummaryStatistics getCloudnessStatistics(List<Weather>region){
//        Integer lon = (int)(key.getLongitude()*100);
//            Integer longitude = Math.floorMod(lon,100);
//            if(longitude.equals(25) || longitude.equals(75)){
        DoubleSummaryStatistics stats = region.stream()
                .filter((x)->!(Math.floorMod((int)(x.getWeatherPK().getLongitude()*100),100) == 25 || Math.floorMod((int)(x.getWeatherPK().getLongitude()*100),100) == 25))
                .mapToDouble((x) -> x.getCloudness())
                .summaryStatistics();
        return stats;
    }

    private static DoubleSummaryStatistics getWindSpeedStatistics(List<Weather>region){
        DoubleSummaryStatistics stats = region.stream()
                .mapToDouble((x) -> x.getWindSpeed())
                .summaryStatistics();
        return stats;
    }

//    public void handle(File file){
//        List<Coordinate> boundaries = getBoundaries(file); // new File("C:/Users/user/Desktop/data.txt")
//        Coordinate point = new Coordinate(50.772, 29.486);
//        System.out.println(isInnerPoint(point, boundaries));
////        return isInnerPoint(point, boundaries);
//    }

//    public List<Coordinate> getBoundaries(File file){
//        List<Coordinate> boundaries = new ArrayList<Coordinate>();
//        try {
//            Scanner myReader = new Scanner(file);
//            while (myReader.hasNextLine()) {
//                String data [] = myReader.nextLine().split("\t");
//                System.out.println("len: " + data.length);
//                System.out.println(data[0] + " " + data[1]);
//                Coordinate point = new Coordinate(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
//                boundaries.add(point);
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        return boundaries;
//    }

    private boolean isInnerPoint(Coordinate point, List<Coordinate> boundaries){

        Location xAxis = findXAxis(point, boundaries);
        Location yAxis = findYAxis(point, boundaries);

//        System.out.println(xAxis.getUpperRightSideCoord() + " " + xAxis.getLowerRightSideCoord() + " " + xAxis.getUpperLeftSideCoord() + " " + xAxis.getLowerLeftSideCoord());
//        System.out.println("x Axis: " + xAxis.size());
//        System.out.println(yAxis.getBottomLeftSideCoord() + " " + yAxis.getBottomRightSideCoord() + " " + yAxis.getTopLeftSideCoord() + " " + yAxis.getTopRightSideCoord());
//        System.out.println("y Axis: " + yAxis.size());

        boolean uppery = false;
        boolean lowery = false;
        boolean leftx = false;
        boolean rightx = false;

//        System.out.println("Check ");
        if(yAxis.existsUpperLeftSideCoord() && yAxis.existsUpperRightSideCoord()){
//            System.out.println("upper Y: " + yAxis.existsUpperLeftSideCoord() + " " + yAxis.existsUpperRightSideCoord());
//            System.out.println("1: " + " (" + yAxis.getUpperLeftSideCoord().getLatitude() + "; " + yAxis.getUpperLeftSideCoord().getLongitude() + ") " + " (" + point.getLatitude() + "; " + point.getLongitude() + ") (" + yAxis.getUpperRightSideCoord().getLatitude() + "; " + yAxis.getUpperRightSideCoord().getLongitude() + ")");
            if(point.getLatitude() <= yAxis.getUpperRightSideCoord().getLatitude() &&
                    point.getLatitude() >= yAxis.getUpperLeftSideCoord().getLatitude() ||
                    point.getLatitude() >= yAxis.getUpperRightSideCoord().getLatitude() &&
                            point.getLatitude() <= yAxis.getUpperLeftSideCoord().getLatitude()){
                uppery = true;
//                System.out.println("yes upper y: " + yAxis.getUpperLeftSideCoord().getLongitude() + " " + point.getLongitude() + " " + yAxis.getUpperRightSideCoord().getLongitude());
            }
        }
        if(yAxis.existsLowerLeftSideCoord()&& yAxis.existsLowerRightSideCoord()){
//            System.out.println("lower Y: " + yAxis.existsLowerLeftSideCoord()+ " " + yAxis.existsLowerRightSideCoord());
//            System.out.println("2: " + " (" + yAxis.getLowerLeftSideCoord().getLatitude() + "; " + yAxis.getLowerLeftSideCoord().getLongitude() + ") (" +  point.getLatitude() + "; " + point.getLongitude() + ") (" + yAxis.getLowerRightSideCoord().getLatitude() + "; " + yAxis.getLowerRightSideCoord().getLongitude() + ")");
            if(point.getLatitude() <= yAxis.getLowerRightSideCoord().getLatitude() &&
                    point.getLatitude() >= yAxis.getLowerLeftSideCoord().getLatitude() ||
                    point.getLatitude() >= yAxis.getLowerRightSideCoord().getLatitude() &&
                            point.getLatitude() <= yAxis.getLowerLeftSideCoord().getLatitude()){
                lowery = true;
//                System.out.println("yes lower y: " + yAxis.getLowerLeftSideCoord().getLongitude() + " " + point.getLongitude() + " " + yAxis.getLowerRightSideCoord().getLongitude());
            }
        }
        if(xAxis.existsTopLeftSideCoord()&& xAxis.existsBottomLeftSideCoord()){
//            System.out.println("right X: " + xAxis.existsTopLeftSideCoord()+ " " + xAxis.existsBottomLeftSideCoord());
//            System.out.println("3: " + " (" + xAxis.getBottomLeftSideCoord().getLatitude() + "; " + xAxis.getBottomLeftSideCoord().getLongitude() + ") (" + point.getLatitude() + "; " + point.getLongitude() + ") (" + xAxis.getTopLeftSideCoord().getLatitude() + "; " + xAxis.getTopLeftSideCoord().getLongitude() + ")");
            if(point.getLongitude() <= xAxis.getTopLeftSideCoord().getLongitude() &&
                    point.getLongitude() >= xAxis.getBottomLeftSideCoord().getLongitude() ||
                    point.getLongitude() >= xAxis.getTopLeftSideCoord().getLongitude() &&
                            point.getLongitude() <= xAxis.getBottomLeftSideCoord().getLongitude()){
                rightx = true;
//                System.out.println("yes left x: " + xAxis.getBottomLeftSideCoord().getLatitude() + " " + point.getLatitude() + " " + xAxis.getTopLeftSideCoord().getLatitude());
            }
        }
        if(xAxis.existsTopRightSideCoord()&& xAxis.existsBottomRightSideCoord()){
//            System.out.println("left X: " + xAxis.existsTopRightSideCoord()+ " " + xAxis.existsBottomRightSideCoord());
//            System.out.println("4: " + " (" + xAxis.getBottomRightSideCoord().getLatitude() + "; " + xAxis.getBottomRightSideCoord().getLongitude() + ") (" + point.getLatitude() + "; " + point.getLongitude() + ") (" + xAxis.getTopRightSideCoord().getLatitude() + "; " + xAxis.getTopRightSideCoord().getLongitude() + ")");
            if(point.getLongitude() <= xAxis.getTopRightSideCoord().getLongitude() &&
                    point.getLongitude() >= xAxis.getBottomRightSideCoord().getLongitude() ||
                    point.getLongitude() >= xAxis.getTopRightSideCoord().getLongitude() &&
                            point.getLongitude() <= xAxis.getBottomRightSideCoord().getLongitude()){
                leftx = true;
//                System.out.println("yes right x: " + xAxis.getBottomRightSideCoord().getLatitude() + " " + point.getLatitude() + " " + xAxis.getTopRightSideCoord().getLatitude());
            }
        }

        return (uppery || lowery) && (leftx || rightx);
    }

    private Location findXAxis(Coordinate input, List<Coordinate> boundaries){
        Double fmin = Double.MAX_VALUE;
        Double smin = Double.MAX_VALUE;
        List<Coordinate>firstList = null;
        List<Coordinate>secondList = null;
        Double fminY = Double.MAX_VALUE;
        Double sminY = Double.MAX_VALUE;

        for(int i = 0; i < boundaries.size(); i++){
            Double currX = null;
            Double currY = null;
            Double nextX = null;
            Double nextY = null;
            if(i == boundaries.size()-1){
                currX = boundaries.get(i).getLatitude();
                currY = boundaries.get(i).getLongitude();
                nextX = boundaries.get(0).getLatitude(); // ????
                if(input.getLatitude() >= currX && input.getLatitude() <= nextX && Math.abs(currY - input.getLongitude()) < fminY){ // left side
                    firstList = new ArrayList<>();
                    firstList.add(boundaries.get(i));
                    firstList.add(boundaries.get(0));
                    fminY = Math.abs(currY - input.getLongitude());
//                    System.out.println("f -> " + "curr: " + currX + " inp: " + input + " next: " + nextX + " abs: " + fmin + " fminY: " + fminY);
                }
                if(input.getLatitude() <= currX && input.getLatitude() >= nextX && Math.abs(currY - input.getLongitude()) < sminY){ // right side
                    secondList = new ArrayList<>();
                    secondList.add(boundaries.get(i));
                    secondList.add(boundaries.get(0));
                    sminY = Math.abs(currY - input.getLongitude());
//                    System.out.println("s -> " + "curr: " + currX + " inp: " + input + " next: " + nextX + " abs: " + smin + " fminY: " + sminY);
//                    System.out.println("size s -> " + secondList.size());
                }
                if(firstList == null){
                    firstList = new ArrayList<>();
                    firstList.add(0, null);
                    firstList.add(1, null);
                }
                if(secondList == null){
                    secondList = new ArrayList<>();
                    secondList.add(0, null);
                    secondList.add(1, null);
                }
            }
            else{
                currX = boundaries.get(i).getLatitude();
                currY = boundaries.get(i).getLongitude();
                nextX = boundaries.get(i+1).getLatitude();
                if(input.getLatitude() >= currX && input.getLatitude() <= nextX && Math.abs(currY - input.getLongitude()) < fminY){
                    firstList = new ArrayList<>();
                    firstList.add(boundaries.get(i));
                    firstList.add(boundaries.get(i+1));
                    fminY = Math.abs(currY - input.getLongitude());
//                    System.out.println("f -> " + "curr: " + currX + " inp: " + input + " next: " + nextX + " abs: " + fmin + " fminY: " + fminY);
                }
                if(input.getLatitude() <= currX && input.getLatitude() >= nextX && Math.abs(currY - input.getLongitude()) < sminY){
                    secondList = new ArrayList<>();
                    secondList.add(boundaries.get(i));
                    secondList.add(boundaries.get(i+1));
                    sminY = Math.abs(currY - input.getLongitude());
//                    System.out.println("s -> " + "curr: " + currX + " inp: " + input + " next: " + nextX + " abs: " + smin + " fminY: " + sminY);
                }
            }
        }
        firstList.addAll(secondList);
        return new Location(firstList);
    }

    private Location findYAxis(Coordinate input, List<Coordinate> boundaries){
        Double fmin = Double.MAX_VALUE;
        Double smin = Double.MAX_VALUE;
        List<Coordinate>firstList = null;
        List<Coordinate>secondList = null;
        Double fminX = Double.MAX_VALUE;
        Double sminX = Double.MAX_VALUE;

        for(int i = 0; i < boundaries.size(); i++){
            Double currY = null;
            Double currX = null;
            Double nextY = null;
            if(i == boundaries.size()-1){
                currY = boundaries.get(i).getLongitude();
                currX = boundaries.get(i).getLatitude();
                nextY = boundaries.get(0).getLongitude();
//                input.getLongitude() <= currY && input.getLongitude() >= nextY && Math.abs(currX - input.getLatitude()) < sminX
//                System.out.println(input.getLongitude() + " " + currY + " " + input.getLongitude() + " " + nextY +
//                        " " + currX + " " + input.getLatitude() + " " + sminX);
                if(input.getLongitude() >= currY && input.getLongitude() <= nextY && Math.abs(currX - input.getLatitude()) < fminX){
                    firstList = new ArrayList<>();
                    firstList.add(boundaries.get(i));
                    firstList.add(boundaries.get(0));
                    fminX = Math.abs(currX - input.getLatitude());
//                    System.out.println("f -> " + "curr: " + currY + " inp: " + input + " next: " + nextY + " abs: " + fmin + " fminX: " + fminX);
                }
                if(input.getLongitude() <= currY && input.getLongitude() >= nextY && Math.abs(currX - input.getLatitude()) < sminX){
                    secondList = new ArrayList<>();
                    secondList.add(boundaries.get(i));
                    secondList.add(boundaries.get(0));
                    sminX = Math.abs(currX - input.getLatitude());
//                    System.out.println("s -> " + "curr: " + currY + " inp: " + input + " next: " + nextY + " abs: " + smin + " fminX: " + fminX);
                }
                if(firstList==null){
                    firstList = new ArrayList<>();
                    firstList.add(0, null);
                    firstList.add(1, null);
                }
                if(secondList==null){
                    secondList = new ArrayList<>();
                    secondList.add(0, null);
                    secondList.add(1, null);
                }
            }
            else{
                currY = boundaries.get(i).getLongitude();
                currX = boundaries.get(i).getLatitude();
                nextY = boundaries.get(i+1).getLongitude();
                if(input.getLongitude() >= currY && input.getLongitude() <= nextY && Math.abs(currX - input.getLatitude()) < fminX){
                    firstList = new ArrayList<>();
                    firstList.add(boundaries.get(i));
                    firstList.add(boundaries.get(i+1));
                    fminX = Math.abs(currX - input.getLatitude());
//                    System.out.println("f -> " + "curr: " + currY + " inp: " + input + " next: " + nextY + " abs: " + fmin + " fminX: " + fminX);
                }
                if(input.getLongitude() <= currY && input.getLongitude() >= nextY && Math.abs(currX - input.getLatitude()) < sminX){
                    secondList = new ArrayList<>();
                    secondList.add(boundaries.get(i));
                    secondList.add(boundaries.get(i+1));
                    sminX = Math.abs(currX - input.getLatitude());
//                    System.out.println("s -> " + "curr: " + currY + " inp: " + input + " next: " + nextY + " abs: " + smin + " sminX: " + sminX);
                }
            }
        }
        firstList.addAll(secondList);
        return new Location(firstList);
    }

    @Override
    public String toString() {
        return "LocationHandler{" +
                "regions=" + regions +
                ", meteoForecastService=" + meteoForecastService +
                ", geocenter=" + geocenter +
                '}';
    }
}

