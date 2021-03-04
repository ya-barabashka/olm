package application.rest.meteoentity;

import java.util.Arrays;

public class GRIB2 {

    float[] values;

    public GRIB2(float[] values) {
        this.values = values;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "GRIB2{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
