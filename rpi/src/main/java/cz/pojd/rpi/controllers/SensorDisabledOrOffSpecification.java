package cz.pojd.rpi.controllers;

import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Special kind of Specification, checking if the dependent sensor is disabled or off.
 *
 * @author Lubos Housa
 * @since Sep 13, 2014 6:28:19 PM
 */
public class SensorDisabledOrOffSpecification implements Specification {

    private final ObservableSensor dependentSensor;

    public SensorDisabledOrOffSpecification(ObservableSensor dependentSensor) {
	this.dependentSensor = dependentSensor;
    }

    @Override
    public boolean isSatisfied() {
	return !dependentSensor.isEnabled() || !dependentSensor.isSwitchedOn();
    }

    @Override
    public String toString() {
	return "SensorDisabledOrOffSpecification [dependentSensor=" + dependentSensor + "]";
    }
}
