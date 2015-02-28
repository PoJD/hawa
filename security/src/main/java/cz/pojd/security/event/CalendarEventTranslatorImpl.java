package cz.pojd.security.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.web.CalendarEvent;

/**
 * Translates security events into calendar events
 *
 * @author Lubos Housa
 * @since Nov 25, 2014 11:56:27 PM
 */
public class CalendarEventTranslatorImpl implements CalendarEventTranslator {
    private static final Log LOG = LogFactory.getLog(CalendarEventTranslatorImpl.class);

    @Override
    public CalendarEvent translate(SecurityEvent securityEvent) {
	if (securityEvent == null) {
	    return null;
	}
	if (securityEvent.getAt() != null) {
	    CalendarEvent result = new CalendarEvent();
	    result.setType(securityEvent.getType() != null ? securityEvent.getType().getName() : "");
	    result.setDetail(securityEvent.getDetail());
	    result.setTitle(result.getType());
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
