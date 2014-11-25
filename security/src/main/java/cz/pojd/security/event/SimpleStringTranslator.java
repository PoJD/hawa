package cz.pojd.security.event;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Translates security events into a string. Might be simpler to use velocity, but seems like a waste of additional jars. The below should be simple
 * enough to keep as a string here.
 *
 * @author Lubos Housa
 * @since Nov 25, 2014 11:56:42 PM
 */
public class SimpleStringTranslator implements SecurityEventTranslator<String> {
    private DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

    @Override
    public String translate(SecurityEvent securityEvent) {
	return "<html><body>" +
		"<h3>Dear owner,</h3><br/>" +
		"a security breach was detected at the house, see more details below: <br/><br/>" +
		"<table><tbody>" +
		(securityEvent.getType() != null ? "<tr><th>Type</th><td>" + securityEvent.getType().getName() + "</td>" : "") +
		(securityEvent.getDetail() != null ? "<tr><th>Detail</th><td>" + securityEvent.getDetail() + "</td>" : "") +
		(securityEvent.getSource() != null ? "<tr><th>Where</th><td>" + securityEvent.getSource().getName() + "</td>"
		+ "<tr><th>Floor</th><td>" + securityEvent.getSource().getFloor() + "</td>" : "") +
		(securityEvent.getFilePath() != null ? "<tr><th>File path</th><td>" + securityEvent.getFilePath() + "</td>" : "") +
		(securityEvent.getAt() != null ? "<tr><th>When</th><td>" + formatter.print(securityEvent.getAt()) + "</td>" : "") +
		"</tbody></table>" +
		"</body></html>";
    }
}
