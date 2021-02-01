package application.rest.handler;

public class Wind {

    private Float ucomp;
    private Float vcomp;

    public Wind(Float ucomp, Float vcomp) {
        this.ucomp = ucomp;
        this.vcomp = vcomp;
    }

    public Float getUComp() {
        return ucomp;
    }

    public void setUComp(Float ucomp) {
        this.ucomp = ucomp;
    }

    public Float getVComp() {
        return vcomp;
    }

    public void setVComp(Float vcomp) {
        this.vcomp = vcomp;
    }

    private Integer convertToKnots(Double windSpeedInMetersPerSecond){
        return (int)Math.round(1.943844 * windSpeedInMetersPerSecond);
    }

    public Double getMeteorologicalAngle(){
        return getAngle() < 0.0 ? getAngle() + 360.0 : getAngle();
    }

    public Integer getWindSpeed(){
        return convertToKnots(Math.sqrt(Math.pow(ucomp, 2) + Math.pow(vcomp, 2)));
    }

    private Double getAngle(){
        return (180.0/Math.PI)*Math.atan2(ucomp, vcomp)+270.0;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "ucomp=" + ucomp +
                ", vcomp=" + vcomp +
                '}';
    }
}

