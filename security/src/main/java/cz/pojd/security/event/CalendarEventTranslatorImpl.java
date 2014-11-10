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
	    return CalendarEvent.newBuilder()
		    .title(new StringBuilder().append(securityEvent.getType()).append(" at ").append(securityEvent.getSource()).toString())
		    .start(securityEvent.getAtAsDate())
		    .end(securityEvent.getAt().plusHours(1).toDate())
		    .build();
	} else {
	    LOG.warn("Found security event without time. Ignoring this event in the translation: " + securityEvent);
	    return null;
	}
    }
}
