package application.rest.controller;

import application.rest.handler.LocationHandler;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

//    final static String INPUT_FILES_DIR_ROOT = "src/main/resources/static/boundaries/all_regions/"; // src/main/resources/boundaries
//
//    private static Map<Double, List<WeatherData>> levelMap = new HashMap<>();
//    private static Map<Integer, List<WeatherData>> timeMap = new HashMap<>();
////    private static MeteoSurveillanceService locationService = new LocationService();
////    private static MeteoSurveillanceService forecastService = new MeteoForecastService();
//
//    static final ObjectMapper mapper = new ObjectMapper();
//
//    static{
//        try {
////            meteoForecastService.init();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public LocationController() {
////        handler = new LocationHandler(file);
//    }
//
////    @RequestMapping (value = "/handle", produces="application/json", headers = {"content-type=text/plain"})
////    public void process(@RequestBody String filePath) {
////        File file = new File(filePath);
////        String absoluteFilePath = file.getAbsolutePath();
////        ObjectMapper mapper = new ObjectMapper();
////        JsonNode childNode = mapper.createObjectNode();
////        ((ObjectNode) childNode).put(file.getName(), file.getAbsolutePath());
////        handler.handle(file);
////    }
//
////    @RequestMapping(value = "/boundaries/coords",  method = RequestMethod.GET)
////    public ResponseEntity boundaries(HttpServletResponse response) throws IOException {
////        List<Coordinate> coords = handler.getBoundaries(new File("src/main/resources/static/boundaries/all_regions/cherkasy_oblast.json"));
////        return ResponseEntity.ok(coords);
////    }
//
    @RequestMapping(value="/init", method = RequestMethod.GET)
    public String init() throws FileNotFoundException {
        LocationHandler handler = new LocationHandler();
        handler.writeIntoCsv();
        return "ok";
    }
