package application.rest.service;

import application.rest.handler.LocationHandler;
import ucar.ma2.InvalidRangeException;

import java.io.IOException;

public class LocationService {

    private LocationHandler LocationHandler;

    public LocationService(String observationHour, String validationHour) throws IOException, InterruptedException, InvalidRangeException {
        this.LocationHandler = new LocationHandler(observationHour, validationHour);
    }

    public application.rest.handler.LocationHandler getLocationHandler() {
        return LocationHandler;
    }

    public void setLocationHandler(application.rest.handler.LocationHandler locationHandler) {
        LocationHandler = locationHandler;
    }

    public void initBoundariesFor(Integer time, Double level){
        this.LocationHandler.initBoundariesFor(time, level);
    }

    @Override
    public String toString() {
        return "LocationService{" +
                "locationHandler=" + this.LocationHandler +
                '}';
    }
}
