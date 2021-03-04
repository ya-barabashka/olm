package application.rest.handler;

import application.rest.meteoentity.*;
import ucar.ma2.*;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GeoGrid;
import ucar.nc2.dt.grid.GridDataset;
import ucar.unidata.io.RandomAccessFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ucar.unidata.geoloc.LatLonRect;
import ucar.unidata.geoloc.LatLonPoint;
import ucar.unidata.geoloc.LatLonPointImpl;

public class MeteoDataHandler {

    public List observation = Arrays. asList("00", "06", "12", "18");
    public List validation = new ArrayList<String>();
    public List meteoParams = Arrays.asList("Pressure_reduced_to_MSL_msl", "Relative_humidity_isobaric", "Total_cloud_cover_isobaric", "Temperature_isobaric", "u-component_of_wind_isobaric", "v-component_of_wind_isobaric");
    public List altitudes = Arrays. asList("100000", "97500", "9500", "92500");
//    private String pattern = "yyyy-MM-dd HH:mm:ss z";
    private String pattern = "yyyyMMdd";
    private String FILE_BASE_URL = "https://nomads.ncep.noaa.gov/pub/data/nccf/com/gfs/prod";
    private String FILE_COMPOUND_URL;
    private String FILE_NAME;
    public String FILE_LOCAL_PATH = "src/main/resources/grib2";
    private String TEMPERATURE = "Temperature_isobaric";
    private String RELATIVE_HUMIDITY = "Relative_humidity_isobaric";
    private String V_COMPONENT = "v-component_of_wind_isobaric";
    private String U_COMPONENT = "u-component_of_wind_isobaric";
    private String CLOUD_COVER = "Total_cloud_cover_isobaric";
    private String PRESS_TO_MSL = "Pressure_reduced_to_MSL_msl";

    private String observationHour;
    private String validationHour;
    private String altitude;

    public MeteoDataHandler(){}

    public MeteoDataHandler(String observationHour, String validationHour) {
        this.observationHour = observationHour;
        this.validationHour = getCorrectedValidationHour(validationHour);
        this.altitude = "100000";
        this.FILE_COMPOUND_URL = getCompoundURLPath(observationHour);
        this.FILE_NAME = getFileName(observationHour, validationHour);
        this.FILE_BASE_URL = FILE_BASE_URL + "/" + FILE_COMPOUND_URL + "/" + FILE_NAME;
    }

    public File getGrib2FileFullPath(){
        return new File(this.FILE_LOCAL_PATH + "/" + this.FILE_NAME + ".grib2");
    }

    private RandomAccessFile getRandomAccessFile(String grib2file) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(grib2file, "r");
            randomAccessFile.order(RandomAccessFile.BIG_ENDIAN);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return randomAccessFile;
    }

    private Boolean checkMeteoDataInitialization(){
        return  RelativeHumidity.getHumidityMap().keySet().size() == CloudCover.getCloudMap().keySet().size() &&
                CloudCover.getCloudMap().keySet().size() == Temperature.getTemperatureMap().keySet().size() &&
                Temperature.getTemperatureMap().keySet().size() == UComponentOfWind.getUComponentMap().keySet().size() &&
                UComponentOfWind.getUComponentMap().keySet().size() == VComponentOfWind.getVComponentMap().keySet().size() &&
                VComponentOfWind.getVComponentMap().keySet().size() == RelativeHumidity.getHumidityMap().keySet().size() &&

                RelativeHumidity.getHumidityMap().entrySet().size() == CloudCover.getCloudMap().entrySet().size() &&
                CloudCover.getCloudMap().entrySet().size() == Temperature.getTemperatureMap().entrySet().size() &&
                Temperature.getTemperatureMap().entrySet().size() == UComponentOfWind.getUComponentMap().entrySet().size() &&
                UComponentOfWind.getUComponentMap().entrySet().size() == VComponentOfWind.getVComponentMap().entrySet().size() &&
                VComponentOfWind.getVComponentMap().entrySet().size() == RelativeHumidity.getHumidityMap().entrySet().size() &&

                !(RelativeHumidity.getHumidityMap().keySet().isEmpty() &&
                        CloudCover.getCloudMap().keySet().isEmpty() &&
                        Temperature.getTemperatureMap().keySet().isEmpty() &&
                        UComponentOfWind.getUComponentMap().keySet().isEmpty() &&
                        VComponentOfWind.getVComponentMap().keySet().isEmpty()) ||

                !(RelativeHumidity.getHumidityMap().entrySet().isEmpty() &&
                        CloudCover.getCloudMap().entrySet().isEmpty() &&
                        Temperature.getTemperatureMap().entrySet().isEmpty() &&
                        UComponentOfWind.getUComponentMap().entrySet().isEmpty() &&
                        VComponentOfWind.getVComponentMap().entrySet().isEmpty());
    }

