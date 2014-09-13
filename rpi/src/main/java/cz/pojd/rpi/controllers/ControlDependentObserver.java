package cz.pojd.rpi.controllers;

import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Special kind of ControlObserver, only updating the control if the dependent sensor is disabled or off.
 *
 * @author Lubos Housa
 * @since Sep 13, 2014 6:28:19 PM
 */
public class ControlDependentObserver extends ControlObserver {

    private static final Log LOG = LogFactory.getLog(ControlDependentObserver.class);

    private final ObservableSensor dependentSensor;

    public ControlDependentObserver(ObservableSensor dependentSensor, Control control) {
	super(control);
	this.dependentSensor = dependentSensor;
    }

    @Override
    public void update(Observable o, Object arg) {
	if (!dependentSensor.isEnabled() || !dependentSensor.isSwitchedOn()) {
	    super.update(o, arg);
	} else if (LOG.isDebugEnabled()) {
	    LOG.debug("Dependent sensor is enabled and switched on: " + dependentSensor + ". Therefore ignoring the update called on this observer: "
		    + arg);
	}
    }
}
