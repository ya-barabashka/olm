package application.rest.location;

public class Coordinate implements Comparable<Coordinate> {

    private Double latitude;
    private Double longitude;

    public Coordinate() {
    }

    public Coordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int compareTo(Coordinate o) {
        return Double.compare(getCompareKey(), o.getCompareKey());
    }

    private Double getCompareKey() {
        return (getLatitude() * getLatitude()) + (getLongitude() * getLongitude());
    }

}

