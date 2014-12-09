package cz.pojd.security.event;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Value;

import cz.pojd.homeautomation.model.dao.DAOImpl;
import cz.pojd.homeautomation.model.dao.StorageCleanup;

/**
 * Simple implementation of SecurityEventDAO using spring JDBC support
 *
 * @author Lubos Housa
 * @since Nov 10, 2014 3:13:12 PM
 */
public class SecurityEventDAOImpl extends DAOImpl implements SecurityEventDAO {

    @Value("${sql.securityEventDAO.insert}")
    private String insertSql;
    @Value("${sql.securityEventDAO.query}")
    private String querySql;

    private final SourceResolver sourceResolver;

    @Inject
    public SecurityEventDAOImpl(StorageCleanup storageCleanup, SourceResolver sourceResolver) {
	storageCleanup.registerTable("securityevents");
	this.sourceResolver = sourceResolver;
    }

    public String getInsertSql() {
	return insertSql;
    }

    public void setInsertSql(String insertSql) {
	this.insertSql = insertSql;
    }

    public String getQuerySql() {
	return querySql;
    }

    public void setQuerySql(String querySql) {
	this.querySql = querySql;
    }

    public SourceResolver getSourceResolver() {
	return sourceResolver;
    }

    @Override
    public Collection<SecurityEvent> query(DateTime start, DateTime end) {
	DateTimeFormatter parser = ISODateTimeFormat.dateTime();
	String startString = parser.print(start);
	String endString = parser.print(end);
	List<SecurityEvent> result = new ArrayList<>();
	try {
	    for (Map<String, Object> row : getJdbcTemplate().queryForList(querySql, new Object[] { startString, endString })) {
		SecurityEvent event = new SecurityEvent();
		event.setFilePath(parsePathFromRow(row, "filepath"));
		event.setDetail(parseStringFromRow(row, "detail"));
		event.setSource(sourceResolver.resolve(parseStringFromRow(row, "source")));
		event.setType(parseEnumFromRow(row, Type.class, "type"));
		event.setAt(parseDateTimeFromRow(row, "at"));
		result.add(event);
	    }
	} catch (Exception e) {
	    throw new SecurityEventDAOException("Unable to query the security events for range " + startString + " -> " + endString, e);
	}
	return result;
    }

    @Override
    public void save(SecurityEvent securityEvent) {
	try {
	    getJdbcTemplate().update(
		    insertSql,
		    securityEvent.getType() != null ? securityEvent.getType().getId() : null,
		    securityEvent.getSource() != null ? securityEvent.getSource().getId() : null,
		    securityEvent.getAtAsDate(),
		    securityEvent.getFilePath() != null ? securityEvent.getFilePath().toString() : null,
		    securityEvent.getDetail());
	} catch (Exception e) {
	    throw new SecurityEventDAOException("Unable to save the security event " + securityEvent, e);
	}
    }

    private Path parsePathFromRow(Map<String, Object> row, String columnName) {
	String value = parseStringFromRow(row, columnName);
	return value != null ? Paths.get(value) : null;
    }

    private DateTime parseDateTimeFromRow(Map<String, Object> row, String columnName) {
	Timestamp value = (Timestamp) row.get(columnName);
	return value != null ? new DateTime(value.getTime()) : null;
    }

    private <T extends Enum<T>> T parseEnumFromRow(Map<String, Object> row, Class<T> enumType, String columnName) {
	String value = parseStringFromRow(row, columnName);
	return (value != null) ? Enum.valueOf(enumType, value) : null;
    }

    private String parseStringFromRow(Map<String, Object> row, String columnName) {
	Object value = row.get(columnName);
	return (value != null) ? value.toString() : null;
    }
}
