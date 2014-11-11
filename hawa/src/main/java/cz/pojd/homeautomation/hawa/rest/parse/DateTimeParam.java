package cz.pojd.homeautomation.hawa.rest.parse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Parses the dates following ISO8601
 *
 * @author Lubos Housa
 * @since Nov 11, 2014 5:43:59 PM
 */
public class DateTimeParam {
    private final DateTime dateTime;

    public DateTimeParam(String dateStr) throws WebApplicationException {
	DateTimeFormatter parser = ISODateTimeFormat.date();
	try {
	    dateTime = parser.parseDateTime(dateStr);
	} catch (IllegalArgumentException e) {
	    throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
		    .entity("Couldn't parse date string: " + e.getMessage())
		    .build());
	}
    }

    public DateTime getDateTime() {
	return dateTime;
    }
}