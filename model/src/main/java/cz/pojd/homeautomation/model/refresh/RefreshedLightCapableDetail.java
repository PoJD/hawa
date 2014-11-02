package cz.pojd.homeautomation.model.refresh;

import cz.pojd.homeautomation.model.lights.LightCapable;
import cz.pojd.homeautomation.model.lights.LightCapableDetail;

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
