package application.rest.handler;

import application.rest.geoentity.Region;
import application.rest.location.Location;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ucar.unidata.geoloc.LatLonPoint;
import ucar.unidata.geoloc.LatLonPointImpl;
import ucar.unidata.geoloc.LatLonRect;

import java.io.*;
import java.util.*;

public class LocationHandler {

    private final static String GEOCENTER_INPUT_FILE_ROOT = "src/main/resources/static/boundaries/geocenter.csv";
    private final static String NEW_GEOCENTER_INPUT_FILE_ROOT = "src/main/resources/static/boundaries/geocent.csv";
    private final static String BOUNDARIES_INPUT_FILES_DIR_ROOT = "src/main/resources/static/boundaries/all_regions/";
    //    private final static String GRIB2_INPUT_FILES_DIR_ROOT = "src/main/resources/static/grib2/";
    public static List<Region> regions = new ArrayList<>();

    public LocationHandler() {
        regions = init();
    }

    private LatLonRect getRectangularBoundariesOfUkrainianRegion(String regionName){
        File file = getTargetRegionFile(regionName);
        List<LatLonPoint>arr = getArbitraryBoundariesOfUkrainianRegion(file);
        Double max_lat = getMaxLat(arr);
        Double min_lon = getMinLon(arr);
        Double min_lat = getMinLat(arr);
        Double max_lon = getMaxLon(arr);
        LatLonPoint minPoint = new LatLonPointImpl(max_lat, min_lon);
        LatLonPoint maxPoint = new LatLonPointImpl(min_lat, max_lon);
        System.out.println("Rect: " + new LatLonRect(minPoint, maxPoint));
        return new LatLonRect(minPoint, maxPoint);
    }

    public void writeIntoCsv() throws FileNotFoundException {
        BufferedReader csvReader = new BufferedReader(new FileReader(GEOCENTER_INPUT_FILE_ROOT));
        PrintWriter writer = new PrintWriter(new File(NEW_GEOCENTER_INPUT_FILE_ROOT));
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("region");
            sb.append(',');
            sb.append("latitude");
            sb.append(',');
            sb.append("longitude");
            sb.append(',');
            sb.append("max_lat");
            sb.append(',');
            sb.append("min_lon");
            sb.append(',');
            sb.append("min_lat");
            sb.append(',');
            sb.append("max_lon");
            sb.append('\n');

            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] data = line.split(",");
                // getGeoBoundariesOfUkrainianRegion(regionName);

                if(isNumeric(data[1]) && isNumeric(data[2])) {

                    String regionName = data[0];

                    LatLonRect bbox = getRectangularBoundariesOfUkrainianRegion(regionName);
                    System.out.println(" ==> " + data[0] + " " + data[1] + " " + data[2] + " " + bbox);

                    Double latitude = Double.valueOf(data[1]);
                    Double longitude = Double.valueOf(data[2]);
                    Double max_lat = bbox.getLatMax();
                    Double min_lon = bbox.getLonMin();
                    Double min_lat = bbox.getLatMin();
                    Double max_lon = bbox.getLonMax();

                    sb.append(regionName);
                    sb.append(',');
                    sb.append(latitude);
                    sb.append(',');
                    sb.append(longitude);
                    sb.append(',');
                    sb.append(max_lat);
                    sb.append(',');
                    sb.append(min_lon);
                    sb.append(',');
                    sb.append(min_lat);
                    sb.append(',');
                    sb.append(max_lon);
                    sb.append('\n');

                }
            }
            writer.write(sb.toString());
            writer.close();
            csvReader.close();

        } catch(IOException ex){

        }
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

    private boolean isNumeric(String strNum) {
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

    public Region getRegion(String regionName){
        for(Region region: regions){
            if(region.getName().equals(regionName)){
                return region;
            }
        }
        return null;
    }

    public String getRegionName(LatLonRect rect){
        for(Region region: regions){
            if(region.getRectangularBoundaries().equals(rect)){
                return region.getName();
            }
        }
        return null;
    }

    private List<Region> init() {
        List<Region> regions = new ArrayList<>();
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(GEOCENTER_INPUT_FILE_ROOT));
            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] data = line.split(",");
                if(isNumeric(data[1]) && isNumeric(data[2])) {
                    String regionName = data[0];
                    List<LatLonPoint>arbitraryBoundaries = getArbitraryBoundariesOfUkrainianRegion(regionName);

                    Double latitude = Double.valueOf(data[1]);
                    Double longitude = Double.valueOf(data[2]);

                    Double max_lat = Double.valueOf(data[3]);
                    Double min_lon = Double.valueOf(data[4]);
                    Double min_lat = Double.valueOf(data[5]);
                    Double max_lon = Double.valueOf(data[6]);

                    regions.add(
                            new Region(
                                    regionName,
                                    new LatLonPointImpl(latitude,longitude),
                                    new LatLonRect(
                                            new LatLonPointImpl(max_lat,min_lon),
                                            new LatLonPointImpl(min_lat,max_lon)),
                                    arbitraryBoundaries));
                }
            }
            csvReader.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return regions;
    }

    private File getTargetRegionFile(String regionName){
        List<File> boundariesFiles = getFilesList(new File(BOUNDARIES_INPUT_FILES_DIR_ROOT));
        for(File file: boundariesFiles){
            if(file.getName().contains(regionName)){
                return file;
            }
        }
        return null;
    }

    public List<LatLonPoint> getArbitraryBoundariesOfUkrainianRegion(String regionName){
        File file = getTargetRegionFile(regionName);

        List<LatLonPoint> points = new ArrayList<>();
//        String jsonArray = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode point = new ObjectMapper().readValue(file, JsonNode.class);
            point.forEach(array -> {
                LatLonPointImpl pt = new LatLonPointImpl(array.at("/0").asDouble(), array.at("/1").asDouble());
                points.add(pt);
            });
//            jsonArray = mapper.writeValueAsString(coords);
        } catch(IOException ex){
            ex.printStackTrace();
        }
