package cz.pojd.rpi.state;



/**
 * Detects the state of the underlying operating system
 *
 * @author Lubos Housa
 * @since Jul 21, 2014 11:38:55 AM
 */
public interface OsStateService {

    /**
     * Detects all the configured file systems and read their state (usage mostly)
     * @return list of file system states
     */
    Iterable<PropertyValue> getFileSystems();
    
    /**
     * Detects current RAM utilization
     * @return current RAM utilization
     */
    PropertyValue getRam();
    
    /**
     * Detects current swap utilization
     * @return current swap utilization
     */
    PropertyValue getSwap();
    
    /**
     * Detects current CPU utilization
     * @return current CPU utilization
     */
    PropertyValue getCpu();
}
