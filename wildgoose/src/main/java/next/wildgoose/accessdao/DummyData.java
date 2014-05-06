package next.wildgoose.accessdao;

import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DummyData.class.getName());
	JSONObject result = null;
	private JSONObject data = null;
	private Random random = new Random();
	
	
	public JSONObject getJsonWithStatPoints(int reporterId) {
		result = new JSONObject();
		
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
