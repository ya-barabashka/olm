package application.rest.service;

import ucar.ma2.InvalidRangeException;

import java.io.IOException;

public class MeteoForecastLocationService {

    private MeteoForecastService meteoForecastService;
    private LocationService locationService;

    public MeteoForecastLocationService(String observationHour, String validationHour) throws IOException, InterruptedException, InvalidRangeException {
        this.meteoForecastService = new MeteoForecastService(observationHour, validationHour);
        this.locationService = new LocationService(observationHour, validationHour);
    }

    public MeteoForecastService getMeteoForecastService() {
        return meteoForecastService;
    }

    public void setMeteoForecastService(MeteoForecastService meteoForecastService) {
        this.meteoForecastService = meteoForecastService;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public String toString() {
        return "MeteoForecastLocationService{" +
                "meteoForecastService=" + meteoForecastService +
                ", locationService=" + locationService +
                '}';
    }
}
