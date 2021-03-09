package application.rest.handler;

import application.rest.geoentity.Region;
import application.rest.meteoentity.*;
import ucar.ma2.*;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GeoGrid;
import ucar.nc2.dt.grid.GridDataset;
import ucar.unidata.io.RandomAccessFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ucar.unidata.geoloc.LatLonRect;
import ucar.unidata.geoloc.LatLonPoint;
import ucar.unidata.geoloc.LatLonPointImpl;

public class MeteoDataHandler {

    public List observation = Arrays. asList("00", "06", "12", "18");
    public List validation = new ArrayList<String>();
    public List meteoParams = Arrays.asList("Pressure_reduced_to_MSL_msl", "Relative_humidity_isobaric", "Total_cloud_cover_isobaric", "Temperature_isobaric", "u-component_of_wind_isobaric", "v-component_of_wind_isobaric");
    public List altitudes = Arrays. asList("100000", "97500", "9500", "92500");
//    private String pattern = "yyyy-MM-dd HH:mm:ss z";
    private String pattern = "yyyyMMdd";
    private String FILE_BASE_URL = "https://nomads.ncep.noaa.gov/pub/data/nccf/com/gfs/prod";
    private String FILE_COMPOUND_URL;
    private String FILE_NAME;
    public String FILE_LOCAL_PATH = "src/main/resources/grib2";

    private String observationHour;
    private String validationHour;
    private String altitude;
    private List<String>selectedMeteoParams = Arrays. asList("Temperature_isobaric", "Pressure_reduced_to_MSL_msl");

    private LocationHandler locationHandler = new LocationHandler();

    public MeteoDataHandler(){}

    public MeteoDataHandler(String observationHour, String validationHour) {
        this.observationHour = observationHour;
        this.validationHour = getCorrectedValidationHour(validationHour);
        this.altitude = "100000";
        this.FILE_COMPOUND_URL = getCompoundURLPath(observationHour);
        this.FILE_NAME = getFileName(observationHour, validationHour);
        this.FILE_BASE_URL = FILE_BASE_URL + "/" + FILE_COMPOUND_URL + "/" + FILE_NAME;
    }

    public File getGrib2FileFullPath(){
        return new File(this.FILE_LOCAL_PATH + "/" + this.FILE_NAME + ".grib2");
    }

    private RandomAccessFile getRandomAccessFile(String grib2file) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(grib2file, "r");
            randomAccessFile.order(RandomAccessFile.BIG_ENDIAN);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return randomAccessFile;
    }

//    private Boolean checkMeteoDataInitialization(){
//        return  RelativeHumidity.getHumidityMap().keySet().size() == CloudCover.getCloudMap().keySet().size() &&
//                CloudCover.getCloudMap().keySet().size() == Temperature.getTemperatureMap().keySet().size() &&
//                Temperature.getTemperatureMap().keySet().size() == UComponentOfWind.getUComponentMap().keySet().size() &&
//                UComponentOfWind.getUComponentMap().keySet().size() == VComponentOfWind.getVComponentMap().keySet().size() &&
//                VComponentOfWind.getVComponentMap().keySet().size() == RelativeHumidity.getHumidityMap().keySet().size() &&
//
//                RelativeHumidity.getHumidityMap().entrySet().size() == CloudCover.getCloudMap().entrySet().size() &&
//                CloudCover.getCloudMap().entrySet().size() == Temperature.getTemperatureMap().entrySet().size() &&
//                Temperature.getTemperatureMap().entrySet().size() == UComponentOfWind.getUComponentMap().entrySet().size() &&
//                UComponentOfWind.getUComponentMap().entrySet().size() == VComponentOfWind.getVComponentMap().entrySet().size() &&
//                VComponentOfWind.getVComponentMap().entrySet().size() == RelativeHumidity.getHumidityMap().entrySet().size() &&
//
//                !(RelativeHumidity.getHumidityMap().keySet().isEmpty() &&
//                        CloudCover.getCloudMap().keySet().isEmpty() &&
//                        Temperature.getTemperatureMap().keySet().isEmpty() &&
//                        UComponentOfWind.getUComponentMap().keySet().isEmpty() &&
//                        VComponentOfWind.getVComponentMap().keySet().isEmpty()) ||
//
//                !(RelativeHumidity.getHumidityMap().entrySet().isEmpty() &&
//                        CloudCover.getCloudMap().entrySet().isEmpty() &&
//                        Temperature.getTemperatureMap().entrySet().isEmpty() &&
//                        UComponentOfWind.getUComponentMap().entrySet().isEmpty() &&
//                        VComponentOfWind.getVComponentMap().entrySet().isEmpty());
//    }

