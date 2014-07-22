package cz.pojd.homeautomation.hawa.state;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import cz.pojd.homeautomation.hawa.state.PropertyValue.Type;

public class OsStateServiceImpl extends StateServiceBase implements OsStateService {

    private static final Log LOG = LogFactory.getLog(OsStateServiceImpl.class);

    private static final String[] CPU_COMMAND = new String[] { "/bin/sh", "-c", "uptime | sed 's/.*average: \\([^,]*\\).*/\\1/'" };
    private static final String CPU_LABEL = "CPU";

    private static final String[] RAM_COMMAND = new String[] { "/bin/sh", "-c",
	    "free -b | grep buffers\\/cache | sed 's/.* \\([^ ]*\\)/\\1/' && free -b | grep Mem: | sed 's/Mem: *\\([^ ]*\\).*/\\1/'" };
    private static final String RAM_LABEL = "RAM";

    private static final String[] SWAP_COMMAND = new String[] { "/bin/sh", "-c", "free -b | grep Swap | sed 's/.* \\([^ ]*\\)/\\1/' && free -b | grep Swap: | sed 's/Swap: *\\([^ ]*\\).*/\\1/'" };
    private static final String SWAP_LABEL = "Swap";

    private static final String[] FILESYSTEM_COMMAND = new String[] { "/bin/sh", "-c", "df FS --block-size=1 --output=avail | tail -1 && df FS --block-size=1 --output=size | tail -1" };
    private static final Pattern pattern = Pattern.compile("FS");

    private final List<String> fileSystems;

    public OsStateServiceImpl(List<String> fileSystems) {
	this.fileSystems = fileSystems;
	LOG.info("About to check file systems: " + fileSystems);
    }

    @Override
    public Iterable<PropertyValue> getFileSystems() {
	List<PropertyValue> result = new ArrayList<>();
	for (String fileSystem : fileSystems) {
	    Matcher matcher = pattern.matcher(FILESYSTEM_COMMAND[2]);
	    String command = matcher.replaceAll(fileSystem);
	    PropertyValue value = getRangeValue(fileSystem, new String[] { FILESYSTEM_COMMAND[0], FILESYSTEM_COMMAND[1], command });
	    if (value != null) {
		result.add(value);
	    }
	}
	return result;
    }

    @Override
    public PropertyValue getRam() {
	return getRangeValue(RAM_LABEL, RAM_COMMAND);
    }

    @Override
    public PropertyValue getSwap() {
	return getRangeValue(SWAP_LABEL, SWAP_COMMAND);
    }

    @Override
    public PropertyValue getCpu() {
	List<Double> currentLoad = runSystemCommand(CPU_COMMAND);
	if (currentLoad.isEmpty()) {
	    return null;
	}

	DecimalFormat format = new DecimalFormat("0.00");
	double percentage = 100 * currentLoad.get(0) / (double) Runtime.getRuntime().availableProcessors();
	return PropertyValue.newBuilder().type(Type.os).name(CPU_LABEL).percentage((int) percentage).criticalPercentage(80).textValue(format.format(percentage) + "%").build();
    }

    private PropertyValue getRangeValue(String label, String... command) {
	List<Double> range = runSystemCommand(command);
	if (range.size() < 2) {
	    return null;
	}

	double free = range.get(0);
	double total = range.get(1);
	double used = total - free;
	int percentage = (int) (total > 0 ? (100 * used / total) : 0);
	return PropertyValue.newBuilder().type(Type.os).name(label).percentage(percentage).criticalPercentage(90).textValue(doubles2RangeMB(used, total)).build();
    }

    private List<Double> runSystemCommand(String... command) {
	List<Double> output = new ArrayList<>();
	String commaSeparatedCommand = StringUtils.arrayToCommaDelimitedString(command);
	try {
	    Process process = Runtime.getRuntime().exec(command);
	    process.waitFor();
	    // since we are pipelining more commands, they always return 0 even if some of them failed. So need to check the error stream to detect the result
	    Collection<String> error = getOutput(process.getErrorStream());
	    if (!error.isEmpty()) {
		LOG.error("System command " + commaSeparatedCommand + " returned error.");
		LOG.error(StringUtils.collectionToCommaDelimitedString(error));
		return output;
	    }
	    for (String s : getOutput(process.getInputStream())) {
		if (s != null) {
		    output.add(Double.parseDouble(s));
		}
	    }
	} catch (IOException | InterruptedException | NumberFormatException e) {
	    LOG.error("Unable to execute/parse system command " + commaSeparatedCommand, e);
	}
	return output;
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
