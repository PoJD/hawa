package cz.pojd.homeautomation.model.web;

import java.util.Date;

/**
 * CalendarEvent is a model object for a calendar event
 *
 * @author Lubos Housa
 * @since Nov 10, 2014 5:31:34 PM
 */
public class CalendarEvent {

    private final String title;
    private final Date start;
    private final Date end;

    public CalendarEvent(Builder builder) {
	this.title = builder.title;
	this.start = builder.start;
	this.end = builder.end;
    }

    public String getTitle() {
	return title;
    }

    public Date getStart() {
	return start;
    }

    public Date getEnd() {
	return end;
    }

    public static Builder newBuilder() {
	return new Builder();
    }

    public static class Builder {
	private String title;
	private Date start;
	private Date end;

	private Builder() {
	};

	public Builder title(String title) {
	    this.title = title;
	    return this;
	}

	public Builder start(Date start) {
	    this.start = start;
	    return this;
	}

	public Builder end(Date end) {
	    this.end = end;
	    return this;
	}

	public CalendarEvent build() {
	    return new CalendarEvent(this);
	}
    }

    @Override
    public String toString() {
	return "CalendarEvent [title=" + title + ", start=" + start + ", end=" + end + "]";
    }
}
