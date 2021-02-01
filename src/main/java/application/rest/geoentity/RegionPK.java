package application.rest.geoentity;

import java.util.Objects;

public class RegionPK {

    private String region;
    private Integer observation;
    private Integer forecast;
    private Double level;

    public RegionPK() {
    }

    public RegionPK(String region, Integer observation, Integer forecast, Double level) {
        this.region = region;
        this.observation = observation;
        this.forecast = forecast;
        this.level = level;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getObservation() {
        return observation;
    }

    public void setObservation(Integer observation) {
        this.observation = observation;
    }

    public Integer getForecast() {
        return forecast;
    }

    public void setForecast(Integer forecast) {
        this.forecast = forecast;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionPK)) return false;
        RegionPK regionPK = (RegionPK) o;
        return Objects.equals(getRegion(), regionPK.getRegion()) &&
                Objects.equals(getObservation(), regionPK.getObservation()) &&
                Objects.equals(getForecast(), regionPK.getForecast()) &&
                Objects.equals(getLevel(), regionPK.getLevel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegion(), getObservation(), getForecast(), getLevel());
    }

    @Override
    public String toString() {
        return "RegionPK{" +
                "region='" + region + '\'' +
                ", observation=" + observation +
                ", forecast=" + forecast +
                ", level=" + level +
                '}';
    }
}

