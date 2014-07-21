package cz.pojd.homeautomation.hawa.state;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.hawa.state.PropertyValue.Type;

public class OsStateServiceImpl implements OsStateService {

    private static final String[] CPU_COMMAND = new String[] { "/bin/sh", "-c", "uptime | sed 's/.*average: \\([^,]*\\).*/\\1/'"};
    private static final Log LOG = LogFactory.getLog(OsStateServiceImpl.class);
    private static final String DELIMITER = System.getProperty("line.separator");

    @Override
    public PropertyValue[] getFileSystems() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PropertyValue getRam() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PropertyValue getSwap() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PropertyValue getCpu() {
	String currentLoad = runSystemCommand(CPU_COMMAND);
	if (currentLoad == null) {
	    return null;
	}

	Double load = null;
	try {
	    load = Double.parseDouble(currentLoad);
	} catch (NumberFormatException e) {
	    LOG.error("Unexpected output returned from system command " + CPU_COMMAND + ". Expected double, got: " + currentLoad, e);
	    return null;
	}
	int percentage = (int) (100 * load / Runtime.getRuntime().availableProcessors());
	return PropertyValue.newBuilder().type(Type.os).name("CPU utilization").percentage(percentage).criticalPercentage(80).textValue(percentage + "%").build();
    }

    private String runSystemCommand(String ... command) {
	try {
	    Process process = Runtime.getRuntime().exec(command);
	    int result = process.waitFor();
	    if (result != 0) {
		LOG.error("System command " + command + " returned non zero status " + result);
		LOG.error(getOutput(process.getErrorStream()));
		return null;
	    }
	    return getOutput(process.getInputStream());
	} catch (IOException | InterruptedException e) {
	    LOG.error("Unable to execute system command " + command, e);
	    return null;
	}
    }

    private String getOutput(InputStream inputStream) throws IOException {
	StringBuilder output = new StringBuilder(200);
	int line = 0;
	try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
	    if (line++ > 0) {
		output.append(DELIMITER);
	    }
	    output.append(reader.readLine());
	}
	return output.toString();
    }
}
