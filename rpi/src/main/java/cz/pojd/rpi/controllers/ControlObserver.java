package cz.pojd.rpi.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.controls.Controllable;

/**
 * ControlObserver is a special observer switching on the control anytime this observer is notified (assuming the sensor is enabled)
 *
 * @author Lubos Housa
 * @since Sep 9, 2014 7:50:49 PM
 */
public class ControlObserver implements Observer<Controllable, Boolean> {

    private static final Log LOG = LogFactory.getLog(ControlObserver.class);

    private final Control control;

    public ControlObserver(Control control) {
	this.control = control;
    }

    public Control getControl() {
	return control;
    }

    @Override
    public void update(Controllable controllable, Boolean switchedOn) {
	// only here we decide based on the enabled flag
	if (!controllable.isEnabled()) {
	    if (LOG.isDebugEnabled()) {
		LOG.debug(controllable + " is not enabled. Therefore update call ignored with switchedOn: " + switchedOn);
	    }
	} else {
	    getControl().setSwitchedOn(switchedOn);
	}
    }
}
