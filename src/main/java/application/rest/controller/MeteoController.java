package application.rest.controller;

import application.rest.geoentity.Region;
import application.rest.handler.*;
import application.rest.meteoentity.PressureToMSL;
import application.rest.meteoentity.Temperature;
import application.rest.service.MeteoForecastService;
import org.springframework.web.bind.annotation.*;
import ucar.ma2.InvalidRangeException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000", "https://maps.googleapis.com/**"}) //  {"http://localhost:7777", "http://someserver:8080"}
@RestController
@RequestMapping("/data")
public class MeteoController {

    private static Map<Double, List<WeatherData>> levelMap = new HashMap<>();
    private static Map<Integer, List<WeatherData>> timeMap = new HashMap<>();
    private MeteoForecastService meteoForecastService;
    private MeteoDataHandler handler = new MeteoDataHandler();

    // call http://localhost:8080/weather?currentObservationTime=00&currentValidationTime=03&currentMeteoParams=PRMS&currentMeteoParams=UGRD
    @PostMapping("/weather")
//    @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
    String newWeather(@RequestParam(value = "currentObservationTime", required = false) String currentObservationTime,
                      @RequestParam(value = "currentValidationTime", required = false) String currentValidationTime,
                      @RequestParam(value = "currentMeteoParams") List<String> currentMeteoParams
    ) {
        return currentObservationTime + " " +
                currentValidationTime + " " +
                currentMeteoParams.size() + " " +
                currentMeteoParams.get(1);
    }

    //      JSON:
//    {
//        "currentObservationTime": "00",
//        "currentValidationTime": "04",
//        "currentMeteoParams": ["a","bhg","c"]
//    }
    @PostMapping("/weather/init")
    @CrossOrigin(origins = {"http://localhost:3000", "https://maps.googleapis.com/**"})
    void newWeather(HttpServletRequest request, @RequestBody WeatherForecast weatherForecast) throws IOException, InterruptedException, InvalidRangeException {
        System.out.println("resource url: " + request.getRequestURL());
        System.out.println("currentObservationTime: " + request.getParameter("currentObservationTime"));
        System.out.println("currentValidationTime: " + request.getParameter("currentValidationTime"));
        System.out.println("weather inst from newWeather => " + weatherForecast);
//        MeteoDataHandler handler = new MeteoDataHandler(weatherForecast.getCurrentObservationTime(), weatherForecast.getCurrentValidationTime());
//        handler.download();
        meteoForecastService = new MeteoForecastService(weatherForecast.getCurrentObservationTime(), weatherForecast.getCurrentValidationTime());
//
//        meteoForecastLocationService.getMeteoForecastService().download();
//
//        while (meteoForecastLocationService.getMeteoForecastService().isEmpty()) {
//            Thread.sleep(1000);
//        }

        for(Region region: LocationHandler.regions){
            meteoForecastService.init(region.getRectangularBoundaries());
        }

//        meteoForecastLocationService.getLocationService();

//        meteoForecastService = new MeteoForecastService(weatherForecast.getCurrentObservationTime(), weatherForecast.getCurrentValidationTime());
//        meteoForecastService.download();
//        meteoForecastService.init();
//
//        locationService = new LocationService(weatherForecast.getCurrentObservationTime(), weatherForecast.getCurrentValidationTime());
        System.out.println("Temperature => " + Temperature.temperatureMap);
        System.out.println("PressureToMSL => " + PressureToMSL.pressureToMSLMap);
//        return weatherForecast;
    }

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

//    @RequestMapping(value = "/data/{time}",  method = RequestMethod.GET)
//    public ResponseEntity newtest(@PathVariable Integer time, HttpServletResponse response) throws IOException {
//        response.setStatus(200);
//        List<WeatherData> dataList =meteoForecastService.getWindDataListByObservationTime(time);
//        timeMap.put(time, dataList);
//        return ResponseEntity.ok(timeMap);
//    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://maps.googleapis.com/**"})
    @RequestMapping(value="/validation", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getValidationList(){
        return handler.getValidationList();
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://maps.googleapis.com/**"})
    @RequestMapping(value="/observation", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getObservationList(){
        return handler.getObservationList();
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://maps.googleapis.com/**"})
    @RequestMapping(value="/meteo/params", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getMeteoParamsList(){
        return handler.getMeteoParamsList();
    }

}
