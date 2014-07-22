package cz.pojd.homeautomation.hawa.state;

/**
 * Base class for state services
 * 
 * @author Lubos Housa
 * @since Jul 22, 2014 11:23:53 AM
 */
public abstract class StateServiceBase {

    private static final long MB = 1024l * 1024l;

    protected String doubles2RangeMB(double size1, double size2) {
	return longs2RangeMB((long)size1, (long)size2);
    }

    protected String longs2RangeMB(long size1, long size2) {
	return long2String(size1) + "/" + long2String(size2);
    }

    protected String long2String(long size) {
	return (size / MB) + "MB";
    }
}