//    private List<Float> getFloatValues(float[] values){
//        List<Float> listOfFloats = new ArrayList<Float>();
//        for(float value : values){
//            listOfFloats.add(Float.valueOf(value));
//        }
//        return listOfFloats;
//    }

    public Boolean isEmpty(){
        List<File> grib2files = getFilesList(new File(FILE_LOCAL_PATH));
        return grib2files.size() == 0;
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
            if(entry.isFile() && entry.getName().endsWith("grib2")){
                files.add(entry);
            }
        }
        return files;
    }

//    private void initWithParam(String paramName, GridDatatype gridDatatype){
//
//        try {
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    private Meteo initWithParamForBBox(String paramName, Region region/*LatLonPoint point1, LatLonPoint point2*/){
        Meteo meteo = null;
        try (GridDataset dataset = GridDataset.open(FILE_LOCAL_PATH + "/" + FILE_NAME + ".grib2")) {
            GeoGrid grid = dataset.findGridByName(paramName);
            GridCoordSystem gcs = grid.getCoordinateSystem();
//            LatLonRect llbb_subset = new LatLonRect(point1, point2);
            List<Range>ranges = gcs.getRangesFromLatLonRect(region.getRectangularBoundaries());
            GridDatatype gridDatatype = grid.subset(null, null, ranges.get(0), ranges.get(1));

            Array data = gridDatatype.readDataSlice(-1, -1, -1, -1);
            GridCoordSystem gcs2 = gridDatatype.getCoordinateSystem();

//            String regionName = locationHandler.getRegionName(region.getRectangularBoundaries());
//            List<LatLonPoint> regionArbitraryBoundaries = locationHandler.getArbitraryBoundariesOfUkrainianRegion(regionName);

            IndexIterator iter = data.getIndexIterator();
            switch(paramName){
                case "Temperature_isobaric": {
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        if (locationHandler.isInnerPoint(point, region.getArbitraryBoundaries())) {
                            meteo = new Temperature(
                                    new WeatherPK(
                                            point.getLatitude(),
                                            to_180(point.getLongitude()),
                                            Double.valueOf("33"),
                                            Integer.valueOf(this.observationHour),
                                            Integer.valueOf(this.validationHour)),
                                    value,
                                    region);
                        }
                    }
                    break;
                }
                case "Relative_humidity_isobaric":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        if (locationHandler.isInnerPoint(point, region.getArbitraryBoundaries())) {
                            meteo = new RelativeHumidity(
                                    new WeatherPK(
                                            point.getLatitude(),
                                            to_180(point.getLongitude()),
                                            Double.valueOf("30"),
                                            Integer.valueOf(this.observationHour),
                                            Integer.valueOf(this.validationHour)),
                                    value,
                                    region);
                        }
                    }
                    break;
                }
                case "v-component_of_wind_isobaric":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        if (locationHandler.isInnerPoint(point, region.getArbitraryBoundaries())) {
                            meteo = new VComponentOfWind(
                                    new WeatherPK(
                                            point.getLatitude(),
                                            to_180(point.getLongitude()),
                                            Double.valueOf("30"),
                                            Integer.valueOf(this.observationHour),
                                            Integer.valueOf(this.validationHour)),
                                    value,
                                    region);
                        }
                    }
                    break;
                }
                case "u-component_of_wind_isobaric":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        if (locationHandler.isInnerPoint(point, region.getArbitraryBoundaries())) {
                            meteo = new UComponentOfWind(
                                    new WeatherPK(
                                            point.getLatitude(),
                                            to_180(point.getLongitude()),
                                            Double.valueOf("30"),
                                            Integer.valueOf(this.observationHour),
                                            Integer.valueOf(this.validationHour)),
                                    value,
                                    region);
                        }
                    }
                    break;
                }
                case "Total_cloud_cover_isobaric":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        if (locationHandler.isInnerPoint(point, region.getArbitraryBoundaries())) {
                            meteo = new CloudCover(
                                    new WeatherPK(
                                            point.getLatitude(),
                                            to_180(point.getLongitude()),
                                            Double.valueOf("21"),
                                            Integer.valueOf(this.observationHour),
                                            Integer.valueOf(this.validationHour)),
                                    value,
                                    region);
                        }
                    }
                    break;
                }
                case "Pressure_reduced_to_MSL_msl":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[2], counter[1]);
                        if (locationHandler.isInnerPoint(point, region.getArbitraryBoundaries())) {
                            meteo = new PressureToMSL(
                                    new WeatherPK(
                                            point.getLatitude(),
                                            to_180(point.getLongitude()),
                                            Double.valueOf("0"),
                                            Integer.valueOf(this.observationHour),
                                            Integer.valueOf(this.validationHour)),
                                    value,
                                    region);
                        }
                    }
                    break;
                }
                default:{

                }
            }