//
//    private static void initialize(){
//        for(Integer time : WeatherPK.getObservations()){
//            for(Double level : WeatherPK.getLevels()){
//                locationHandler.initFor(time, level);
//            }
//        }
//    }
//
//    private void write(PrintWriter writer, List<WeatherData> region){
//        for(WeatherData data : region){
//            writer.println(data);
//        }
//    }
//
//    private static void write(PrintWriter writer, DoubleSummaryStatistics statistics){
//        writer.println("avg: " + statistics.getAverage() + " min: " + statistics.getMin() + " max: " + statistics.getMax());
//    }
//
//    @RequestMapping(value = "/{region}/{observation}/{forecast}/{level}",  method = RequestMethod.GET)
//    public ResponseEntity calculate(@PathVariable String region, @PathVariable Integer observation, @PathVariable Integer forecast, @PathVariable Double level) throws IOException {
//        for (Map.Entry<RegionPK, WeatherSummaryStatistics> entry : LocationHandler.regionalStatistics.entrySet()) {
//            RegionPK key = entry.getKey();
////            WeatherSummaryStatistics value = entry.getValue();
//            if(key.equals(new RegionPK(region, observation, forecast, level))){
//                List<Weather> value = LocationHandler.regionalWindDataList.get(key);
//                return ResponseEntity.ok(value);
//            }
//        }
//        return ResponseEntity.ok(null);
//    }
//
//    @RequestMapping(value="/objmapper", method = RequestMethod.GET)
//    @ResponseBody
//    public Map<RegionPK, WeatherSummaryStatistics> getObjNode(){
////        Map<String, List<Double>>map = new HashMap<>();
////        List<Double>list = new ArrayList<>(Arrays.asList(23.6, 78.4, 13.8));
////        map.put("zhyt", list);
////        map.put("polt", list);
//        return LocationHandler.regionalStatistics;
////        return map;
//    }
//
//    private ObjectNode convObjToONode(Object o) {
//        StringWriter stringify = new StringWriter();
//        ObjectNode objToONode = null;
//
//        try {
//            mapper.writeValue(stringify, o);
//            objToONode = (ObjectNode) mapper.readTree(stringify.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(objToONode);
//        return objToONode;
//    }
//
//    @RequestMapping(value="/mapper", method = RequestMethod.GET)
//    //pass it your payload
//    public ObjectNode conv(Object o) {
//        return convObjToONode(LocationHandler.regionalStatistics);
//    }
//
////    @RequestMapping(value = "/ukraine",  method = RequestMethod.POST)
////    private String getBoundaries(@RequestBody String filePath) throws JsonProcessingException {
////        List<File>files = getFileList(new File(filePath));
////        Map<String, List<Coordinate>>ukraine = new HashMap<>();
////        try{
////            for(File file : files){
////                List<Coordinate> coords = new ArrayList<>();
////                JsonNode point = new ObjectMapper()
////                        .readValue(new File(file.getPath()), // "src/main/resources/boundaries/zhytomyr/zhytomyr.json"
////                                JsonNode.class);
////                point.forEach(array -> {
////                    coords.add(new Coordinate(array.at("/0").asDouble(), array.at("/1").asDouble()));
////                });
////                ukraine.put(file.getName(), coords);
////            }
//            /*for (Map.Entry<String,List<Coordinate>> entry : ukraine.entrySet()){
//                String key = entry.getKey();
//                List<Coordinate> value = entry.getValue();
//                System.out.println("Key = " + key);
//                for(Coordinate coord : value){
//                    System.out.println("Val = " + coord);
//                }
//            }*/
////    } catch(IOException ex){
////            ex.printStackTrace();
////        }
////        return new ObjectMapper().writeValueAsString(ukraine);
////    }
//
////    private static List<File> getFileList(File folder){
////        File[] folderEntries = folder.listFiles();
////        List<File> files = new ArrayList<File>();
////        for (File entry : folderEntries) {
////            if (entry.isDirectory()) {
////                List<File> subFolderEntries = getFileList(entry);
////                for (File subEntry : subFolderEntries) {
////                    if(subEntry.isFile() && subEntry.getName().endsWith(".json"))
////                        files.add(subEntry);
////                }
////            }
////            if(entry.isFile() && entry.getName().endsWith(".json")){
////                files.add(entry);
////            }
////        }
////        return files;
////    }
//
////    @RequestMapping(value = "/data",  method = RequestMethod.GET)
////    public ResponseEntity testGen(HttpServletResponse response) throws IOException {
////        response.setStatus(200);
////        UcarGrib2Utility util = new UcarGrib2Utility();
//////        File file = new File("src/main/resources/grib2/nogaps_GRIB2_02-07_F000.grb2");
////        util.init();
////        Set<Double> levels = WindPK.getLevels();
////        System.out.println("levels size: " + levels.size());
////        for(Double level : levels){
////            List<WindData> dataList = util.getWindDataGeneralList(level);
////            map.put(level, dataList);
//////            System.out.println("dataList size: " + dataList.size());
//////            response.getWriter().println("level: " + level);
//////            for(WindData data : dataList){
//////                response.getWriter().println(data);
//////                System.out.println(data);
//////            }
////        }
////        return ResponseEntity.ok(map);
////    }
//
////    @RequestMapping(value = "/data/{time}/{level}",  method = RequestMethod.GET)
////    public ResponseEntity newtest(@PathVariable Integer time, @PathVariable Double level, HttpServletResponse response) throws IOException {
////        response.setStatus(200);
////        LocationHandler loc = new LocationHandler();
////        List<WindData> dataList = handler.getWeatherData(time, level);
////        List<WindData> dataList = new ArrayList<>();
///*
//        UcarGrib2Utility util = new UcarGrib2Utility();
//        util.init();
//        System.out.println("from fronend: " + time + " " + level);
//        List<WindData> dataList = util.getWindDataListByObservationTimeAndLevel(time, level);
//*/
////        LocationHandler location = new LocationHandler();
////        List<WindData> dataList = handler.getWeatherData(time, level);
////        return ResponseEntity.ok(dataList);
////    }
//
//
//    @RequestMapping(value = "/data/{time}",  method = RequestMethod.GET)
//    public ResponseEntity newtest(@PathVariable Integer time, HttpServletResponse response) throws IOException {
//        response.setStatus(200);
//        MeteoDataHandler handler = new MeteoDataHandler();
//        File file = new File("src/main/resources/grib2/gfs.t00z.pgrb2.0p25.f004.grib2");
//        handler.initialize();
////        Set<Double> levels = WindPK.getLevels();
////        System.out.println("levels size: " + levels.size());
////        for(Double level : levels){
//        List<WeatherData> dataList = handler.getWindDataListByObservationTime(time);
//        timeMap.put(time, dataList);
////            System.out.println("dataList size: " + dataList.size());
////            response.getWriter().println("level: " + level);
////            for(WindData data : dataList){
////                response.getWriter().println(data);
////                System.out.println(data);
////            }
////        }
//        return ResponseEntity.ok(timeMap);
//    }
//
////    @RequestMapping(value = "/data/{level}",  method = RequestMethod.GET)
////    public ResponseEntity test00(@PathVariable Double level, HttpServletResponse response) throws IOException {
////        response.setStatus(200);
////        MeteoDataHandler handler = new MeteoDataHandler();
////        File file = new File("src/main/resources/grib2/gfs.t00z.pgrb2.0p25.f004.grib2");
////        handler.initialize(file);
//////        Set<Double> levels = WindPK.getLevels();
//////        System.out.println("levels size: " + levels.size());
//////        for(Double level : levels){
////        List<WeatherData> dataList = handler.getWindDataListByObservationTimeAndLevel(level);
////        levelMap.put(level, dataList);
//////            System.out.println("dataList size: " + dataList.size());
//////            response.getWriter().println("level: " + level);
//////            for(WindData data : dataList){
//////                response.getWriter().println(data);
//////                System.out.println(data);
//////            }
//////        }
////        return ResponseEntity.ok(levelMap);
////    }
//
//    /*
//    @RequestMapping(value = "/cloud",  method = RequestMethod.GET)
//    public ResponseEntity testCloud(HttpServletResponse response) throws IOException {
//        response.setStatus(200);
//        UcarGrib2Utility util = new UcarGrib2Utility();
////        File file = new File("src/main/resources/grib2/nogaps_GRIB2_02-07_F000.grb2");
//        util.init();
//        Set<Double> levels = WindPK.getLevels();
//        System.out.println("levels size: " + levels.size());
//        for(Double level : levels){
//            List<WindData> dataList = util.getWindDataGeneralList(level);
//            map.put(level, dataList);
////            System.out.println("dataList size: " + dataList.size());
////            response.getWriter().println("level: " + level);
////            for(WindData data : dataList){
////                response.getWriter().println(data);
////                System.out.println(data);
////            }
//        }
//        return ResponseEntity.ok(map);
//    }
//    */
//
////    @RequestMapping(value = "/ncep/download",  method = RequestMethod.GET)
////    private static void ncep() {
////        NcepModelDataSet ncep = new NcepModelDataSet("https://nomads.ncep.noaa.gov/", new File("src/main/resources/ncep"));
////        try {
////            ncep.downloadWeatherData();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    @RequestMapping(value = "/",  method = RequestMethod.GET)
//    public String test() {
//        return "index";
//    }

}
