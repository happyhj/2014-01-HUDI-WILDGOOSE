package next.wildgoose.dao;

import java.util.Random;

import org.json.JSONObject;

public class DummyData {
	public JSONObject getJsonWithStatPoints(int reporterId) {
		JSONObject result = new JSONObject();
		JSONObject data;
		Random random = new Random();
		
		data = new JSONObject().put("꾸준함", random.nextInt(101)/10);
		result.append("data", data);
		data = new JSONObject().put("받아씀", random.nextInt(101)/10);
		result.append("data", data);
		data = new JSONObject().put("어그로", random.nextInt(101)/10);
		result.append("data", data);
		data = new JSONObject().put("강태공", random.nextInt(101)/10);
		result.append("data", data);
		data = new JSONObject().put("잡식성", random.nextInt(101)/10);
		result.append("data", data);
		data = new JSONObject().put("정의감", random.nextInt(101)/10);
		result.append("data", data);
		data = new JSONObject().put("먹튀", random.nextInt(101)/10);
		result.append("data", data);
		
		return result;
	}
	
	public String getEmail () {
		return "hello@world.com";
	}
}