//            System.out.println(
//                    "param: " + paramName + " " +
//                    "region: " + region + " " +
//                    "meteo: " + meteo.getSummaryStatistics());

        } catch (IOException | InvalidRangeException e) {
            e.printStackTrace();
        }
        return meteo;
    }

    public Boolean init(/*LatLonRect rect*/ Region region) throws IOException, InterruptedException, InvalidRangeException {

        long startTime = System.nanoTime();

        for(String param: selectedMeteoParams){
            Meteo meteo = initWithParamForBBox(param,region);
//                    new LatLonPointImpl(rect.getLatMax(), rect.getLonMin()),
//                    new LatLonPointImpl(rect.getLatMin(), rect.getLonMax())

            System.out.println(meteo.toJson());
            meteo.clear();
        }

        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Total execution time to create 1000K objects in Java in millis: "
                + elapsedTime / 1000000);

        return true;
    }

//    public List<WeatherData> getWindDataListByObservationTimeAndLevel(Integer observation, Double level){
//        List<WeatherData> selectedWeatherDataList = new ArrayList<>();
//        List<WeatherData> weatherDataList = getWindDataListByObservationTime(observation);
//
//        IntStream.range(0,weatherDataList.size())
//                .filter(i -> weatherDataList.get(i).getWeatherPK().getLevel().equals(level))
//                .forEach(item -> selectedWeatherDataList.add(weatherDataList.get(item)));


//        for(Integer index = 0; index < weatherDataList.size(); index++){
//            if(weatherDataList.get(index).getWeatherPK().getLevel().equals(level)){
//                selectedWeatherDataList.add(weatherDataList.get(index));
//            }
//        }
//        return selectedWeatherDataList;
//    }

//    public List<WeatherData> getWindDataListByObservationTime(Integer observation){
//        List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
//        Map<WeatherPK,Float> temperatureMap = Temperature.getTemperatureForObservationTime(observation);
//        Map<WeatherPK,Float> ucomponentMap = UComponentOfWind.getUComponentForObservationTime(observation);
//        Map<WeatherPK,Float> vcomponentMap = VComponentOfWind.getVComponentForObservationTime(observation);
//        Map<WeatherPK,Float> cloudMap = CloudCover.getCloudForObservationTime(observation);
//        Map<WeatherPK,Float> humidityMap = RelativeHumidity.getHumidityForObservationTime(observation);
//        if(!(temperatureMap.isEmpty() && ucomponentMap.isEmpty() && vcomponentMap.isEmpty() && humidityMap.isEmpty()) &&
//                ((temperatureMap.size() == ucomponentMap.size()) && (temperatureMap.size() == vcomponentMap.size()) &&
//                        (temperatureMap.size() == cloudMap.size())&&(temperatureMap.size() == humidityMap.size()))){

//        WeatherPK.getKeys()
//                .stream()
//                .filter(pk -> pk.getObservation().equals(observation))
//                .forEach(pk -> weatherDataList.add(
//                        new WeatherData(
//                                pk,
//                                (Float)temperatureMap.get(pk),
//                                (Float)ucomponentMap.get(pk),
//                                (Float)vcomponentMap.get(pk),
//                                (Float)humidityMap.get(pk),
//                                (Float)cloudMap.get(pk))));

