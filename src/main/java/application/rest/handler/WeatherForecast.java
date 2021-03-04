package application.rest.handler;

import java.util.List;

public class WeatherForecast {
    private String currentObservationTime;
    private String currentValidationTime;
    private List<String> currentMeteoParams;

    public WeatherForecast(){}

    public WeatherForecast(String currentObservationTime, String currentValidationTime, List<String> currentMeteoParams) {
        this.currentObservationTime = currentObservationTime;
        this.currentValidationTime = currentValidationTime;
        this.currentMeteoParams = currentMeteoParams;
    }

    public String getCurrentObservationTime() {
        return currentObservationTime;
    }

    public void setCurrentObservationTime(String currentObservationTime) {
        this.currentObservationTime = currentObservationTime;
    }

    public String getCurrentValidationTime() {
        return currentValidationTime;
    }

    public void setCurrentValidationTime(String currentValidationTime) {
        this.currentValidationTime = currentValidationTime;
    }

    public List<String> getCurrentMeteoParams() {
        return currentMeteoParams;
    }

    public void setCurrentMeteoParams(List<String> currentMeteoParams) {
        this.currentMeteoParams = currentMeteoParams;
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "currentObservationTime='" + currentObservationTime + '\'' +
                ", currentValidationTime='" + currentValidationTime + '\'' +
                ", currentMeteoParams=" + currentMeteoParams +
                '}';
    }
}
