package cz.pojd.rpi.system;

import java.util.List;

/**
 * RuntimeExecutor is a general executor abstraction to execute some commands using runtime
 * 
 * @author Lubos Housa
 * @since Jul 30, 2014 12:19:44 AM
 */
public interface RuntimeExecutor {

    /**
     * Execute the command and return the list of double integers as a result
     * 
     * @param command
     *            command to execute
     * @return list of double integers
     */
    public List<Double> execute(String command);

    /**
     * Detects CPU count on this machine
     * 
     * @return number of CPU cores
     */
    public int getCpuCount();
}
