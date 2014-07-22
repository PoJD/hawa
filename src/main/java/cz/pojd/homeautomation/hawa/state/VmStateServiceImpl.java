package cz.pojd.homeautomation.hawa.state;

import cz.pojd.homeautomation.hawa.state.PropertyValue.Type;

public class VmStateServiceImpl extends StateServiceBase implements VmStateService {
    private static final String PROCESSOR = "processor";
    private static final String HEAP = "heap";

    @Override
    public PropertyValue getHeap() {
	long total = Runtime.getRuntime().totalMemory();
	long used = total - Runtime.getRuntime().freeMemory();
	return PropertyValue.newBuilder()
		.type(Type.jvm)
		.name(HEAP)
		.percentage((int)(100 * (used / (double)total)))
		.criticalPercentage(90)
		.textValue(longs2Range(used, total))
		.build();
    }

    @Override
    public PropertyValue getProcessors() {
	int processorsCount = Runtime.getRuntime().availableProcessors();
	return PropertyValue.newBuilder()
		.type(Type.jvm)
		.name(PROCESSOR)
		.percentage(100)
		.criticalPercentage(110) // we don't want this value to turn red anytime
		.textValue(processorsCount + " " + PROCESSOR + (processorsCount>1 ? "s" : ""))
		.build();
    }
}
