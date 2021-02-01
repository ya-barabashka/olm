package application.rest.location;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private List<Coordinate> locations = new ArrayList<>();

    public Location() {
    }

    public Location(List<Coordinate> locations) {
        this.locations = locations;
    }

    public void add(Coordinate point){
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

    public Coordinate getUpperRightSideCoord(){
        return locations.get(0);
    }

    public Coordinate getLowerRightSideCoord(){
        return locations.get(1);
    }

    public Coordinate getUpperLeftSideCoord(){
        return locations.get(2);
    }

    public Coordinate getLowerLeftSideCoord(){
        return locations.get(3);
    }

    // ------------------------------------------------ //

    public Coordinate getBottomLeftSideCoord(){
        return locations.get(0);
    }

    public Coordinate getBottomRightSideCoord(){
        return locations.get(1);
    }

    public Coordinate getTopLeftSideCoord(){
        return locations.get(2);
    }

    public Coordinate getTopRightSideCoord(){
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

