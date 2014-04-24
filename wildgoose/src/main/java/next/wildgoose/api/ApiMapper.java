package next.wildgoose.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.dao.ArticleCount;
import next.wildgoose.dao.DummyData;
import next.wildgoose.model.ArticleCard;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiMapper extends HttpServlet {
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject result = null;
		DummyData dummy = new DummyData();
		ArticleCount date = new ArticleCount();
		
		response.setContentType("text/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String requestURI = request.getRequestURI();
		
		// already mapped as "api/v1/"
		String requestApi = requestURI.substring(8);
		logger.debug(requestApi);

		String apiCategory = requestApi.substring(0, requestApi.indexOf('/'));
		logger.debug(apiCategory);
		
		if ("reporters".equals(apiCategory)) {
			requestApi = requestApi.substring(10);
			int reporterId = Integer.parseInt(requestApi.substring(0, requestApi.indexOf('/')));
			String apiName = requestApi.substring(requestApi.indexOf('/')+1);
			
			if ("number_of_hook_keywords".equals(apiName)) {
				result = dummy.getJsonWithNumberOfHookKeywords(reporterId);
				
				out.println(result.toString());
				logger.debug(result.toString());
			}
			
			else if ("number_of_articles".equals(apiName)) {
				String by = request.getParameter("by");
				String condition = null;
				
				// when by is null
				if (by == null) {
					// by = "I am not null any more";
					// default값 수정 필요
					by = "section";
				}
				
				condition = new String (by.getBytes("8859_1"), "UTF-8");
				if ("section".equals(condition)) {
					result = dummy.getJsonWithNumberOfArticlesBy(reporterId, condition);
				}
				else if ("day".equals(condition)) {
					result = dummy.getJsonWithNumberOfArticleByDay(reporterId);
				}
				
				out.println(result.toString());
				logger.debug(result.toString());
			}
		}
	}
}
