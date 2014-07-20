package cz.pojd.homeautomation.hawa.rest;

/**
 * VmStateProperty is a wrapper for a particular property describing virtual machine state (e.g. heap size)
 *
 * @author Lubos Housa
 * @since Jul 21, 2014 12:01:55 AM
 */
public class VmStateProperty {

    public enum Type {
	heap, processors;
    }
    
    private final Type type;
    private final long currentValue;
    private final long maxValue;
    private final String textValue;
    
    VmStateProperty(Type type, long currentValue, long maxValue, String textValue)  {
	this.type = type;
	this.currentValue = currentValue;
	this.maxValue = maxValue;
	this.textValue = textValue;
    }

    public Type getType() {
        return type;
    }
    
    public String getName() {
	return getType().name();
    }

    public long getCurrentValue() {
        return currentValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public String getTextValue() {
        return textValue;
    }
}
