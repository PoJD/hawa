package cz.pojd.homeautomation.hawa.rooms.factory;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.homeautomation.hawa.lights.LightCapableFactorySupport;
import cz.pojd.homeautomation.hawa.rooms.Floor;
import cz.pojd.homeautomation.hawa.rooms.Room;
import cz.pojd.homeautomation.hawa.rooms.RoomDetail;
import cz.pojd.homeautomation.hawa.spring.RoomSpecification;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.w1.Ds18B20TemperatureSensor;
import cz.pojd.rpi.system.RuntimeExecutor;

public class DefaultRoomFactory extends LightCapableFactorySupport implements RoomFactory {

    private static final Log LOG = LogFactory.getLog(DefaultRoomFactory.class);
    private final GpioProvider basementFloorLightSwitches, basementFloorLightControls, firstFloorLightSwitches, firstFloorLightControls;
    private final boolean newRaspi;

    @Inject
    private Gpio gpio;

    @Inject
    private RuntimeExecutor runtimeExecutor;

    public DefaultRoomFactory(GpioProvider basementFloorLightSwitches, GpioProvider basementFloorLightControls, GpioProvider firstFloorLightSwitches,
	    GpioProvider firstFloorLightControls, boolean newRaspi) {
	this.basementFloorLightSwitches = basementFloorLightSwitches;
	this.basementFloorLightControls = basementFloorLightControls;
	this.firstFloorLightSwitches = firstFloorLightSwitches;
	this.firstFloorLightControls = firstFloorLightControls;
	this.newRaspi = newRaspi;
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
	LOG.info("Creating new instance of room using: " + roomSpecification);

	Room room = new Room();
	room.setName(roomSpecification.getName());
	room.setTemperatureSensor(new Ds18B20TemperatureSensor(getRuntimeExecutor(), roomSpecification.getTemperatureID()));
	room.setFloor(roomSpecification.getFloor() != null ? roomSpecification.getFloor() : Floor.BASEMENT);

	@SuppressWarnings("unused")
	GpioProvider switchProvider = null, controlProvider = null;
	if (Floor.BASEMENT.equals(room.getFloor())) {
	    switchProvider = getBasementFloorLightSwitches();
	    controlProvider = getBasementFloorLightControls();
	} else {
	    switchProvider = getFirstFloorLightSwitches();
	    controlProvider = getFirstFloorLightControls();
	}

	// TODO temporary until the app is deployed to real house
	if (RoomSpecification.HALL_DOWN.equals(roomSpecification)) {
	    enrichLight(room, getGpio(), getGpio().getDefaultProvider(), getGpio().getDefaultProvider(), RaspiPin.GPIO_04, RaspiPin.GPIO_05,
		    RaspiPin.GPIO_06, newRaspi, roomSpecification.getLightLevelSensorAddress(), roomSpecification.getLightLevelTreshold());
	}
	//enrichLight(room, getGpio(), switchProvider, controlProvider, roomSpecification.getMotionSensorPin(), roomSpecification.getLightPin(),
		//roomSpecification.getLightPin(), newRaspi, roomSpecification.getLightLevelSensorAddress(), roomSpecification.getLightLevelTreshold());

	// #21 avoid reading temperature now on room creation
	room.setLastDetail(new RoomDetail(room, false));

	LOG.info("New room created: " + room);
	return room;
    }
}
