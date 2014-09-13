package cz.pojd.homeautomation.hawa;

import java.util.List;

import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.observable.BaseObservableSensor;

public class MockObservableSensor extends BaseObservableSensor {
    private int readCount;

    public List<Reading> readAll() {
	return null;
    }

    public Reading read() {
	readCount++;
	return Reading.newBuilder().doubleValue(1.00).build();
    }

    public int getReadCount() {
	return readCount;
    }

    public void setReadCount(int readCount) {
	this.readCount = readCount;
    }

    @Override
    public boolean isInitiated() {
	return true;
    }
}
