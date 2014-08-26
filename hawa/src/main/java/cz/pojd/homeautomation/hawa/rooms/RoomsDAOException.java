package cz.pojd.homeautomation.hawa.rooms;

public class RoomsDAOException extends RuntimeException {

    private static final long serialVersionUID = 7854433102686735700L;

    public RoomsDAOException(String message) {
	super(message);
    }

    public RoomsDAOException(String message, Throwable t) {
	super(message, t);
    }
}
