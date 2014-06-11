package next.wildgoose.dto;

import next.wildgoose.framework.Result;

public class SimpleResult extends Result {

	public SimpleResult() {
		super();
	}
	public SimpleResult(boolean success) {
		super(success);
	}
	public SimpleResult(boolean success, String pageName) {
		super(success);
		this.setPageName(pageName);
	}
	public void setData(String key, Object value) {
		super.data.put(key, value);
	}
}
