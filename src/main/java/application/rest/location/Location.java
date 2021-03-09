package application.rest.location;

import ucar.unidata.geoloc.LatLonPoint;
import ucar.unidata.geoloc.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private List<LatLonPoint> locations = new ArrayList<>();

    public Location() {
    }

    public Location(List<LatLonPoint> locations) {
        this.locations = locations;
    }

    public void add(LatLonPoint point){
        this.locations.add(point);
    }

    public boolean existsUpperLeftSideCoord(){
        return getUpperLeftSideCoord()!=null;
    }

    public boolean existsUpperRightSideCoord(){
        return getUpperRightSideCoord()!=null;
    }

    public boolean existsLowerLeftSideCoord(){
        return getLowerLeftSideCoord()!=null;
    }

    public boolean existsLowerRightSideCoord(){
        return getLowerRightSideCoord()!=null;
    }

    public boolean existsBottomLeftSideCoord(){
        return getBottomLeftSideCoord()!=null;
    }

    public boolean existsBottomRightSideCoord(){
        return getBottomRightSideCoord()!=null;
    }

    public boolean existsTopLeftSideCoord(){
        return getTopLeftSideCoord()!=null;
    }

    public boolean existsTopRightSideCoord(){
        return getTopRightSideCoord()!=null;
    }

    public LatLonPoint getUpperRightSideCoord(){
        return locations.get(0);
    }

    public LatLonPoint getLowerRightSideCoord(){
        return locations.get(1);
    }

    public LatLonPoint getUpperLeftSideCoord(){
        return locations.get(2);
    }

    public LatLonPoint getLowerLeftSideCoord(){
        return locations.get(3);
    }

    // ------------------------------------------------ //

    public LatLonPoint getBottomLeftSideCoord(){
        return locations.get(0);
    }

    public LatLonPoint getBottomRightSideCoord(){
        return locations.get(1);
    }

    public LatLonPoint getTopLeftSideCoord(){
        return locations.get(2);
    }

    public LatLonPoint getTopRightSideCoord(){
        return locations.get(3);
    }

    // ------------------------------------------------ //

    public int size(){
        return this.locations.size();
    }

    @Override
    public String toString() {
        return "locations=" + locations;
    }

}

