package next.wildgoose.api;

import java.sql.SQLException;

import next.wildgoose.dao.ArticleCountDAO;

import org.json.JSONObject;

public class ReporterData {
	
	DummyData dummy = new DummyData();
	
	public JSONObject getJSON(int reporterId, String apiName, String condition) {
		ArticleCountDAO articleCount = new ArticleCountDAO();
		JSONObject result = null;
		
		if ("number_of_hook_keywords".equals(apiName)) {
			result = dummy.getJsonWithNumberOfHookKeywords(reporterId);
		}
		if ("number_of_articles".equals(apiName)) {
			// when by is null, day is default value
			if (condition == null) {
				condition = "day";
			}
			
			if ("section".equals(condition)) {
				result = dummy.getJsonWithNumberOfArticlesBy(reporterId, condition);
			} else if ("day".equals(condition)) {
//				result = dummy.getJsonWithNumberOfArticleByDay(reporterId);
				try {
					result = articleCount.countArticle(reporterId);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
}
