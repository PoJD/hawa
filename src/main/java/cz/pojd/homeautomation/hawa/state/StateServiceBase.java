package cz.pojd.homeautomation.hawa.state;

/**
 * Base class for state services
 * 
 * @author Lubos Housa
 * @since Jul 22, 2014 11:23:53 AM
 */
public abstract class StateServiceBase {

    private static final long KB = 1024l;
    private static final long MB = KB * KB;

    protected String doubles2Range(double size1, double size2) {
	return longs2Range((long) size1, (long) size2);
    }

    protected String longs2Range(long size1, long size2) {
	return long2String(size1) + "/" + long2String(size2);
    }

    /**
     * Transforms the in passed long (expected in raw bytes) into some human readable form. Currently works with MB and GB only.
     * @param size size to transform
     * @return human readable form
     */
    protected String long2String(long size) {
	long sizeMB = (size / MB);
	if (sizeMB > KB) {
	    return (sizeMB / KB) + "G";
	} else {
	    return sizeMB + "M";
	}
    }
}
