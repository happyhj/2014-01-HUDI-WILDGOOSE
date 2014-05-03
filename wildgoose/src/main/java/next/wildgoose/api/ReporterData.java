package next.wildgoose.api;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.HookingKeywordDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.dummy.DummyData;

import org.json.JSONObject;

public class ReporterData {
	
	public JSONObject getJSON(HttpServletRequest request, int reporterId, String apiName, String condition) {
		JSONObject result = null;
		
		// sevlet context
		ServletContext context = request.getSession().getServletContext();
		
		DummyData dummy = (DummyData) context.getAttribute("dummy");
		NumberOfArticlesDAO numberOfArticlesDao = (NumberOfArticlesDAO) context.getAttribute("numberOfArticlesDAO");
		HookingKeywordDAO hkDao = (HookingKeywordDAO) context.getAttribute("hookingKeywordDAO");
		
		if ("number_of_hook_keywords".equals(apiName)) {
			result = hkDao.getHookingKeywordsCount(reporterId);
			if (result == null) {
				// DO Something...
			}
		}
		else if ("number_of_articles".equals(apiName)) {
			// when by is null, day is default value
			if (condition == null) {
				condition = "day";
			}
			
			if ("section".equals(condition)) {
				result = numberOfArticlesDao.bySection(reporterId);
			} else if ("day".equals(condition)) {
				result = numberOfArticlesDao.byDay(reporterId);
			}
		}
		else if ("stat_points".equals(apiName)) {
			result = dummy.getJsonWithStatPoints(reporterId);
		}
		
		return result;
	}
}
