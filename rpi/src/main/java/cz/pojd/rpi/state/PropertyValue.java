package cz.pojd.rpi.state;

/**
 * PropertyValue is a wrapper for a particular property value to be determined
 * by some of the rest services. For example heap size in VM, hard drives usage
 * in the OS, etc.
 * 
 * @author Lubos Housa
 * @since Jul 21, 2014 12:01:55 AM
 */
public class PropertyValue {

    public static enum Type {
	jvm, os;
    }

    public static class Builder {
	private Type type;
	private String name;
	private String textValue;
	private int percentage;
	private int criticalPercentage;

	private Builder() {
	}

	public Builder type(Type type) {
	    this.type = type;
	    return this;
	}

	public Builder name(String name) {
	    this.name = name;
	    return this;
	}

	public Builder textValue(String textValue) {
	    this.textValue = textValue;
	    return this;
	}

	public Builder percentage(int percentage) {
	    this.percentage = percentage;
	    return this;
	}

	public Builder criticalPercentage(int criticalPercentage) {
	    this.criticalPercentage = criticalPercentage;
	    return this;
	}

	public PropertyValue build() {
	    PropertyValue value = new PropertyValue();
	    value.type = type;
	    value.name = name;
	    value.textValue = textValue;
	    value.percentage = percentage;
	    value.criticalPercentage = criticalPercentage;
	    return value;
	}
    }

    public static Builder newBuilder() {
	return new Builder();
    }

    private Type type;
    private String name;
    private String textValue;
    private int percentage;
    private int criticalPercentage;

    public Type getType() {
	return type;
    }

    public String getName() {
	return name;
    }

    public String getTextValue() {
	return textValue;
    }

    public int getPercentage() {
	return percentage;
    }

    public int getCriticalPercentage() {
	return criticalPercentage;
    }

    @Override
    public String toString() {
	return "PropertyValue [type=" + type + ", name=" + name + ", textValue=" + textValue + ", percentage=" + percentage + ", criticalPercentage=" + criticalPercentage + "]";
    }
}
