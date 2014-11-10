package cz.pojd.security.event;

public class SecurityEventDAOException extends RuntimeException {

    private static final long serialVersionUID = 4333605726077972069L;

    public SecurityEventDAOException(String message) {
	super(message);
    }

    public SecurityEventDAOException(String message, Throwable t) {
	super(message, t);
    }
}
