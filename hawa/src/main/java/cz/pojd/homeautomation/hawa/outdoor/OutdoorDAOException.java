package cz.pojd.homeautomation.hawa.outdoor;

public class OutdoorDAOException extends RuntimeException {

    private static final long serialVersionUID = 2482333655229197593L;

    public OutdoorDAOException(String message) {
	super(message);
    }

    public OutdoorDAOException(String message, Throwable t) {
	super(message, t);
    }
}
