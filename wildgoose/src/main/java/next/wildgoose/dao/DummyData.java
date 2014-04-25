package next.wildgoose.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyData {
	
	private static final Logger logger = LoggerFactory.getLogger(DummyData.class.getName());
	JSONObject result = null;
	private JSONObject data = null;
	private Random random = new Random();
	
	public JSONObject getJsonWithNumberOfArticlesBy(int reporterId, String condition) {
		
		logger.debug(this.getClass().getName() + condition);
		if ("section".equals(condition)) {
			List<String> section = new ArrayList<String>();
			section.add("사회");
			section.add("경제");
			section.add("국제");
			section.add("교양");
			section.add("사설");
			
			result = new JSONObject();
			JSONObject subJsonObj = null;
			
			for (int i=0; i<section.size(); i++) {
				subJsonObj = new JSONObject();
				
				subJsonObj.put("label", section.get(i));
				subJsonObj.put("value", random.nextInt(25));
				result.append("data", subJsonObj);
			}
		}
		
		return result;
	}
	
	public JSONObject getJsonWithNumberOfHookKeywords(int reporterId) {
		result = new JSONObject();
		
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
	
	public JSONObject getJsonWithNumberOfArticleByDay(int reporterId) {
		result = new JSONObject();
				
		data = new JSONObject().put("04/18", random.nextInt(4));
		result.append("data", data);
		data = new JSONObject().put("04/19", random.nextInt(4));
		result.append("data", data);
		data = new JSONObject().put("04/20", random.nextInt(4));
		result.append("data", data);
		data = new JSONObject().put("04/21", random.nextInt(4));
		result.append("data", data);
		data = new JSONObject().put("04/22", random.nextInt(4));
		result.append("data", data);
		data = new JSONObject().put("04/23", random.nextInt(4));
		result.append("data", data);
		data = new JSONObject().put("04/24", random.nextInt(4));
		result.append("data", data);
		
		return result;
	}

}
