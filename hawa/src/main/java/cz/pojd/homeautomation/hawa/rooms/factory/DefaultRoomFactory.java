package cz.pojd.homeautomation.hawa.rooms.factory;

import javax.inject.Inject;

import com.pi4j.io.gpio.GpioProvider;

import cz.pojd.homeautomation.hawa.rooms.Floor;
import cz.pojd.homeautomation.hawa.rooms.Room;
import cz.pojd.homeautomation.hawa.spring.RoomSpecification;
import cz.pojd.rpi.controllers.ControlObserver;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.observable.GpioObservableSensor;
import cz.pojd.rpi.sensors.w1.Ds18B20TemperatureSensor;
import cz.pojd.rpi.system.RuntimeExecutor;

public class DefaultRoomFactory implements RoomFactory {

    private final GpioProvider basementFloorLightSwitches, basementFloorLightControls, firstFloorLightSwitches, firstFloorLightControls;

    @Inject
    private Gpio gpio;

    @Inject
    private RuntimeExecutor runtimeExecutor;

    public DefaultRoomFactory(GpioProvider basementFloorLightSwitches, GpioProvider basementFloorLightControls, GpioProvider firstFloorLightSwitches,
	    GpioProvider firstFloorLightControls) {
	this.basementFloorLightSwitches = basementFloorLightSwitches;
	this.basementFloorLightControls = basementFloorLightControls;
	this.firstFloorLightSwitches = firstFloorLightSwitches;
	this.firstFloorLightControls = firstFloorLightControls;
    }

    public Gpio getGpio() {
	return gpio;
    }

    public void setGpio(Gpio gpio) {
	this.gpio = gpio;
    }

    public RuntimeExecutor getRuntimeExecutor() {
	return runtimeExecutor;
    }

    public void setRuntimeExecutor(RuntimeExecutor runtimeExecutor) {
	this.runtimeExecutor = runtimeExecutor;
    }

    public GpioProvider getBasementFloorLightSwitches() {
	return basementFloorLightSwitches;
    }

    public GpioProvider getBasementFloorLightControls() {
	return basementFloorLightControls;
    }

    public GpioProvider getFirstFloorLightSwitches() {
	return firstFloorLightSwitches;
    }

    public GpioProvider getFirstFloorLightControls() {
	return firstFloorLightControls;
    }

    @Override
    public Room create(RoomSpecification roomSpecification) {
	Room room = new Room();
	room.setName(roomSpecification.getName());
	room.setTemperatureSensor(new Ds18B20TemperatureSensor(getRuntimeExecutor(), roomSpecification.getTemperatureID()));
	room.setFloor(roomSpecification.getFloor() != null ? roomSpecification.getFloor() : Floor.BASEMENT);

	GpioProvider switchProvider = null, lightProvider = null;
	if (Floor.BASEMENT.equals(room.getFloor())) {
	    switchProvider = getBasementFloorLightSwitches();
	    lightProvider = getBasementFloorLightControls();
	} else {
	    switchProvider = getFirstFloorLightSwitches();
	    lightProvider = getFirstFloorLightControls();
	}

	room.setLightControl(new GpioControl(getGpio(), lightProvider, roomSpecification.getName() + " light control", roomSpecification
		.getLightPin()));
	room.setLightSwitch(new GpioObservableSensor(getGpio(), switchProvider, roomSpecification.getName() + " light switch", roomSpecification
		.getLightPin()));
	room.getLightSwitch().addObserver(new ControlObserver(room.getLightControl()));

	if (roomSpecification.getMotionSensorPin() != null) {
	    room.setMotionSensor(new GpioObservableSensor(getGpio(), switchProvider, roomSpecification.getName() + " motion sensor",
		    roomSpecification.getMotionSensorPin()));
	    room.getMotionSensor().addObserver(new ControlObserver(room.getLightControl()));
	}

	return room;
    }
}
