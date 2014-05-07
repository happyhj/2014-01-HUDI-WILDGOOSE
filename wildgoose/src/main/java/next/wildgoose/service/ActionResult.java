package next.wildgoose.service;

import java.util.HashMap;

import next.wildgoose.utility.Constants;

public class ActionResult {
	private boolean isRedirect = false;
	private String path = null;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	
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
	
	public void setAttribute(String key, Object value) {
		map.put(key, value);
	}
	public HashMap<String, Object> getAttribute() {
		return map;
	}
	
}
