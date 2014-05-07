package next.wildgoose.service;

import next.wildgoose.dao.DummyData;
import next.wildgoose.dao.HookingKeywordDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;

public class GetGraphData {
	private static GetGraphData graphData;
	public static GetGraphData getInstance() {
		if (graphData == null) {
			graphData = new GetGraphData();
		}
		return graphData;
	}
	
	public JSONObject getData(Uri uri) {
		NumberOfArticlesDAO noaDao = null;
		HookingKeywordDAO hkDao = null;
		DummyData dummy = null;
		
		JSONObject result = null;
		int reporterId = Integer.parseInt(uri.get(3));
		String apiName = uri.get(4);
		
		if (apiName == null) {
			return null;
		}
		if ("number_of_articles".equals(apiName)) {
			noaDao = NumberOfArticlesDAO.getInstance();
			String condition = uri.getParameter("by");
			if ("section".equals(condition)) {
				result = noaDao.bySection(reporterId);
			} else if ("day".equals(condition)) {
				result = noaDao.byDay(reporterId);
			}
		} else if ("number_of_hook_keywords".equals(apiName)) {
			hkDao = HookingKeywordDAO.getInstance();
			result = hkDao.getHookingKeywordsCount(reporterId);
		} else if ("stat_points".equals(apiName)) {
			dummy = DummyData.getInstance();
			result = dummy.getJsonWithStatPoints(reporterId);
		}
		
		return result;
	}
}
