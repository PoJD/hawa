package cz.pojd.security.event;

import java.nio.file.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

/**
 * SecurityEvent wraps a security event triggered within the application.
 *
 * @author Lubos Housa
 * @since Nov 2, 2014 7:47:45 PM
 */
public class SecurityEvent {
    private static final Log LOG = LogFactory.getLog(SecurityEvent.class);

    private Type type;
    private Source source;
    private final DateTime when = new DateTime();
    private Path filePath;

    public Type getType() {
	return type;
    }

    public void setType(Type type) {
	this.type = type;
    }

    public Source getSource() {
	return source;
    }

    public void setSource(Source source) {
	this.source = source;
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

    /**
     * Dispose this security event and any resources it may have allocated
     */
    public void dispose() {
	// only attempt deleting the file if exists..
	if (filePath != null) {
	    try {
		filePath.toFile().delete();
	    } catch (Exception e) {
		LOG.error("Unable to delete the file at path: " + filePath, e);
	    }
	}
    }

    @Override
    public String toString() {
	return "SecurityEvent [type=" + type + ", source=" + source + ", when=" + when + ", filePath=" + filePath + "]";
    }
}
