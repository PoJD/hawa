package cz.pojd.rpi.sensors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractSensor {
    private static final Log LOG = LogFactory.getLog(AbstractSensor.class);

    protected void waitfor(long howMuch) {
	try {
	    Thread.sleep(howMuch);
	} catch (InterruptedException e) {
	    LOG.error("Interrupted while sleeping...");
	}
    }
}
