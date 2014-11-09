package cz.pojd.security.rules;

/**
 * Represents a rule detail - a DTO to display e.g. in the web
 *
 * @author Lubos Housa
 * @since Nov 9, 2014 12:40:34 PM
 */
public class RuleDetail {

    private int id;
    private String description;
    private boolean enabled;

    public RuleDetail() {
    }

    public RuleDetail(Rule rule) {
	setId(rule.getId());
	setDescription(rule.getDescription());
	setEnabled(rule.isEnabled());
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    @Override
    public String toString() {
	return "RuleDetail [id=" + id + ", description=" + description + ", enabled=" + enabled + "]";
    }
}
