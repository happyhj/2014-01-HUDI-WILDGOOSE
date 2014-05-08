package next.wildgoose.service;

import next.wildgoose.utility.Constants;

public class ActionResult {
	private boolean isRedirect = false;
	private String path = null;
	
	public void setForwardingOption(boolean isRedirect, String path) {
		this.isRedirect = isRedirect;
		this.path = path;
	}
	
	public boolean isRedirect() {
		return this.isRedirect;
	}
	public String getPath() {
		return Constants.RESOURCE_ROOT + this.path;
	}
}
