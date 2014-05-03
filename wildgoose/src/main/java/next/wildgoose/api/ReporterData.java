package next.wildgoose.api;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.HookingKeywordDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.dummy.DummyData;

import org.json.JSONObject;

public class ReporterData {
	
	public JSONObject getJSON(HttpServletRequest request, int reporterId, String apiName, String condition) {
		
		// sevlet context
		ServletContext context = request.getSession().getServletContext();
		
		DummyData dummy = (DummyData) context.getAttribute("dummy");
		NumberOfArticlesDAO numberOfArticlesDao = (NumberOfArticlesDAO) context.getAttribute("numberOfArticlesDAO");
		HookingKeywordDAO hkDao = (HookingKeywordDAO) context.getAttribute("hookingKeywordDAO");
		
		/*
		 * 이하 graph
		 */
		if ("number_of_hook_keywords".equals(apiName)) {
			return hkDao.getHookingKeywordsCount(reporterId);
		}
		
		if ("number_of_articles".equals(apiName)) {
			// when by is null, day is default value
			if (condition == null) {
				condition = "day";
			}
			
			if ("section".equals(condition)) {
				return numberOfArticlesDao.bySection(reporterId);
			}
			
			if ("day".equals(condition)) {
				return numberOfArticlesDao.byDay(reporterId);
			}
		}
		
		if ("stat_points".equals(apiName)) {
			return dummy.getJsonWithStatPoints(reporterId);
		}
		
		return null;
	}
}
