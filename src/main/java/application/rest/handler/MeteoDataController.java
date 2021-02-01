package application.rest.handler;

//@RestController
//@RequestMapping("/data")
public class MeteoDataController {

//    private MeteoDataHandler handler = new MeteoDataHandler();

//    // call http://localhost:8080/weather?currentObservationTime=00&currentValidationTime=03&currentMeteoParams=PRMS&currentMeteoParams=UGRD
//    @PostMapping("/weather")
//    @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
//    String newWeather(@RequestParam(value = "currentObservationTime", required = false) String currentObservationTime,
//                      @RequestParam(value = "currentValidationTime", required = false) String currentValidationTime,
//                      @RequestParam(value = "currentMeteoParams") List<String> currentMeteoParams
//    ) {
//        return currentObservationTime + " " +
//                currentValidationTime + " " +
//                currentMeteoParams.size() + " " +
//                currentMeteoParams.get(1);
//    }
//
//    //      JSON:
////    {
////        "currentObservationTime": "00",
////        "currentValidationTime": "04",
////        "currentMeteoParams": ["a","bhg","c"]
////    }
//    @PostMapping("/weather/init")
////    @CrossOrigin
////    @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
//    WeatherForecast newWeather(HttpServletRequest request, @RequestBody WeatherForecast weatherForecast) throws IOException {
////        System.out.println("resource url: " + request.getRequestURL());
////        System.out.println("currentObservationTime: " + request.getParameter("currentObservationTime"));
////        System.out.println("currentValidationTime: " + request.getParameter("currentValidationTime"));
//        System.out.println("weather inst from newWeather => " + weatherForecast);
//        handler = new MeteoDataHandler(weatherForecast.getCurrentObservationTime(), weatherForecast.getCurrentValidationTime());
//        handler.download();
////        List<File> grib2files = handler.getGrib2FilesList();
////        for(File currentGrib2: grib2files) {
//            handler.initialize();
////        }
//        System.out.println("Temperature => " + Temperature.getTemperatureMap());
//        return weatherForecast;
//    }
//
//    @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
//    @RequestMapping(value="/validation", method = RequestMethod.GET)
//    @ResponseBody
//    public List<String> getValidationList(){
//        return MeteoDataHandler.validation;
//    }
//
//    @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
//    @RequestMapping(value="/observation", method = RequestMethod.GET)
//    @ResponseBody
//    public List<String> getObservationList(){
//        return MeteoDataHandler.observation;
//    }
//
//    @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
//    @RequestMapping(value="/meteo/params", method = RequestMethod.GET)
//    @ResponseBody
//    public List<String> getMeteoParamsList(){
//        return MeteoDataHandler.meteoParams;
//    }

}
