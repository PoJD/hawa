package cz.pojd.homeautomation.model.rooms.factory;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.GpioProvider;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.homeautomation.model.rooms.Entry;
import cz.pojd.homeautomation.model.rooms.Room;
import cz.pojd.homeautomation.model.spring.EntrySpecification;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.observable.GpioSwitch;

/**
 * Default entry factory
 *
 * @author Lubos Housa
 * @since Dec 9, 2014 11:32:31 PM
 */
public class DefaultEntryFactory implements EntryFactory {

    private static final Log LOG = LogFactory.getLog(DefaultEntryFactory.class);

    private final GpioProvider basementProvider, firstFloorProvider;

    @Inject
    private Gpio gpio;

    public DefaultEntryFactory(GpioProvider basementPovider, GpioProvider firstFloorProvider) {
	this.basementProvider = basementPovider;
	this.firstFloorProvider = firstFloorProvider;
    }

    @Override
    public Entry create(EntrySpecification specification, Room room) {
	LOG.info("Creating new instance of entry using: " + specification + " for room: " + room);

	Entry entry = new Entry();
	entry.setSpecification(specification);
	entry.setName(specification.getName());
	entry.setReedSwitch(new GpioSwitch(gpio, Floor.Basement == room.getFloor() ? basementProvider : firstFloorProvider, specification
		.getName() + " reed switch", specification.getReedSwitchPin()));
	entry.setRoom(room);

	LOG.info("New entry created: " + entry);

	return entry;
    }
}
