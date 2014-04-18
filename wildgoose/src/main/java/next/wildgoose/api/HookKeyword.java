package next.wildgoose.api;

import java.util.Random;

import org.json.JSONObject;

public class HookKeyword {

	public static JSONObject getJson(String reporterId) {
		JSONObject result = new JSONObject();
		JSONObject data;
		Random random = new Random();
		data = new JSONObject().put("미모", random.nextInt(25));
		result.append("data", data);
		data = new JSONObject().put("충격", random.nextInt(25));
		result.append("data", data);
		data = new JSONObject().put("헉", random.nextInt(25));
		result.append("data", data);
		data = new JSONObject().put("경악", random.nextInt(25));
		result.append("data", data);
		data = new JSONObject().put("이럴수가", random.nextInt(25));
		result.append("data", data);
		return result;
	}

}
