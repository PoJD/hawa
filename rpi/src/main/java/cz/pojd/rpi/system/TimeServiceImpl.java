package cz.pojd.rpi.system;

import org.joda.time.DateTime;

public class TimeServiceImpl implements TimeService {

    @Override
    public DateTime now() {
	return DateTime.now();
    }
}
