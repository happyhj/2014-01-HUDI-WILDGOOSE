package next.wildgoose.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.DummyData;
import next.wildgoose.dao.HookingKeywordDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;

public class GraphDataService implements Daction {
	public DactionResult execute(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		NumberOfArticlesDAO noaDao = null;
		HookingKeywordDAO hkDao = null;
		DummyData dummy = null;
		
		
		DactionResult result = null;
		JSONObject json = null;
		int reporterId = Integer.parseInt(uri.get(3));
		String apiName = uri.get(4);
		
		if (apiName == null) {
			return null;
		}
		if ("number_of_articles".equals(apiName)) {
			noaDao = (NumberOfArticlesDAO) context.getAttribute("NumberOfArticlesDAO");
			String condition = request.getParameter("by");
			if ("section".equals(condition)) {
				json = noaDao.bySection(reporterId);
			} else if ("day".equals(condition)) {
				json = noaDao.byDay(reporterId);
			}
		} else if ("number_of_hook_keywords".equals(apiName)) {
			hkDao = (HookingKeywordDAO) context.getAttribute("HookingKeywordDAO");
			json = hkDao.getHookingKeywordsCount(reporterId);
		} else if ("stat_points".equals(apiName)) {
			dummy = (DummyData) context.getAttribute("DummyData");
			json = dummy.getJsonWithStatPoints(reporterId);
		}
		
		result = new DactionResult("json", json);
		return result;
	}
}
