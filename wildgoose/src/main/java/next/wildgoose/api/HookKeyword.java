package next.wildgoose.api;

import java.util.ArrayList;

import org.json.JSONObject;

public class HookKeyword {

	public static JSONObject getJson(String reporterId) {
		JSONObject result = new JSONObject();
		JSONObject data;
		data = new JSONObject().put("미모", 23);
		result.append("data", data);
		data = new JSONObject().put("충격", 12);
		result.append("data", data);
		data = new JSONObject().put("네티즌", 3);
		result.append("data", data);
		return result;
	}

}
