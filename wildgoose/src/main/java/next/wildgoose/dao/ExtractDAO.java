package next.wildgoose.dao;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public interface ExtractDAO {
	JSONObject getJson(HttpServletRequest request);
}
