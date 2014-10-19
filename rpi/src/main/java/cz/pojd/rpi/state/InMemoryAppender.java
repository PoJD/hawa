package cz.pojd.rpi.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * InMemoryAppender is special log4j appender simply logging the log events into a memory array (of limited size). This array is overwritten with new
 * log events, so servers sort of tail purpose.
 *
 * @author Lubos Housa
 * @since Oct 19, 2014 10:15:41 PM
 */
public class InMemoryAppender extends AppenderSkeleton {

    @SuppressWarnings("unchecked")
    private static final Collection<String> buffer = new CircularFifoBuffer(100);

    // TODO ugly hack to avoid complex accessing instances created by Log4J from spring
    public static synchronized List<String> getLog() {
	return new ArrayList<>(buffer);
    }

    @Override
    protected void append(LoggingEvent event) {
	synchronized (InMemoryAppender.class) {
	    buffer.add(layout.format(event));
	}
    }

    @Override
    public void close() {
	buffer.clear();
    }

    @Override
    public boolean requiresLayout() {
	return true;
    }
}
