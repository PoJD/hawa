package cz.pojd.homeautomation.hawa.rest.state;


/**
 * Detects the state of the virtual machine
 * 
 * @author Lubos Housa
 * @since Jul 21, 2014 10:57:42 AM
 */
public interface VmStateService {

    PropertyValue getHeap();
    PropertyValue getProcessors();
}
