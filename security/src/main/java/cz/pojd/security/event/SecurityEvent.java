package cz.pojd.security.event;

import java.nio.file.Path;

import org.joda.time.DateTime;

/**
 * SecurityEvent wraps a security event triggered within the application.
 *
 * @author Lubos Housa
 * @since Nov 2, 2014 7:47:45 PM
 */
public class SecurityEvent {
    private Type type;
    private String where;
    private final DateTime when = new DateTime();
    private Path filePath;

    public Type getType() {
	return type;
    }

    public void setType(Type type) {
	this.type = type;
    }

    public String getWhere() {
	return where;
    }

    public void setWhere(String where) {
	this.where = where;
    }

    public DateTime getWhen() {
	return when;
    }

    public Path getFilePath() {
	return filePath;
    }

    public void setFilePath(Path filePath) {
	this.filePath = filePath;
    }

    @Override
    public String toString() {
	return "SecurityEvent [type=" + type + ", where=" + where + ", when=" + when + ", filePath=" + filePath + "]";
    }
}
