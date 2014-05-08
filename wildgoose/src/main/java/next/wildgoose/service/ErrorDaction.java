package next.wildgoose.service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public class ErrorDaction implements Daction {

	public DactionResult execute(HttpServletRequest request) {
		return new DactionResult("text", new JSONObject().put("text", "error"));
	}

}
