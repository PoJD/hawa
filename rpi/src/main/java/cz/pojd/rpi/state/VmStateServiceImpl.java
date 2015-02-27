package cz.pojd.rpi.state;

import javax.inject.Inject;

import cz.pojd.rpi.state.PropertyValue.Type;

public class VmStateServiceImpl extends StateServiceBase implements VmStateService {
    private static final String PROCESSOR = "processors";
    private static final String HEAP = "heap";

    @Inject
    private Runtime runtime;

    @Override
    public PropertyValue getHeap() {
	long total = runtime.totalMemory();
	long used = total - Runtime.getRuntime().freeMemory();
	return PropertyValue.newBuilder()
		.type(Type.jvm)
		.name(HEAP)
		.percentage((int) (100 * (used / (double) total)))
		.criticalPercentage(90)
		.textValue(longs2Range(used, total))
		.build();
    }

    @Override
    public PropertyValue getProcessors() {
	int processorsCount = runtime.availableProcessors();
	return PropertyValue.newBuilder()
		.type(Type.jvm)
		.name(PROCESSOR)
		.noPercentage() // we don't want this value to turn red anytime
		.textValue(processorsCount + "")
		.build();
    }
}
