package cz.pojd.security.rules.impl;

import cz.pojd.security.rules.Rule;

public abstract class AbstractRule implements Rule {

    @Override
    public int getID() {
	return getDescription().hashCode();
    }

    @Override
    public String toString() {
	return "Rule [getID()=" + getID() + ", getDescription()=" + getDescription() + "]";
    }
}
