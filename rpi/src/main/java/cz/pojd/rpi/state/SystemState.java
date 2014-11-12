package cz.pojd.rpi.state;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * System state wraps the current system state
 * 
 * @author Lubos Housa
 * @since Aug 20, 2014 9:04:10 PM
 */
public class SystemState {
    private final List<PropertyValue> values = new ArrayList<>();
    private Date lastUpdate;
    private List<String> logSystem, logApplication;
    private boolean dbRunning;

    public void addValue(PropertyValue value) {
	if (value != null) {
	    this.values.add(value);
	}
    }

    public List<PropertyValue> getValues() {
	return values;
    }

    public Date getLastUpdate() {
	return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
	this.lastUpdate = lastUpdate;
    }

    public List<String> getLogSystem() {
	return logSystem;
    }

    public void setLogSystem(List<String> logSystem) {
	this.logSystem = logSystem;
    }

    public List<String> getLogApplication() {
	return logApplication;
    }

    public void setLogApplication(List<String> logApplication) {
	this.logApplication = logApplication;
    }

    public boolean isDbRunning() {
	return dbRunning;
    }

    public void setDbRunning(boolean dbRunning) {
	this.dbRunning = dbRunning;
    }

    @Override
    public String toString() {
	return "SystemState [values=" + values + ", lastUpdate=" + lastUpdate + ", logSystem=" + logSystem + ", logApplication=" + logApplication
		+ ", dbRunning=" + dbRunning + "]";
    }
}
