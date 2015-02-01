package cz.pojd.rpi.state;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.inject.Inject;

import cz.pojd.rpi.state.PropertyValue.Type;
import cz.pojd.rpi.system.RuntimeExecutor;

public class OsStateServiceImpl extends StateServiceBase implements OsStateService {

    private static final String UPTIME_COMMAND = "uptime | sed 's/\\([^,]*\\).*/\\1/' | sed 's/^ *//'";
    private static final String UPTIME_LABEL = "Uptime";

    private static final String CPU_COMMAND = "uptime | sed 's/.*average: \\([^,]*\\).*/\\1/'";
    private static final String CPU_LABEL = "CPU";

    private static final String RAM_COMMAND = "free -b | grep buffers\\/cache | sed 's/.* \\([^ ]*\\)/\\1/' && free -b | grep Mem: | sed 's/Mem: *\\([^ ]*\\).*/\\1/'";
    private static final String RAM_LABEL = "RAM";

    private static final String SWAP_COMMAND = "free -b | grep Swap | sed 's/.* \\([^ ]*\\)/\\1/' && free -b | grep Swap: | sed 's/Swap: *\\([^ ]*\\).*/\\1/'";
    private static final String SWAP_LABEL = "Swap";

    private static final String SYSTEM_LOG_COMMAND = "dmesg | tail -100";

    private static final String SHUTDOWN_COMMAND = "shutdown -h now";

    private static final String FILESYSTEM_COMMAND = "df FS --block-size=1 --output=avail | tail -1 && df FS --block-size=1 --output=size | tail -1";
    private static final Pattern pattern = Pattern.compile("FS");

    @Resource(name = "fileSystems")
    private List<String> fileSystemsNames;

    @Resource(name = "detectDbIsRunningCommand")
    private String detectDbCommand;

    @Inject
    private RuntimeExecutor runtimeExecutor;

    public List<String> getFileSystemsNames() {
	return fileSystemsNames;
    }

    public void setFileSystemsNames(List<String> fileSystemsNames) {
	this.fileSystemsNames = fileSystemsNames;
    }

    public String getDetectDbCommand() {
	return detectDbCommand;
    }

    public void setDetectDbCommand(String detectDbCommand) {
	this.detectDbCommand = detectDbCommand;
    }

    public RuntimeExecutor getRuntimeExecutor() {
	return runtimeExecutor;
    }

    public void setRuntimeExecutor(RuntimeExecutor runtimeExecutor) {
	this.runtimeExecutor = runtimeExecutor;
    }

    @Override
    public Iterable<PropertyValue> getFileSystems() {
	List<PropertyValue> result = new ArrayList<>();
	for (String fileSystem : getFileSystemsNames()) {
	    Matcher matcher = pattern.matcher(FILESYSTEM_COMMAND);
	    String command = matcher.replaceAll(fileSystem);
	    PropertyValue value = getRangeValue(fileSystem, command);
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
	List<Double> currentLoad = getRuntimeExecutor().executeDouble(CPU_COMMAND);
	if (currentLoad.isEmpty()) {
	    return null;
	}

	double percentage = 100 * currentLoad.get(0) / (double) getRuntimeExecutor().getCpuCount();
	return PropertyValue.newBuilder()
		.type(Type.os)
		.name(CPU_LABEL)
		.percentage((int) percentage)
		.criticalPercentage(80)
		.textValue(String.format("%.2f%%", percentage))
		.build();
    }

    @Override
    public PropertyValue getUptime() {
	List<String> uptime = getRuntimeExecutor().executeString(UPTIME_COMMAND);
	if (uptime.size() != 1) {
	    return null;
	}
	return PropertyValue.newBuilder()
		.type(Type.os)
		.name(UPTIME_LABEL)
		.noPercentage()
		.textValue(uptime.get(0))
		.build();
    }

    @Override
    public List<String> getSystemLog() {
	return getRuntimeExecutor().executeString(SYSTEM_LOG_COMMAND);
    }

    @Override
    public List<String> getApplicationLog() {
	// TODO just static access for now
	return InMemoryAppender.getLog();
    }

    private PropertyValue getRangeValue(String label, String command) {
	List<Double> range = getRuntimeExecutor().executeDouble(command);
	if (range.size() < 2) {
	    return null;
	}

	double free = range.get(0);
	double total = range.get(1);
	double used = total - free;
	int percentage = (int) (total > 0 ? (100 * used / total) : 0);
	return PropertyValue.newBuilder()
		.type(Type.os)
		.name(label)
		.percentage(percentage)
		.criticalPercentage(90)
		.textValue(doubles2Range(used, total))
		.build();
    }

    @Override
    public void shutdownRpi() {
	getRuntimeExecutor().executeNoReturn(SHUTDOWN_COMMAND);
    }

    @Override
    public boolean isDbRunning() {
	List<String> result = getRuntimeExecutor().executeString(detectDbCommand);
	if (result.size() == 1) {
	    return Boolean.valueOf(result.get(0));
	} else {
	    return false;
	}
    }
}
