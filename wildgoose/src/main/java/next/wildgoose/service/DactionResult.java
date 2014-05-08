package next.wildgoose.service;

import org.json.JSONObject;

public class DactionResult {
	String dataType;
	JSONObject result;
	
	public DactionResult(String dataType, JSONObject result) {
		this.dataType = dataType;
		this.result = result;
	}
	public DactionResult(String dataType, String result) {
		this.dataType = dataType;
		this.result = new JSONObject().put(dataType, result);
	}
	
	public String getDataType() {
		return this.dataType;
	}
	
	public String getData() {
		if ("json".equals(dataType)) {
			return result.toString();
		}
		else return result.getString(dataType);
	}
}
