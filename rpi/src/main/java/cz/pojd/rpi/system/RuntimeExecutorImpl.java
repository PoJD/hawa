package cz.pojd.rpi.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * RuntimeExecutorImpl is an implementation of RuntimeExecutor using java.lang.Runtime
 * 
 * @author Lubos Housa
 * @since Jul 30, 2014 12:19:44 AM
 */
public class RuntimeExecutorImpl implements RuntimeExecutor {
    private static final Log LOG = LogFactory.getLog(RuntimeExecutorImpl.class);
    private static final String[] COMMAND_START = new String[] { "/bin/sh", "-c" };

    @Inject
    private Runtime runtime;

    public Runtime getRuntime() {
	return runtime;
    }

    public void setRuntime(Runtime runtime) {
	this.runtime = runtime;
    }

    @Override
    public int getCpuCount() {
	return getRuntime().availableProcessors();
    }

    @Override
    public boolean executeNoReturn(String command) {
	return executeCommand(command) != null;
    }

    @Override
    public List<Double> execute(String commandSimple) {
	List<Double> output = new ArrayList<>();
	try {
	    Process process = executeCommand(commandSimple);
	    if (process != null) {
		for (String s : getOutput(process.getInputStream())) {
		    output.add(Double.parseDouble(s));
		}
	    }
	} catch (IOException e) {
	    LOG.error("Unable to parse output of system command " + commandSimple, e);
	}
	return output;
    }

    private Process executeCommand(String commandSimple) {
	String[] command = new String[] { COMMAND_START[0], COMMAND_START[1], commandSimple };
	String commaSeparatedCommand = StringUtils.arrayToCommaDelimitedString(command);
	try {
	    Process process = getRuntime().exec(command);
	    process.waitFor();

	    // since we might pipelining more commands, they always return 0 even if some of them failed. So need to check the error stream to detect
	    // the result
	    Collection<String> error = getOutput(process.getErrorStream());
	    if (error.isEmpty()) {
		return process;
	    } else {
		LOG.error("System command " + commaSeparatedCommand + " returned error.");
		LOG.error(StringUtils.collectionToCommaDelimitedString(error));
	    }
	} catch (IOException | InterruptedException | NumberFormatException e) {
	    LOG.error("Unable to execute/parse system command " + commaSeparatedCommand, e);
	}
	return null;
    }

    private Collection<String> getOutput(InputStream inputStream) throws IOException {
	List<String> output = new ArrayList<>();
	try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
	    String line = null;
	    while ((line = reader.readLine()) != null) {
		output.add(line);
	    }
	}
	return output;
    }
}