//    private List<Float> getFloatValues(float[] values){
//        List<Float> listOfFloats = new ArrayList<Float>();
//        for(float value : values){
//            listOfFloats.add(Float.valueOf(value));
//        }
//        return listOfFloats;
//    }

    public Boolean isEmpty(){
        List<File> grib2files = getFilesList(new File(FILE_LOCAL_PATH));
        return grib2files.size() == 0;
    }

    private List<File> getFilesList(File folder){
        File[] folderEntries = folder.listFiles();
        List<File> files = new ArrayList<>();
        for (File entry : folderEntries) {
            if (entry.isDirectory()) {
                List<File> subFolderEntries = getFilesList(entry);
                subFolderEntries.stream().filter((subEntry) -> (subEntry.isFile())).forEachOrdered((subEntry) -> {
                    files.add(subEntry);
                });
            }
            if(entry.isFile() && entry.getName().endsWith("grib2")){
                files.add(entry);
            }
        }
        return files;
    }

    private void initWithParam(String paramName, GridDatatype gridDatatype){

        try {
            Array data = gridDatatype.readDataSlice(-1, -1, -1, -1);
            GridCoordSystem gcs2 = gridDatatype.getCoordinateSystem();

            IndexIterator iter = data.getIndexIterator();
            switch(paramName){
                case "Temperature_isobaric": {
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        new Temperature(
                                new WeatherPK(
                                        point.getLatitude(),
                                        to_180(point.getLongitude()),
                                        Double.valueOf("33"),
                                        Integer.valueOf(this.observationHour),
                                        Integer.valueOf(this.validationHour)),
                                value);
                    }
                    break;
                }
                case "Relative_humidity_isobaric":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        new RelativeHumidity(
                                new WeatherPK(
                                        point.getLatitude(),
                                        to_180(point.getLongitude()),
                                        Double.valueOf("30"),
                                        Integer.valueOf(this.observationHour),
                                        Integer.valueOf(this.validationHour)),
                                value);
                    }
                    break;
                }
                case "v-component_of_wind_isobaric":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        new VComponentOfWind(
                                new WeatherPK(
                                        point.getLatitude(),
                                        to_180(point.getLongitude()),
                                        Double.valueOf("30"),
                                        Integer.valueOf(this.observationHour),
                                        Integer.valueOf(this.validationHour)),
                                value);
                    }
                    break;
                }
                case "u-component_of_wind_isobaric":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        new UComponentOfWind(
                                new WeatherPK(
                                        point.getLatitude(),
                                        to_180(point.getLongitude()),
                                        Double.valueOf("30"),
                                        Integer.valueOf(this.observationHour),
                                        Integer.valueOf(this.validationHour)),
                                value);
                    }
                    break;
                }
                case "Total_cloud_cover_isobaric":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
                        new CloudCover(
                                new WeatherPK(
                                        point.getLatitude(),
                                        to_180(point.getLongitude()),
                                        Double.valueOf("21"),
                                        Integer.valueOf(this.observationHour),
                                        Integer.valueOf(this.validationHour)),
                                value);
                    }
                    break;
                }
                case "Pressure_reduced_to_MSL_msl":{
                    while(iter.hasNext()) {
                        float value = iter.getFloatNext();
                        int counter [] = iter.getCurrentCounter();
                        LatLonPoint point = gcs2.getLatLon(counter[2], counter[1]);
                        new PressureToMSL(
                                new WeatherPK(
                                        point.getLatitude(),
                                        to_180(point.getLongitude()),
                                        Double.valueOf("0"),
                                        Integer.valueOf(this.observationHour),
                                        Integer.valueOf(this.validationHour)),
                                value);
                    }
                    break;
                }
                default:{

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initWithParamForBBox(String paramName, LatLonPoint point1, LatLonPoint point2){

        try (GridDataset dataset = GridDataset.open(FILE_LOCAL_PATH + "/" + FILE_NAME + ".grib2")) {
            GeoGrid grid = dataset.findGridByName(paramName);
            GridCoordSystem gcs = grid.getCoordinateSystem();

//            System.out.println("original bbox= " + gcs.getBoundingBox());
//            System.out.println("lat/lon bbox= " + gcs.getLatLonBoundingBox());

            LatLonRect llbb = gcs.getLatLonBoundingBox();
            // point1 = new LatLonPointImpl(25.0, 30.0);
            // point2 = new LatLonPointImpl(79.0, 69.0);
            LatLonRect llbb_subset = new LatLonRect(point1, point2);
//            System.out.println("subset lat/lon bbox= " + llbb_subset);

            List<Range>ranges = gcs.getRangesFromLatLonRect(llbb_subset);

//            for(Range range : ranges){
//                System.out.println("range: " + " first: " + range.first() + " last: " + range.last());
//            }

            GridDatatype gridDatatype = grid.subset(null, null, ranges.get(0), ranges.get(1));
//            Array data = gridDatatype.readDataSlice(-1, -1, -1, -1);
//            GridCoordSystem gcs2 = gridDatatype.getCoordinateSystem();

            // -----------------------------------------------

//            System.out.println("Y increment      : " + ((CoordinateAxis1D) gcs.getYHorizAxis()).getIncrement());
//            LatLonPoint p = gcs.getLatLon(0, 0);
//            System.out.println("index (0,0) --> lat/lon : " + p.getLatitude() + " ; " + p.getLongitude());
//            p = gcs.getLatLon(1, 1);
//            System.out.println("index (1,1) --> lat/lon : " + p.getLatitude() + " ; " + p.getLongitude());
//            System.out.println("Y increment      : " + ((CoordinateAxis1D) gcs2.getYHorizAxis()).getIncrement());
//            LatLonPoint p = gcs2.getLatLon(0, 0);
//            System.out.println("index (0,0) --> lat/lon : " + p.getLatitude() + " ; " + p.getLongitude());
//            p = gcs2.getLatLon(1, 1);
//            System.out.println("index (1,1) --> lat/lon : " + p.getLatitude() + " ; " + p.getLongitude());
//            p = gcs2.getLatLon(156, 216);
//            System.out.println("index (1,1) --> lat/lon : " + p.getLatitude() + " ; " + p.getLongitude() );

            // -----------------------------------------------

            initWithParam(paramName, gridDatatype);


//            IndexIterator iter = data.getIndexIterator();
//            for(int i = 0; i < ranges.get(1).last() - ranges.get(1).first(); i++){
//                for(int j = 0; j < ranges.get(0).last() - ranges.get(0).first(); j++){
//                    float value = iter.getFloatNext();
//                    int counter [] = iter.getCurrentCounter();
//                    for(int c: counter) {
//                        System.out.println("c: " + c + " sz: " + counter.length );
//                    }
//                    LatLonPoint point = gcs2.getLatLon(counter[2], counter[1]);
//                    new PressureToMSL(new WeatherPK(point.getLatitude(), to_180(point.getLongitude()), Double.valueOf(this.altitude), Integer.valueOf(this.observationHour), Integer.valueOf(this.validationHour)), value);
//                    System.out.println( "Pressure = "+ value + " lat: " + point.getLatitude() + " lon: " + point.getLongitude() + " " + " data sz: " + data.getSize() );
//                }
//            }


//            IndexIterator iter = data.getIndexIterator();
//            while(iter.hasNext()) {
//                float value = iter.getFloatNext();
//                int counter [] = iter.getCurrentCounter();
//                for(int c: counter) System.out.println("c: " + c + " sz: " + counter.length + " data: " + data.getDouble(215));
//                LatLonPoint point = gcs2.getLatLon(counter[3], counter[2]);
//                new PressureToMSL(new WeatherPK(point.getLatitude(), to_180(point.getLongitude()), Double.valueOf(this.altitude), Integer.valueOf(this.observationHour), Integer.valueOf(this.validationHour)), value);
//                System.out.println( "Pressure = "+ value + " lat: " + point.getLatitude() + " lon: " + point.getLongitude() + " " + counter[2] + " " + counter[3] + " data sz: " + data.getSize() );
//            }


//            System.out.println("ranges (0) -> lat: " + " first: " + ranges.get(0).first() + " last: " + ranges.get(0).last() + " delta: " + (ranges.get(0).last() - ranges.get(0).first()));


//            System.out.println("result lat/lon bbox= " + gcs2.getLatLonBoundingBox());
//            System.out.println("result bbox= " + gcs2.getBoundingBox());
//
//            ProjectionRect pr = gcs2.getProjection().getDefaultMapArea();
//            System.out.println("projection mapArea= " + pr);
        } catch (IOException | InvalidRangeException e) {
            e.printStackTrace();
        }

    }

    public Boolean init() throws IOException, InterruptedException, InvalidRangeException {

        long startTime = System.nanoTime();

        for(String param: getMeteoParamsList()){
            initWithParamForBBox(
                    param,
                    new LatLonPointImpl(25.0, 30.0),
                    new LatLonPointImpl(79.0, 69.0));
        }

//        System.out.println("temp array: " + temperature.getSize());

//        try(RandomAccessFile randomAccessFile = new RandomAccessFile(FILE_LOCAL_PATH + "/" + FILE_NAME + ".grib2", "r")) {
//            Grib2RecordScanner scan = new Grib2RecordScanner(randomAccessFile);
//            while (scan.hasNext()) {
//                ucar.nc2.grib.grib2.Grib2Record record = scan.next();
//                ucar.nc2.grib.grib2.Grib2SectionDataRepresentation dataRepresentationSection = record.getDataRepresentationSection();
//                ucar.nc2.grib.grib2.Grib2SectionIdentification id = record.getId();
//                Grib2SectionIndicator is = record.getIs();
//                ucar.nc2.grib.grib2.Grib2Pds pdsv = record.getPDS();
//                ucar.nc2.grib.grib2.Grib2Gds gdsv = record.getGDS();
//                float [] values = record.readData(randomAccessFile, dataRepresentationSection.getStartingPosition());
//
//                final GridDataset gridDataSet = GridDataset.open(FILE_LOCAL_PATH + "/" + FILE_NAME + ".grib2");
//                final GridDatatype pwd = gridDataSet.findGridDatatype("Temperature_isobaric");
//
////                System.out.println("Info : " + pwd.getInfo());
//
////                for(Dimension dim : pwd.getDimensions()){
////                    System.out.println("dim: " + dim);
////                }
//
//                final GridCoordSystem pwdGcs = pwd.getCoordinateSystem();
//
//                int []start_result = new int [2];
//                int []end_result = new int [2];
//
//                // xy[0] = x, xy[1] = y
//                int[] start_xy = pwdGcs.findXYindexFromLatLon( 25.0, -30.0, start_result );
//                int[] end_xy = pwdGcs.findXYindexFromLatLon( 79.0, 69.0, end_result );
//
//
//
//                System.out.println(
//                        "start_idx y "+ start_xy[1] + " " +
//                        "start_idx x " + start_xy[0] + " " +
//                        "end_idx y " + end_xy[1] + " " +
//                        "end_idx x " + end_xy[0]);
//
//                // Extract data value for time 0, no Vert index, Lat index, Lon index
//                // note t, z, y, x
////                double val = data.getDouble(0);
//
//                System.out.println(
//                        " T: " + pwd.getTimeDimensionIndex() + " " +
//                        " Z: " + pwd.getZDimensionIndex() +  " " +
//                        " Y: " + pwd.getYDimensionIndex() + " " +
//                        " X: " + pwd.getXDimensionIndex());
//
//                System.out.println("rank: " + pwd.getRank());
//
//                Array data = pwd.readDataSlice(1, 33, 44, 276, 260, 120);
//
////                Range range = new Range();
////                GridDatatype gridDatatype = pwd. // makeSubset(1, 33, rect, 1, 1, 1);
//
////                List<Dimension> dimentions = pwd.getDimensions();
////                for(Dimension dim: dimentions){
////                    System.out.println("Dim: " + dim);
////                }
//
//                IndexIterator iter = data.getIndexIterator();
//                while(iter.hasNext()) {
//                    float val = iter.getFloatNext();
//                    System.out.println( "Temperature = "+ val);
//                }
//
//                int[] ntps = gdsv.getNptsInLine();
//                int drs = dataRepresentationSection.getDataPoints();
//
//                CalendarDate calendarDate = record.getReferenceDate();
//                int dataPoints = dataRepresentationSection.getDataPoints();
//                int dataTemplate = dataRepresentationSection.getDataTemplate();
//                CalendarDate idCalendarDate = id.getReferenceDate();
//                int day = id.getDay();
//                int hour = id.getHour();
//                int min = id.getMinute();
//                int month = id.getMonth();
//                int second = id.getSecond();
//
//                double lon_start = gdsv.makeHorizCoordSys().getStartX();
//                double lon_end = gdsv.makeHorizCoordSys().getEndX(); // x
//                double lat_start = gdsv.makeHorizCoordSys().getStartY();
//                double lat_end = gdsv.makeHorizCoordSys().getEndY(); // y
//
//                double dx = gdsv.makeHorizCoordSys().dx;
//                double dy = gdsv.makeHorizCoordSys().dy;
//
//                int index = 0;
//
////                DoublePredicate isLonEurope = lon -> (to_180(lon) >= -30 && to_180(lon) <= 69);
////                DoublePredicate isLatEurope = lat -> (lat >= 25 && lat <= 79);
////
////                DoublePredicate isRelativeHumidity = rh -> (is.getDiscipline() == 0) && (pdsv.getParameterCategory() == 1) && (pdsv.getParameterNumber() == 1) && (/*pdsv.getLevelValue1()>=90000.0 && */pdsv.getLevelValue1() == 100000.0);
////                DoublePredicate isUComponentOfWind  = uc -> (is.getDiscipline() == 0) && (pdsv.getParameterCategory() == 2) && (pdsv.getParameterNumber() == 2) && (/*pdsv.getLevelValue1()>=90000.0 && */pdsv.getLevelValue1() == 100000.0);
////                DoublePredicate isVComponentOfWind = vc -> (is.getDiscipline() == 0) && (pdsv.getParameterCategory() == 2) && (pdsv.getParameterNumber() == 3) && (/*pdsv.getLevelValue1()>=90000.0 && */pdsv.getLevelValue1() == 100000.0);
////                DoublePredicate isTemperature = t -> (is.getDiscipline() == 0) && (pdsv.getParameterCategory() == 0) && (pdsv.getParameterNumber() == 0) && (/*pdsv.getLevelValue1()>=90000.0 && */pdsv.getLevelValue1() == 100000.0);
////                DoublePredicate isCloudCover = cc -> (is.getDiscipline() == 0) && (pdsv.getParameterCategory() == 6) && (pdsv.getParameterNumber() == 1) && (/*pdsv.getLevelValue1()>=90000.0 && */pdsv.getLevelValue1() == 100000.0);
////
////                AtomicInteger counterRH = new AtomicInteger(0);
////                AtomicInteger counterUC = new AtomicInteger(0);
////                AtomicInteger counterVC = new AtomicInteger(0);
////                AtomicInteger counterT = new AtomicInteger(0);
////                AtomicInteger counterCC = new AtomicInteger(0);
////
////
////                IntStream.range(counterRH.intValue(), gdsv.getNy())
////                        .mapToDouble(i -> i*(-0.25) + 90)
////                        .parallel()
////                        .filter(isLatEurope)
////                        .forEachOrdered( lat -> {
////                            IntStream.range((int)Math.round(lon_start), gdsv.getNx())
////                                    .mapToDouble(j -> j*0.25)
////                                    .parallel()
////                                    .filter(isLonEurope.and(isRelativeHumidity))
////                                    .forEachOrdered( lon -> {
////                                        final int[] result = null;
////                                        // Get index value for lat/lon
////                                        final int[] idx = pwdGcs.findXYindexFromLatLon(lat, lon, result);
//////                                        1 + (idx[1] + gdsv.getNx() * idx[0])
////                                        new RelativeHumidity(new WeatherPK(lat, to_180(lon), pdsv.getLevelValue1(), Integer.valueOf(this.observationHour), Integer.valueOf(this.validationHour)), values[(idx[0] + gdsv.getNx() * idx[1])]);
//////                                        System.out.printf("%d. (lat; lon) %02f %02f => %5.4f \n", counterRH.intValue(), lat, lon, values[counterRH.intValue()]);
////                                        counterRH.getAndIncrement();
////                                    });
////                        });
////
////                IntStream.range(counterUC.intValue(), gdsv.getNy())
////                        .mapToDouble(i -> i*(-0.25) + 90)
////                        .parallel()
////                        .filter(isLatEurope)
////                        .forEachOrdered( lat -> {
////                            IntStream.range((int)Math.round(lon_start), gdsv.getNx())
////                                    .mapToDouble(j -> j*0.25)
////                                    .parallel()
////                                    .filter(isLonEurope.and(isUComponentOfWind))
////                                    .forEachOrdered( lon -> {
////                                        final int[] result = null;
////                                        // Get index value for lat/lon
////                                        final int[] idx = pwdGcs.findXYindexFromLatLon(lat, lon, result);
////                                        new UComponentOfWind(new WeatherPK(lat, to_180(lon), pdsv.getLevelValue1(), Integer.valueOf(this.observationHour), Integer.valueOf(this.validationHour)), values[(idx[0] + gdsv.getNx() * idx[1])]);
//////                                        System.out.printf("%d. (lat; lon) %02f %02f => %5.4f \n", counterUC.intValue(), lat, lon, values[counterUC.intValue()]);
////                                        counterUC.getAndIncrement();
////                                    });
////                        });
////
////                IntStream.range(counterVC.intValue(), gdsv.getNy())
////                        .mapToDouble(i -> i*(-0.25) + 90)
////                        .parallel()
////                        .filter(isLatEurope)
////                        .forEachOrdered( lat -> {
////                            IntStream.range((int)Math.round(lon_start), gdsv.getNx())
////                                    .mapToDouble(j -> j*0.25)
////                                    .parallel()
////                                    .filter(isLonEurope.and(isVComponentOfWind))
////                                    .forEachOrdered( lon -> {
////                                        final int[] result = null;
////                                        // Get index value for lat/lon
////                                        final int[] idx = pwdGcs.findXYindexFromLatLon(lat, lon, result);
////                                        new VComponentOfWind(new WeatherPK(lat, to_180(lon), pdsv.getLevelValue1(), Integer.valueOf(this.observationHour), Integer.valueOf(this.validationHour)), values[(idx[0] + gdsv.getNx() * idx[1])]);
//////                                        System.out.printf("%d. (lat; lon) %02f %02f => %5.4f \n", counterVC.intValue(), lat, lon, values[counterVC.intValue()]);
////                                        counterVC.getAndIncrement();
////                                    });
////                        });
////
////                IntStream.range(counterT.intValue(), gdsv.getNy())
////                        .mapToDouble(i -> i*(-0.25) + 90)
////                        .parallel()
////                        .filter(isLatEurope)
////                        .forEachOrdered( lat -> {
////                            IntStream.range((int)Math.round(lon_start), gdsv.getNx())
////                                    .mapToDouble(j -> j*0.25)
////                                    .parallel()
////                                    .filter(isLonEurope.and(isTemperature))
////                                    .forEachOrdered( lon -> {
////                                        final int[] result = null;
////                                        // Get index value for lat/lon
////                                        final int[] idx = pwdGcs.findXYindexFromLatLon(lat, lon, result);
////                                        new Temperature(new WeatherPK(lat, to_180(lon), pdsv.getLevelValue1(), Integer.valueOf(this.observationHour), Integer.valueOf(this.validationHour)), values[(idx[0] + gdsv.getNx() * idx[1])]);
////                                        System.out.printf("%d. (lat; lon) %02f %02f => %5.4f \n", counterT.intValue(), lat, lon, values[(idx[0] + gdsv.getNx() * idx[1])]);
////                                        counterT.getAndIncrement();
////                                    });
////                        });
////
////                IntStream.range(counterCC.intValue(), gdsv.getNy())
////                        .mapToDouble(i -> i*(-0.25) + 90)
////                        .parallel()
////                        .filter(isLatEurope)
////                        .forEachOrdered( lat -> {
////                            IntStream.range((int)Math.round(lon_start), gdsv.getNx())
////                                    .mapToDouble(j -> j*0.25)
////                                    .parallel()
////                                    .filter(isLonEurope.and(isCloudCover))
////                                    .forEachOrdered( lon -> {
////                                        final int[] result = null;
////                                        // Get index value for lat/lon
////                                        final int[] idx = pwdGcs.findXYindexFromLatLon(lat, lon, result);
////                                        new CloudCover(new WeatherPK(lat, to_180(lon), pdsv.getLevelValue1(), Integer.valueOf(this.observationHour), Integer.valueOf(this.validationHour)), values[(idx[0] + gdsv.getNx() * idx[1])]);
//////                                        System.out.printf("%d. (lat; lon) %02f %02f => %5.4f \n", counterCC.intValue(), lat, lon, values[counterCC.intValue()]);
////                                        counterCC.getAndIncrement();
////                                    });
////                        });
//            }
//        }
//
        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Total execution time to create 1000K objects in Java in millis: "
                + elapsedTime / 1000000);

//        return checkMeteoDataInitialization();
        return true;
    }

    public List<WeatherData> getWindDataListByObservationTimeAndLevel(Integer observation, Double level){
        List<WeatherData> selectedWeatherDataList = new ArrayList<>();
        List<WeatherData> weatherDataList = getWindDataListByObservationTime(observation);

        IntStream.range(0,weatherDataList.size())
                .filter(i -> weatherDataList.get(i).getWeatherPK().getLevel().equals(level))
                .forEach(item -> selectedWeatherDataList.add(weatherDataList.get(item)));


//        for(Integer index = 0; index < weatherDataList.size(); index++){
//            if(weatherDataList.get(index).getWeatherPK().getLevel().equals(level)){
//                selectedWeatherDataList.add(weatherDataList.get(index));
//            }
//        }
        return selectedWeatherDataList;
    }

    public List<WeatherData> getWindDataListByObservationTime(Integer observation){
        List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
        Map<WeatherPK,Float> temperatureMap = Temperature.getTemperatureForObservationTime(observation);
        Map<WeatherPK,Float> ucomponentMap = UComponentOfWind.getUComponentForObservationTime(observation);
        Map<WeatherPK,Float> vcomponentMap = VComponentOfWind.getVComponentForObservationTime(observation);
        Map<WeatherPK,Float> cloudMap = CloudCover.getCloudForObservationTime(observation);
        Map<WeatherPK,Float> humidityMap = RelativeHumidity.getHumidityForObservationTime(observation);
//        if(!(temperatureMap.isEmpty() && ucomponentMap.isEmpty() && vcomponentMap.isEmpty() && humidityMap.isEmpty()) &&
//                ((temperatureMap.size() == ucomponentMap.size()) && (temperatureMap.size() == vcomponentMap.size()) &&
//                        (temperatureMap.size() == cloudMap.size())&&(temperatureMap.size() == humidityMap.size()))){

        WeatherPK.getKeys()
                .stream()
                .filter(pk -> pk.getObservation().equals(observation))
                .forEach(pk -> weatherDataList.add(
                        new WeatherData(
                                pk,
                                (Float)temperatureMap.get(pk),
                                (Float)ucomponentMap.get(pk),
                                (Float)vcomponentMap.get(pk),
                                (Float)humidityMap.get(pk),
                                (Float)cloudMap.get(pk))));

//            for(WeatherPK pk : WeatherPK.getKeys()){
//                if(pk.getObservation().equals(observation)){
//                    Float temperature = (Float)temperatureMap.get(pk);
//                    Float ucomponent = (Float)ucomponentMap.get(pk);
//                    Float vcomponent = (Float)vcomponentMap.get(pk);
//                    Float humidity = (Float)humidityMap.get(pk);
//                    Float cloudness = (Float)cloudMap.get(pk);
//                    weatherDataList.add(new WeatherData(pk, temperature, ucomponent, vcomponent, humidity, cloudness));
//                }
//            }
//        }
//        else{
//            System.out.println("temp: " + temperatureMap.size() + " ucomp: " + ucomponentMap.size() + " vcomp: " + vcomponentMap.size() + " cloud: " + cloudMap.size() + " num: " + humidityMap.size());
//            System.out.println("Temperature, UComponent, VComponent, Humidity list don't match equal sizes for observation " + observation);
//        }
        return weatherDataList;
    }

    public Set<Double> getDistinctLevels(){
        return WeatherPK.getLevels();
    }

    private double getLatLon(double longitude){
        return longitude >= 180.0 ? longitude - 360.0 : longitude;
    }

    // ---------------------------------------------------------------- //

    private Float convertToCelsius(Float temperatureInKelvins){
        return temperatureInKelvins - 273.15f;
    }

    private Double to_180(Double longitude){
        return mod((longitude + 180.0), 360.0) - 180.0 + getModulo(longitude);
    }

    private double mod(double x, double y){
        return (int)( x - (int)Math.floor( x / y ) * y );
    }

    private double getModulo(double longitude){
        return longitude - Math.floor(longitude);
    }

    public String getFileBaseUrl() {
        return FILE_BASE_URL;
    }

    public void setFileBaseUrl(String fileBaseUrl) {
        FILE_BASE_URL = fileBaseUrl;
    }

    public String getFileName() {
        return FILE_NAME;
    }

    public void setFileName(String fileName) {
        FILE_NAME = fileName;
    }

    public String getObservationHour() {
        return observationHour;
    }

    public void setObservationHour(String observationHour) {
        this.observationHour = observationHour;
    }

    public String getValidationHour() {
        return validationHour;
    }

    public void setValidationHour(String validationHour) {
        this.validationHour = validationHour;
    }

    private String getCorrectedValidationHour(String validationHour){
        if(validationHour != null || !validationHour.isEmpty()){
            if(validationHour.length()==3){
                return validationHour;
            }
            else if(validationHour.length()==2){
                return "0".concat(validationHour);
            }
            else if(validationHour.length()==1){
                return "00".concat(validationHour);
            }
        }
        else{
            return null;
        }
        return null;
    }

    private String getCompoundURLPath(String observationHour){
        return "gfs." +
                getSimpleDate(getCurrentDate()) +
                "/" +
                observationHour;
    }

    private String getFileName(String observationHour, String validationHour){
        return  "gfs" +
                "." +
                "t" + observationHour + "z" +
                "." +
                "pgrb2" +
                "." +
                "0p25" +
                "." +
                "f"+ getCorrectedValidationHour(validationHour);
    }

    public void download(){
        String grib2FileName = FILE_LOCAL_PATH + "/" + FILE_NAME + ".grib2";
        System.out.println("URL: " + FILE_BASE_URL + " fname: " + grib2FileName);
        try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_BASE_URL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(grib2FileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("file is downloaded");
        } catch (IOException e) {
            // handle exception
        }
    }

    private Date getCurrentDate(){
        return Calendar.getInstance().getTime();
    }

    private String getSimpleDate(Date date){
        DateFormat df = new SimpleDateFormat(pattern);
        String dateAsString = df.format(date);
        System.out.println("This is: " + dateAsString);
        return dateAsString;
    }

    private String getUTCDate(Date date){
        DateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateAsString = df.format(date);
        System.out.println(df.format(date));
        return dateAsString;
    }

    private String cmpDates(){
        Date date = getCurrentDate();
        return getSimpleDate(date) + "  " + getUTCDate(date);
    }

    public List<String> getValidationList(){
         return IntStream.rangeClosed(0, 384)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());
    }

    public List<String> getObservationList(){
        return this.observation;
    }

    public List<String> getMeteoParamsList(){
        System.out.println(this.meteoParams);
        return this.meteoParams;
    }

}
