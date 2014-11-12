package cz.pojd.security.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.web.CalendarEvent;

public class CalendarEventTranslatorImpl implements CalendarEventTranslator {
    private static final Log LOG = LogFactory.getLog(CalendarEventTranslatorImpl.class);

    @Override
    public Collection<CalendarEvent> translate(Collection<SecurityEvent> securityEvents) {
	List<CalendarEvent> result = new ArrayList<>();
	if (securityEvents != null) {
	    for (SecurityEvent securityEvent : securityEvents) {
		CalendarEvent calendarEvent = translate(securityEvent);
		if (calendarEvent != null) {
		    result.add(calendarEvent);
		}
	    }
	}
	return result;
    }

    private CalendarEvent translate(SecurityEvent securityEvent) {
	if (securityEvent.getAt() != null) {
	    CalendarEvent result = new CalendarEvent();
	    result.setWhat(securityEvent.getType() != null ? securityEvent.getType().toString() : "N/A");
	    result.setTitle(result.getWhat());
	    if (securityEvent.getSource() != null) {
		result.setWhere(securityEvent.getSource().getName());
		result.setFloor(securityEvent.getSource().getFloor());
	    }
	    result.setStart(securityEvent.getAtAsDate());
	    result.setEnd(securityEvent.getAt().plusHours(1).toDate());
	    return result;
	} else {
	    LOG.warn("Found security event without time. Ignoring this event in the translation: " + securityEvent);
	    return null;
	}
    }
}
