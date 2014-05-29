package next.wildgoose.framework;

import java.util.HashMap;
import java.util.Map;

public abstract class Result {
	
	protected int status;
	protected String message;
	protected Map<String, String[]> parameters;
	protected Map<String, Object> data;
	
	protected Result(Map<String, String[]> parameters) {
		this.status = 500;
		this.message = "failure";
		this.parameters = parameters;
		this.data = new HashMap<String, Object>();
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(String status) {
		setStatus(Integer.parseInt(status));
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Map<String, String[]> getParameters() {
		return parameters;
	}
	public String[] getParameter(String key) {
		return parameters.get(key);
	}
	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}
	
	public Object getData(String key) {
		return this.data.get(key);
	}
	public Map<String, Object> getData() {
		return this.data;
	}
	protected void setData(String key, Object value) {
		this.data.put(key, value);
	}
	
	
}
