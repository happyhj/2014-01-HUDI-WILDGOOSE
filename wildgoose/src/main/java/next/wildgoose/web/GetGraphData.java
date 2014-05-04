package next.wildgoose.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.HookingKeywordDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.model.DummyData;

import org.json.JSONObject;

public class GetGraphData {
	
	public JSONObject getJSON(HttpServletRequest request, int reporterId, String apiName, String condition) {
		
		// sevlet context
		ServletContext context = request.getSession().getServletContext();
		
		DummyData dummy = (DummyData) context.getAttribute("dummy");
		NumberOfArticlesDAO numberOfArticlesDao = (NumberOfArticlesDAO) context.getAttribute("numberOfArticlesDAO");
		HookingKeywordDAO hkDao = (HookingKeywordDAO) context.getAttribute("hookingKeywordDAO");
		
		if (condition == null) {
			condition = "day";
		}
		/*
		 * 이하 graph
		 */
		if ("number_of_hook_keywords".equals(apiName)) {
			return hkDao.getHookingKeywordsCount(reporterId);
		} else if ("number_of_articles".equals(apiName)) {
			if ("section".equals(condition)) {
				return numberOfArticlesDao.bySection(reporterId);
			} else if ("day".equals(condition)) {
				return numberOfArticlesDao.byDay(reporterId);
			}
		} else if ("stat_points".equals(apiName)) {
			return dummy.getJsonWithStatPoints(reporterId);
		}
		
		return null;
	}
}
