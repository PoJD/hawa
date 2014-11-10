package cz.pojd.homeautomation.model;

/**
 * Generic Marker interface for DAOs. Used mostly to have some common constants available in DAOs.
 *
 * @author Lubos Housa
 * @since Nov 10, 2014 1:49:28 PM
 */
public interface DAO {

    /**
     * How many days back in the history should a DAO search
     */
    int DAYS_BACK_HISTORY = 7;
}
