package cz.pojd.homeautomation.model.lights;

import cz.pojd.rpi.controllers.Specification;
import cz.pojd.rpi.controls.Control;

/**
 * Special kind of Specification, checking if the light level reading on the light capable is below a treshold or if the control is on. In either case
 * this will be satisfied.
 *
 * @author Lubos Housa
 * @since Oct 3, 2014 11:33:27 PM
 */
public class LightLevelBelowTresholdOrControlOnSpecification implements Specification {

    private final LightCapable lightCapable;
    private final double treshold;
    private final Control control;

    public LightLevelBelowTresholdOrControlOnSpecification(LightCapable lightCapable, double treshold, Control control) {
	this.lightCapable = lightCapable;
	this.treshold = treshold;
	this.control = control;
    }

    @Override
    public boolean isSatisfied() {
	return control.isSwitchedOn()
		|| (lightCapable.getLastDetail() != null && lightCapable.getLastDetail().getLightLevel() != null && lightCapable.getLastDetail()
			.getLightLevel().getDoubleValue() < treshold);
    }

    @Override
    public String toString() {
	return "LightLevelBelowTresholdOrControlOnSpecification [lightCapable=" + lightCapable + ", treshold=" + treshold + ", control=" + control
		+ "]";
    }
}
