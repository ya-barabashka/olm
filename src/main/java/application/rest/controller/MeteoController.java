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
import java.util.ArrayList;
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
    List<String> newWeather(HttpServletRequest request, @RequestBody WeatherForecast weatherForecast) throws IOException, InterruptedException, InvalidRangeException {
        System.out.println("resource url: " + request.getRequestURL());
        System.out.println("currentObservationTime: " + request.getParameter("currentObservationTime"));
        System.out.println("currentValidationTime: " + request.getParameter("currentValidationTime"));
        System.out.println("weather inst from newWeather => " + weatherForecast);

        meteoForecastService = new MeteoForecastService(weatherForecast.getCurrentObservationTime(), weatherForecast.getCurrentValidationTime());

        return meteoForecastService.getMeteoJsonList();
    }

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
