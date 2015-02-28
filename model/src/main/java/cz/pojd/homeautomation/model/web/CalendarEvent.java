package cz.pojd.homeautomation.model.web;

import java.util.Date;

import cz.pojd.homeautomation.model.Floor;

/**
 * CalendarEvent is a model object for a calendar event
 *
 * @author Lubos Housa
 * @since Nov 10, 2014 5:31:34 PM
 */
public class CalendarEvent {

    private String title;
    private String type;
    private String detail;
    private String where;
    private Floor floor;
    private Date start;
    private Date end;

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getDetail() {
	return detail;
    }

    public void setDetail(String detail) {
	this.detail = detail;
    }

    public String getWhere() {
	return where;
    }

    public void setWhere(String where) {
	this.where = where;
    }

    public Floor getFloor() {
	return floor;
    }

    public void setFloor(Floor floor) {
	this.floor = floor;
    }

    public Date getStart() {
	return start;
    }

    public void setStart(Date start) {
	this.start = start;
    }

    public Date getEnd() {
	return end;
    }

    public void setEnd(Date end) {
	this.end = end;
    }

    @Override
    public String toString() {
	return "CalendarEvent [type=" + type + ", detail=" + detail + ", where=" + where + ", floor=" + floor + ", start=" + start + ", end=" + end
		+ "]";
    }
}
