package cz.pojd.homeautomation.model.refresh;

import java.util.Date;

import cz.pojd.homeautomation.model.lights.LightCapable;
import cz.pojd.homeautomation.model.lights.LightCapableDetail;

public class RefreshedLightCapableDetail extends LightCapableDetail {
    private Date lastUpdate;

    protected RefreshedLightCapableDetail() {
    }

    protected RefreshedLightCapableDetail(RefreshedLightCapableDetail copy) {
	super(copy);
	setLastUpdate(copy.getLastUpdate());
    }

    protected RefreshedLightCapableDetail(LightCapable lightCapable) {
	super(lightCapable);
    }

    public Date getLastUpdate() {
	return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
	this.lastUpdate = lastUpdate;
    }
}