//        return jsonArray;
        return points;
    }

    private double getMaxLat(List<LatLonPoint>arr){
        return arr.stream().mapToDouble(x -> x.getLatitude()).max().getAsDouble();
    }

    private double getMaxLon(List<LatLonPoint>arr){
        return arr.stream().mapToDouble(x -> x.getLongitude()).max().getAsDouble();
    }

    private double getMinLat(List<LatLonPoint>arr){
        return arr.stream().mapToDouble(x -> x.getLatitude()).min().getAsDouble();
    }

    private double getMinLon(List<LatLonPoint>arr){
        return arr.stream().mapToDouble(x -> x.getLongitude()).min().getAsDouble();
    }

    private List<LatLonPoint> getArbitraryBoundariesOfUkrainianRegion(File file){
        List<LatLonPoint> points = new ArrayList<>();
//        String jsonArray = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode point = new ObjectMapper().readValue(file, JsonNode.class);
            point.forEach(array -> {
                points.add(new LatLonPointImpl(array.at("/0").asDouble(), array.at("/1").asDouble()));
            });
//            jsonArray = mapper.writeValueAsString(coords);
        } catch(IOException ex){
            ex.printStackTrace();
        }
//        return jsonArray;
        return points;
    }

//    private void initRegionalCollectionsFor(Integer time, Double level, File boundaryFile) {
//        List<WeatherData> weatherDataList = getWeatherData(time, level);
//        System.out.println("// ------------- // " + boundaryFile.getName() + " // ------------- //");
//        List<Coordinate> boundaries = getBoundaries(boundaryFile);
//        System.out.println("boundaries: " + boundaries.size());
//        System.out.println("geocenter: " + geocenter.size());
//        for (String regionName : geocenter.keySet()) {
//            if (boundaryFile.getName().contains(regionName)) {
//                System.out.println(boundaryFile.getName() + " " + regionName);
//                List<WeatherData> innerDataList = new ArrayList<>();
//                for (WeatherData data : weatherDataList) {
//                    Coordinate point = new Coordinate(data.getWeatherPK().getLatitude(), data.getWeatherPK().getLongitude());
//                    if (isInnerPoint(point, boundaries)) {
//                        innerDataList.add(data);
//                    }
//                    setRegionalData(regionName, innerDataList);
//                }
//            }
//        }
//    }

//    private static DoubleSummaryStatistics getTemperatureStatistics(List<Weather>region){
//        DoubleSummaryStatistics stats = region.stream()
//                .mapToDouble((x) -> x.getTemperature())
//                .summaryStatistics();
//        return stats;
//    }
//
//    private static DoubleSummaryStatistics getHumidityStatistics(List<Weather>region){
//        DoubleSummaryStatistics stats = region.stream()
//                .mapToDouble((x) -> x.getHumidity())
//                .summaryStatistics();
//        return stats;
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

    public boolean isInnerPoint(LatLonPoint point, List<LatLonPoint> boundaries){

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

    private Location findXAxis(LatLonPoint input, List<LatLonPoint> boundaries){
        Double fmin = Double.MAX_VALUE;
        Double smin = Double.MAX_VALUE;
        List<LatLonPoint>firstList = null;
        List<LatLonPoint>secondList = null;
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

    private Location findYAxis(LatLonPoint input, List<LatLonPoint> boundaries){
        Double fmin = Double.MAX_VALUE;
        Double smin = Double.MAX_VALUE;
        List<LatLonPoint>firstList = null;
        List<LatLonPoint>secondList = null;
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

}

