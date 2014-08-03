package cz.pojd.rpi.sensors;

public abstract class AbstractSensor {

    protected String double2String(double d) {
	return String.format("%.2f", d);
    }
}
