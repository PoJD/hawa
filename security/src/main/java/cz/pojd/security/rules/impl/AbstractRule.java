package cz.pojd.security.rules.impl;

import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RuleDetail;

public abstract class AbstractRule implements Rule {

    private boolean enabled = true;

    @Override
    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    @Override
    public int getId() {
	return getDescription().hashCode();
    }

    @Override
    public final void updateFrom(RuleDetail ruleDetail) {
	setEnabled(ruleDetail.isEnabled());
    }

    @Override
    public String toString() {
	return "Rule [getID()=" + getId() + ", getDescription()=" + getDescription() + "]";
    }
}
