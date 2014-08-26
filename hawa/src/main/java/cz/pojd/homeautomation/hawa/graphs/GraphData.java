package cz.pojd.homeautomation.hawa.graphs;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Wrapper for graph data - used to retrieve for NVD3 charts via Rest
 * 
 * @author Lubos Housa
 * @since Aug 25, 2014 11:47:45 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphData {

    private String key;
    private String color;
    private Object[][] values;

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    public Object[][] getValues() {
	return values;
    }

    public void setValues(Object[][] values) {
	this.values = values;
    }

    @Override
    public String toString() {
	return "GraphData [key=" + key + ", color=" + color + ", values=" + Arrays.toString(values) + "]";
    }
}