//            for(WeatherPK pk : WeatherPK.getKeys()){
//                if(pk.getObservation().equals(observation)){
//                    Float temperature = (Float)temperatureMap.get(pk);
//                    Float ucomponent = (Float)ucomponentMap.get(pk);
//                    Float vcomponent = (Float)vcomponentMap.get(pk);
//                    Float humidity = (Float)humidityMap.get(pk);
//                    Float cloudness = (Float)cloudMap.get(pk);
//                    weatherDataList.add(new WeatherData(pk, temperature, ucomponent, vcomponent, humidity, cloudness));
//                }
//            }
//        }
//        else{
//            System.out.println("temp: " + temperatureMap.size() + " ucomp: " + ucomponentMap.size() + " vcomp: " + vcomponentMap.size() + " cloud: " + cloudMap.size() + " num: " + humidityMap.size());
//            System.out.println("Temperature, UComponent, VComponent, Humidity list don't match equal sizes for observation " + observation);
//        }
//        return weatherDataList;
//    }

    public void toJson(){

    }

    public Set<Double> getDistinctLevels(){
        return WeatherPK.getLevels();
    }

    private double getLatLon(double longitude){
        return longitude >= 180.0 ? longitude - 360.0 : longitude;
    }

    // ---------------------------------------------------------------- //

    private Float convertToCelsius(Float temperatureInKelvins){
        return temperatureInKelvins - 273.15f;
    }

    private Double to_180(Double longitude){
        return mod((longitude + 180.0), 360.0) - 180.0 + getModulo(longitude);
    }

    private double mod(double x, double y){
        return (int)( x - (int)Math.floor( x / y ) * y );
    }

    private double getModulo(double longitude){
        return longitude - Math.floor(longitude);
    }

    public String getFileBaseUrl() {
        return FILE_BASE_URL;
    }

    public void setFileBaseUrl(String fileBaseUrl) {
        FILE_BASE_URL = fileBaseUrl;
    }

    public String getFileName() {
        return FILE_NAME;
    }

    public void setFileName(String fileName) {
        FILE_NAME = fileName;
    }

    public String getObservationHour() {
        return observationHour;
    }

    public void setObservationHour(String observationHour) {
        this.observationHour = observationHour;
    }

    public String getValidationHour() {
        return validationHour;
    }

    public void setValidationHour(String validationHour) {
        this.validationHour = validationHour;
    }

    private String getCorrectedValidationHour(String validationHour){
        if(validationHour != null || !validationHour.isEmpty()){
            if(validationHour.length()==3){
                return validationHour;
            }
            else if(validationHour.length()==2){
                return "0".concat(validationHour);
            }
            else if(validationHour.length()==1){
                return "00".concat(validationHour);
            }
        }
        else{
            return null;
        }
        return null;
    }

    private String getCompoundURLPath(String observationHour){
        return "gfs." +
                getSimpleDate(getCurrentDate()) +
                "/" +
                observationHour;
    }

    private String getFileName(String observationHour, String validationHour){
        return  "gfs" +
                "." +
                "t" + observationHour + "z" +
                "." +
                "pgrb2" +
                "." +
                "0p25" +
                "." +
                "f"+ getCorrectedValidationHour(validationHour);
    }

    public void download(){
        String grib2FileName = FILE_LOCAL_PATH + "/" + FILE_NAME + ".grib2";
        System.out.println("URL: " + FILE_BASE_URL + " fname: " + grib2FileName);
        try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_BASE_URL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(grib2FileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("file is downloaded");
        } catch (IOException e) {
            // handle exception
        }
    }

    private Date getCurrentDate(){
        return Calendar.getInstance().getTime();
    }

    private String getSimpleDate(Date date){
        DateFormat df = new SimpleDateFormat(pattern);
        String dateAsString = df.format(date);
        System.out.println("This is: " + dateAsString);
        return dateAsString;
    }

    private String getUTCDate(Date date){
        DateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateAsString = df.format(date);
        System.out.println(df.format(date));
        return dateAsString;
    }

    private String cmpDates(){
        Date date = getCurrentDate();
        return getSimpleDate(date) + "  " + getUTCDate(date);
    }

    public List<String> getValidationList(){
         return IntStream.rangeClosed(0, 384)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());
    }

    public List<String> getObservationList(){
        return this.observation;
    }

    public List<String> getMeteoParamsList(){
        System.out.println(this.meteoParams);
        return this.meteoParams;
    }

}
