package application.rest.geoentity;

import application.rest.handler.WeatherData;
import ucar.unidata.geoloc.LatLonPoint;
import ucar.unidata.geoloc.LatLonPointImpl;
import ucar.unidata.geoloc.LatLonRect;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private String name;
    private LatLonPointImpl geoCenterPoint;
    private LatLonRect rectangularBoundaries;
    private List<LatLonPoint> arbitraryBoundaries;

    public Region(String name, LatLonPointImpl geoCenterPoint, LatLonRect rectangularBoundaries, List<LatLonPoint> arbitraryBoundaries) {
        this.name = name;
        this.geoCenterPoint = geoCenterPoint;
        this.rectangularBoundaries = rectangularBoundaries;
        this.arbitraryBoundaries = arbitraryBoundaries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLonPoint getGeoCenterPoint() {
        return geoCenterPoint;
    }

    public void setGeoCenterPoint(LatLonPointImpl geoCenterPoint) {
        this.geoCenterPoint = geoCenterPoint;
    }

    public LatLonRect getRectangularBoundaries() {
        return rectangularBoundaries;
    }

    public void setRectangularBoundaries(LatLonRect rectangularBoundaries) {
        this.rectangularBoundaries = rectangularBoundaries;
    }

    public List<LatLonPoint> getArbitraryBoundaries() {
        return arbitraryBoundaries;
    }

    public void setArbitraryBoundaries(List<LatLonPoint> arbitraryBoundaries) {
        this.arbitraryBoundaries = arbitraryBoundaries;
    }

    @Override
    public String toString() {
        return "Region{" +
                "name='" + name + '\'' +
                ", geoCenterPoint=" + geoCenterPoint +
                ", rectangularBoundaries=" + rectangularBoundaries +
                ", arbitraryBoundaries=" + arbitraryBoundaries +
                '}';
    }

    //    private static List<WeatherData> cherkasyRegion = new ArrayList<>();
//    private static List<WeatherData> chernihivRegion = new ArrayList<>();
//    private static List<WeatherData> chernivtsiRegion = new ArrayList<>();
//    private static List<WeatherData> dnipropetrovskRegion = new ArrayList<>();
//    private static List<WeatherData> donetskRegion = new ArrayList<>();
//    private static List<WeatherData> ivanofrankivskRegion = new ArrayList<>();
//    private static List<WeatherData> kharkivRegion = new ArrayList<>();
//    private static List<WeatherData> khersonRegion = new ArrayList<>();
//    private static List<WeatherData> khmelnytskyiRegionl = new ArrayList<>();
//    private static List<WeatherData> kievRegion = new ArrayList<>();
//    private static List<WeatherData> kirovohradRegion = new ArrayList<>();
//    private static List<WeatherData> luhanskRegion = new ArrayList<>();
//    private static List<WeatherData> lvivRegion = new ArrayList<>();
//    private static List<WeatherData> mykolaivRegion = new ArrayList<>();
//    private static List<WeatherData> odessaRegion = new ArrayList<>();
//    private static List<WeatherData> poltavaRegion = new ArrayList<>();
//    private static List<WeatherData> rivneRegion = new ArrayList<>();
//    private static List<WeatherData> sumyRegion = new ArrayList<>();
//    private static List<WeatherData> ternopilRegion = new ArrayList<>();
//    private static List<WeatherData> vinnytsiaRegion = new ArrayList<>();
//    private static List<WeatherData> volynRegion = new ArrayList<>();
//    private static List<WeatherData> zakarpattiaRegion = new ArrayList<>();
//    private static List<WeatherData> zaporizhiaRegion = new ArrayList<>();
//    private static List<WeatherData> zhytomyrRegion = new ArrayList<>();
//
//    public Region() {
//    }
//
//    public static List<WeatherData> getCherkasyRegion() {
//        return cherkasyRegion;
//    }
//
//    public static void setCherkasyRegion(List<WeatherData> cherkasyRegion) {
//        Region.cherkasyRegion = cherkasyRegion;
//    }
//
//    public static List<WeatherData> getChernihivRegion() {
//        return chernihivRegion;
//    }
//
//    public static void setChernihivRegion(List<WeatherData> chernihivRegion) {
//        Region.chernihivRegion = chernihivRegion;
//    }
//
//    public static List<WeatherData> getChernivtsiRegion() {
//        return chernivtsiRegion;
//    }
//
//    public static void setChernivtsiRegion(List<WeatherData> chernivtsiRegion) {
//        Region.chernivtsiRegion = chernivtsiRegion;
//    }
//
//    public static List<WeatherData> getDnipropetrovskRegion() {
//        return dnipropetrovskRegion;
//    }
//
//    public static void setDnipropetrovskRegion(List<WeatherData> dnipropetrovskRegion) {
//        Region.dnipropetrovskRegion = dnipropetrovskRegion;
//    }
//
//    public static List<WeatherData> getDonetskRegion() {
//        return donetskRegion;
//    }
//
//    public static void setDonetskRegion(List<WeatherData> donetskRegion) {
//        Region.donetskRegion = donetskRegion;
//    }
//
//    public static List<WeatherData> getIvanofrankivskRegion() {
//        return ivanofrankivskRegion;
//    }
//
//    public static void setIvanofrankivskRegion(List<WeatherData> ivanofrankivskRegion) {
//        Region.ivanofrankivskRegion = ivanofrankivskRegion;
//    }
//
//    public static List<WeatherData> getKharkivRegion() {
//        return kharkivRegion;
//    }
//
//    public static void setKharkivRegion(List<WeatherData> kharkivRegion) {
//        Region.kharkivRegion = kharkivRegion;
//    }
//
//    public static List<WeatherData> getKhersonRegion() {
//        return khersonRegion;
//    }
//
//    public static void setKhersonRegion(List<WeatherData> khersonRegion) {
//        Region.khersonRegion = khersonRegion;
//    }
//
//    public static List<WeatherData> getKhmelnytskyiRegionl() {
//        return khmelnytskyiRegionl;
//    }
//
//    public static void setKhmelnytskyiRegionl(List<WeatherData> khmelnytskyiRegionl) {
//        Region.khmelnytskyiRegionl = khmelnytskyiRegionl;
//    }
//
//    public static List<WeatherData> getKievRegion() {
//        return kievRegion;
//    }
//
//    public static void setKievRegion(List<WeatherData> kievRegion) {
//        Region.kievRegion = kievRegion;
//    }
//
//    public static List<WeatherData> getKirovohradRegion() {
//        return kirovohradRegion;
//    }
//
//    public static void setKirovohradRegion(List<WeatherData> kirovohradRegion) {
//        Region.kirovohradRegion = kirovohradRegion;
//    }
//
//    public static List<WeatherData> getLuhanskRegion() {
//        return luhanskRegion;
//    }
//
//    public static void setLuhanskRegion(List<WeatherData> luhanskRegion) {
//        Region.luhanskRegion = luhanskRegion;
//    }
//
//    public static List<WeatherData> getLvivRegion() {
//        return lvivRegion;
//    }
//
//    public static void setLvivRegion(List<WeatherData> lvivRegion) {
//        Region.lvivRegion = lvivRegion;
//    }
//
//    public static List<WeatherData> getMykolaivRegion() {
//        return mykolaivRegion;
//    }
//
//    public static void setMykolaivRegion(List<WeatherData> mykolaivRegion) {
//        Region.mykolaivRegion = mykolaivRegion;
//    }
//
//    public static List<WeatherData> getOdessaRegion() {
//        return odessaRegion;
//    }
//
//    public static void setOdessaRegion(List<WeatherData> odessaRegion) {
//        Region.odessaRegion = odessaRegion;
//    }
//
//    public static List<WeatherData> getPoltavaRegion() {
//        return poltavaRegion;
//    }
//
//    public static void setPoltavaRegion(List<WeatherData> poltavaRegion) {
//        Region.poltavaRegion = poltavaRegion;
//    }
//
//    public static List<WeatherData> getRivneRegion() {
//        return rivneRegion;
//    }
//
//    public static void setRivneRegion(List<WeatherData> rivneRegion) {
//        Region.rivneRegion = rivneRegion;
//    }
//
//    public static List<WeatherData> getSumyRegion() {
//        return sumyRegion;
//    }
//
//    public static void setSumyRegion(List<WeatherData> sumyRegion) {
//        Region.sumyRegion = sumyRegion;
//    }
//
//    public static List<WeatherData> getTernopilRegion() {
//        return ternopilRegion;
//    }
//
//    public static void setTernopilRegion(List<WeatherData> ternopilRegion) {
//        Region.ternopilRegion = ternopilRegion;
//    }
//
//    public static List<WeatherData> getVinnytsiaRegion() {
//        return vinnytsiaRegion;
//    }
//
//    public static void setVinnytsiaRegion(List<WeatherData> vinnytsiaRegion) {
//        Region.vinnytsiaRegion = vinnytsiaRegion;
//    }
//
//    public static List<WeatherData> getVolynRegion() {
//        return volynRegion;
//    }
//
//    public static void setVolynRegion(List<WeatherData> volynRegion) {
//        Region.volynRegion = volynRegion;
//    }
//
//    public static List<WeatherData> getZakarpattiaRegion() {
//        return zakarpattiaRegion;
//    }
//
//    public static void setZakarpattiaRegion(List<WeatherData> zakarpattiaRegion) {
//        Region.zakarpattiaRegion = zakarpattiaRegion;
//    }
//
//    public static List<WeatherData> getZaporizhiaRegion() {
//        return zaporizhiaRegion;
//    }
//
//    public static void setZaporizhiaRegion(List<WeatherData> zaporizhiaRegion) {
//        Region.zaporizhiaRegion = zaporizhiaRegion;
//    }
//
//    public static List<WeatherData> getZhytomyrRegion() {
//        return zhytomyrRegion;
//    }
//
//    public static void setZhytomyrRegion(List<WeatherData> zhytomyrRegion) {
//        Region.zhytomyrRegion = zhytomyrRegion;
//    }

}

