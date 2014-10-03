package cz.pojd.rpi.controllers;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Observer dependent on a list of specifications. All must be satisfied in order to fire the dependent control.
 *
 * @author Lubos Housa
 * @since Oct 3, 2014 10:56:38 PM
 */
public class SpecificationsDependentObserver extends ControlObserver {

    private static final Log LOG = LogFactory.getLog(SpecificationsDependentObserver.class);

    private final List<Specification> specifications;

    public SpecificationsDependentObserver(Control control, Specification... specifications) {
	super(control);
	this.specifications = Arrays.asList(specifications);
    }

    @Override
    public void switched(ObservableSensor sensor, boolean switchedOn) {
	for (Specification specification : specifications) {
	    if (specification.isSatisfied()) {
		if (LOG.isDebugEnabled()) {
		    LOG.debug("Specification " + specification + " is satisfied. Checking next specification.");
		}
	    } else {
		if (LOG.isDebugEnabled()) {
		    LOG.debug("Specification " + specification + " is not satisfied. Suppressing the switched event.");
		}
		return;
	    }
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("All specifications are satisfied. Letting the switched event processing.");
	}
	super.switched(sensor, switchedOn);
    }
}
