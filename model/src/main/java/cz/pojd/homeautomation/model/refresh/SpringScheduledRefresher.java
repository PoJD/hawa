package cz.pojd.homeautomation.model.refresh;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Implementation of Refresher using Spring annotation @Scheduled to invoke some refreshing logic
 * 
 * @author Lubos Housa
 * @since Aug 19, 2014 5:39:52 PM
 */
public class SpringScheduledRefresher implements Refresher {

    private static final Log LOG = LogFactory.getLog(SpringScheduledRefresher.class);

    private final List<Refreshable> refreshableList = new ArrayList<>();
    private final DateTimeFormatter formatter;

    private LocalDateTime lastUpdate;

    @Inject
    public SpringScheduledRefresher(String datePattern) {
	formatter = DateTimeFormat.forPattern(datePattern);
    }

    /**
     * Run refresh logic every 5 mins and refresh all injected refreshable interfaces
     * 
     * @see cz.pojd.homeautomation.model.refresh.Refresher#refresh()
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Override
    public void refresh() {
	LOG.info("Refresh triggered in SpringScheduledRefresher - notifying all refreshable instances......");
	lastUpdate = new LocalDateTime();
	for (Refreshable refreshable : refreshableList) {
	    refreshable.refresh(lastUpdate);
	}
	LOG.info("Refresh finished.");
    }

    @Override
    public String getLastUpdate() {
	return formatter.print(lastUpdate);
    }

    @Override
    public void register(Refreshable refreshable) {
	refreshableList.add(refreshable);
    }
}
