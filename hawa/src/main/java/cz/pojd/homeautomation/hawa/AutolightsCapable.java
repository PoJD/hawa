package cz.pojd.homeautomation.hawa;

import cz.pojd.rpi.sensors.observable.ObservableSensor;

public class AutolightsCapable {
    private ObservableSensor autoLights; // can be null

    public ObservableSensor getAutoLights() {
	return autoLights;
    }

    public void setAutoLights(ObservableSensor autoLights) {
	this.autoLights = autoLights;
    }

    public void setAutolightsEnabled(Boolean enabled) {
	if (getAutoLights() != null && enabled != null) {
	    getAutoLights().setEnabled(enabled);
	}
    }

    public Boolean getAutoLightsEnabled() {
	return (getAutoLights() != null) ? getAutoLights().isEnabled() : null;
    }
}
