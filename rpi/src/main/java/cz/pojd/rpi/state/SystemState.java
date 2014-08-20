package cz.pojd.rpi.state;

import java.util.ArrayList;
import java.util.List;

/**
 * System state wraps the current system state
 * 
 * @author Lubos Housa
 * @since Aug 20, 2014 9:04:10 PM
 */
public class SystemState {
    private final List<PropertyValue> values = new ArrayList<>();
    private final String lastUpdate;

    public SystemState(String lastUpdate) {
	this.lastUpdate = lastUpdate;
    }

    public void addValue(PropertyValue value) {
	if (value != null) {
	    this.values.add(value);
	}
    }

    public List<PropertyValue> getValues() {
	return values;
    }

    public String getLastUpdate() {
	return lastUpdate;
    }

    @Override
    public String toString() {
	return "SystemState [values=" + values + ", lastUpdate=" + lastUpdate + "]";
    }
}
