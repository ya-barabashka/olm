package application.rest.handler;

import application.rest.meteoentity.*;

import java.util.List;

public class GeneralWindInf {

    private List<UComponentOfWind> uComponentOfWindList;
    private List<VComponentOfWind> vComponentOfWindList;
    private List<Temperature> temperatureList;
    private List<CloudCover> cloudList;
    private List<RelativeHumidity> humidityList;

    public GeneralWindInf() {
    }

    public GeneralWindInf(List<UComponentOfWind> uComponentOfWindList, List<VComponentOfWind> vComponentOfWindList, List<Temperature> temperatureList, List<CloudCover> cloudList, List<RelativeHumidity> humidityList) {
        this.uComponentOfWindList = uComponentOfWindList;
        this.vComponentOfWindList = vComponentOfWindList;
        this.temperatureList = temperatureList;
        this.cloudList = cloudList;
        this.humidityList = humidityList;
    }

    public List<UComponentOfWind> getUComponentOfWindList() {
        return uComponentOfWindList;
    }

    public void setUComponentOfWindList(List<UComponentOfWind> uComponentOfWindList) {
        this.uComponentOfWindList = uComponentOfWindList;
    }

    public List<VComponentOfWind> getVComponentOfWindList() {
        return vComponentOfWindList;
    }

    public void setVComponentOfWindList(List<VComponentOfWind> vComponentOfWindList) {
        this.vComponentOfWindList = vComponentOfWindList;
    }

    public List<Temperature> getTemperatureList() {
        return temperatureList;
    }

    public void setTemperatureList(List<Temperature> temperatureList) {
        this.temperatureList = temperatureList;
    }

    public List<CloudCover> getCloudList() {
        return cloudList;
    }

    public void setCloudList(List<CloudCover> cloudList) {
        this.cloudList = cloudList;
    }

    public List<RelativeHumidity> getHumidityList() {
        return humidityList;
    }

    public void setHumidityList(List<RelativeHumidity> humidityList) {
        this.humidityList = humidityList;
    }

    @Override
    public String toString() {
        return "uComponentOfWindList=" + uComponentOfWindList + ", "
                + "vComponentOfWindList=" + vComponentOfWindList + ", "
                + "temperatureList=" + temperatureList + ", "
                + "cloudList=" + cloudList + ", "
                + "humidityList=" + humidityList;
    }

}
