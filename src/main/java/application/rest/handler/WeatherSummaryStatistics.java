package application.rest.handler;

import java.util.DoubleSummaryStatistics;

public class WeatherSummaryStatistics {

    private DoubleSummaryStatistics temperatureStatistics;
    private DoubleSummaryStatistics humidityStatistics;
    private DoubleSummaryStatistics cloudnessStatistics;
    private DoubleSummaryStatistics windSpeedStatistics;

    public WeatherSummaryStatistics() {
    }

    public WeatherSummaryStatistics(DoubleSummaryStatistics temperatureStatistics, DoubleSummaryStatistics humidityStatistics, DoubleSummaryStatistics cloudnessStatistics, DoubleSummaryStatistics windSpeedStatistics) {
        this.temperatureStatistics = temperatureStatistics;
        this.humidityStatistics = humidityStatistics;
        this.cloudnessStatistics = cloudnessStatistics;
        this.windSpeedStatistics = windSpeedStatistics;
    }

    public DoubleSummaryStatistics getTemperatureStatistics() {
        return temperatureStatistics;
    }

    public void setTemperatureStatistics(DoubleSummaryStatistics temperatureStatistics) {
        this.temperatureStatistics = temperatureStatistics;
    }

    public DoubleSummaryStatistics getHumidityStatistics() {
        return humidityStatistics;
    }

    public void setHumidityStatistics(DoubleSummaryStatistics humidityStatistics) {
        this.humidityStatistics = humidityStatistics;
    }

    public DoubleSummaryStatistics getCloudnessStatistics() {
        return cloudnessStatistics;
    }

    public void setCloudnessStatistics(DoubleSummaryStatistics cloudnessStatistics) {
        this.cloudnessStatistics = cloudnessStatistics;
    }

    public DoubleSummaryStatistics getWindSpeedStatistics() {
        return windSpeedStatistics;
    }

    public void setWindSpeedStatistics(DoubleSummaryStatistics windSpeedStatistics) {
        this.windSpeedStatistics = windSpeedStatistics;
    }

    @Override
    public String toString() {
        return "WeatherSummaryStatistics{" +
                "temperatureStatistics=" + temperatureStatistics +
                ", humidityStatistics=" + humidityStatistics +
                ", cloudnessStatistics=" + cloudnessStatistics +
                ", windSpeedStatistics=" + windSpeedStatistics +
                '}';
    }
}

