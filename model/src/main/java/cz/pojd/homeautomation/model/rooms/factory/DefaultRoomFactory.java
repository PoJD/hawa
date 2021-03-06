package cz.pojd.homeautomation.model.rooms.factory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.homeautomation.model.lights.MotionSensorLightDetails;
import cz.pojd.homeautomation.model.lights.MotionSensorLightTrigger;
import cz.pojd.homeautomation.model.rooms.Entry;
import cz.pojd.homeautomation.model.rooms.Room;
import cz.pojd.homeautomation.model.rooms.RoomDetail;
import cz.pojd.homeautomation.model.spring.EntrySpecification;
import cz.pojd.homeautomation.model.spring.RoomSpecification;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.w1.Ds18B20TemperatureSensor;
import cz.pojd.rpi.system.RuntimeExecutor;

public class DefaultRoomFactory implements RoomFactory {

    private static final Log LOG = LogFactory.getLog(DefaultRoomFactory.class);
    private final GpioProvider basementFloorLightSwitches, basementFloorLightControls, firstFloorLightSwitches,
	    firstFloorLightControls;

    @Inject
    private Gpio gpio;
    @Inject
    private RuntimeExecutor runtimeExecutor;
    @Inject
    private MotionSensorLightTrigger lightTrigger;
    @Inject
    private EntryFactory entryFactory;

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

    public MotionSensorLightTrigger getLightTrigger() {
	return lightTrigger;
    }

    public void setLightTrigger(MotionSensorLightTrigger lightTrigger) {
	this.lightTrigger = lightTrigger;
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

    public EntryFactory getEntryFactory() {
	return entryFactory;
    }

    public void setEntryFactory(EntryFactory entryFactory) {
	this.entryFactory = entryFactory;
    }

    @Override
    public Room create(RoomSpecification roomSpecification) {
	LOG.info("Creating new instance of room using: " + roomSpecification);

	Room room = new Room();
	room.setSpecification(roomSpecification);
	room.setName(roomSpecification.getName());
	room.setTemperatureSensor(new Ds18B20TemperatureSensor(getRuntimeExecutor(), roomSpecification.getTemperatureID()));
	room.setFloor(roomSpecification.getFloor());

	@SuppressWarnings("unused")
	GpioProvider switchProvider = null, controlProvider = null;
	if (Floor.Basement.equals(room.getFloor())) {
	    switchProvider = getBasementFloorLightSwitches();
	    controlProvider = getBasementFloorLightControls();
	} else {
	    switchProvider = getFirstFloorLightSwitches();
	    controlProvider = getFirstFloorLightControls();
	}

	// TODO temporary until the app is deployed to real house, then change below to real switch and control providers found above and use motion
	// sensor pin and light pins from roomspecifications too
	if (RoomSpecification.HALL_DOWN.equals(roomSpecification)) {
	    lightTrigger.setup(MotionSensorLightDetails.newBuilder()
		    .lightCapable(room)
		    .switchProvider(getGpio().getDefaultProvider())
		    .controlProvider(getGpio().getDefaultProvider())
		    .motionSensorPin(RaspiPin.GPIO_04)
		    .lightSwitchPin(RaspiPin.GPIO_05)
		    .lightControlPin(RaspiPin.GPIO_06)
		    .lightLevelSensorPin(null)
		    .lightLevelTreshold(roomSpecification.getLightLevelTreshold())
		    .build());
	}

	List<Entry> entries = new ArrayList<>();
	for (EntrySpecification entrySpecification : roomSpecification.getEntrySpecifications()) {
	    Entry entry = entryFactory.create(entrySpecification, room);
	    entries.add(entry);
	}
	room.setEntries(entries);

	// #21 avoid reading temperature now on room creation
	room.setLastDetail(new RoomDetail(room, false));

	LOG.info("New room created: " + room);
	return room;
    }
}
