package application.rest.service;

import application.rest.geoentity.Region;
import application.rest.handler.LocationHandler;
import application.rest.handler.MeteoDataHandler;
import application.rest.handler.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import ucar.ma2.InvalidRangeException;
import ucar.unidata.geoloc.LatLonRect;

import java.io.IOException;
import java.util.List;

public class MeteoForecastService {

    private MeteoDataHandler meteoDataHandler = new MeteoDataHandler();

    public MeteoForecastService(String observationHour, String validationHour) {
        this.meteoDataHandler = new MeteoDataHandler(observationHour, validationHour);
    }

    public MeteoDataHandler getMeteoDataHandler() {
        return meteoDataHandler;
    }

    public void setMeteoDataHandler(MeteoDataHandler meteoDataHandler) {
        this.meteoDataHandler = meteoDataHandler;
    }

//    public List<WeatherData> getWindDataListByObservationTimeAndLevel(Integer observation, Double level){
//        return this.meteoDataHandler.getWindDataListByObservationTimeAndLevel(observation,level);
//    }
//
//    public List<WeatherData> getWindDataListByObservationTime(Integer observation){
//        return this.meteoDataHandler.getWindDataListByObservationTime(observation);
//    }

    public void download(){
        this.meteoDataHandler.download();
    }

    public Boolean isEmpty(){
        return this.meteoDataHandler.isEmpty();
    }

//    public Boolean init(Region region) throws JsonProcessingException {
//        return this.meteoDataHandler.init(region);
//    }

    public List<String> getMeteoJsonList() throws JsonProcessingException {
        return this.meteoDataHandler.getMeteoJsonList();
    }

    public List<String> getValidationList(){
        return this.meteoDataHandler.getValidationList();
    }

    public List<String> getObservationList(){
        return this.meteoDataHandler.getObservationList();
    }

    public List<String> getMeteoParamsList(){
        return this.meteoDataHandler.getMeteoParamsList();
    }

    @Override
    public String toString() {
        return "MeteoForecastService{" +
                "meteoHandler=" + this.meteoDataHandler +
                '}';
    }
}
