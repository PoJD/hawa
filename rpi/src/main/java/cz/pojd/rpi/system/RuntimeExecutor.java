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
     * Execute the command, validate the output of the command (e.g. error stream) and exit
     * 
     * @param command command to execute
     * @return true if the process was OK, false otherwise
     */
    public boolean executeNoReturn(String command);

    /**
     * Detects CPU count on this machine
     * 
     * @return number of CPU cores
     */
    public int getCpuCount();
}
