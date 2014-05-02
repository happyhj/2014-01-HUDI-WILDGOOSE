package next.wildgoose.api;

import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DummyData.class.getName());
	JSONObject result = null;
	private JSONObject data = null;
	private Random random = new Random();
	
	public String getCreateAccountHtml() {
		StringBuilder account_page = new StringBuilder();
		account_page.append("<form method='post'>");
		account_page.append("<input type='email' id='email' name='email' placeholder='이메일을 입력하세요' />");
		account_page.append("<input type='password' id='password' placeholder='비밀번호를 입력하세요' />");
		account_page.append("<input type='submit' id='create' />");
		account_page.append("</form>");
		return account_page.toString();
	}
	
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

}
