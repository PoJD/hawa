package cz.pojd.homeautomation.hawa.refresh;

import cz.pojd.homeautomation.hawa.lights.LightCapable;
import cz.pojd.homeautomation.hawa.lights.LightCapableDetail;

public class RefreshedLightCapableDetail extends LightCapableDetail {
    private String lastUpdate;

    protected RefreshedLightCapableDetail() {
    }

    protected RefreshedLightCapableDetail(RefreshedLightCapableDetail copy) {
	super(copy);
	setLastUpdate(copy.getLastUpdate());
    }

    protected RefreshedLightCapableDetail(LightCapable lightCapable) {
	super(lightCapable);
    }

    public String getLastUpdate() {
	return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
	this.lastUpdate = lastUpdate;
    }
}
