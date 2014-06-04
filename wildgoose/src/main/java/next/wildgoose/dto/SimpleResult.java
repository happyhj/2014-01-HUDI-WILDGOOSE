package next.wildgoose.dto;

import next.wildgoose.framework.Result;

public class SimpleResult extends Result {

	public SimpleResult() {
		super();
	}
	public void setData(String key, Object value) {
		super.data.put(key, value);
	}
}
