package next.wildgoose.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.dao.DummyData;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiMapper extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject result = null;
		DummyData dummy = new DummyData();
		// ArticleCount date = new ArticleCount();
		
		response.setContentType("text/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String requestURI = request.getRequestURI();
		

		String[] splitUri = requestURI.split("/");
		// ""/"api"/"v1"/"reporters"/<rep_ID>/"number_of_articles?by=something"
		
		if (splitUri.length < 4) {
			logger.error("WRONG REQUEST");
			return;
		}
		String apiCategory = splitUri[3];
		
		if ("reporters".equals(apiCategory)) {
			int reporterId = Integer.parseInt(splitUri[4]);
			String apiName = splitUri[5];
			
			if ("number_of_hook_keywords".equals(apiName)) {
				result = dummy.getJsonWithNumberOfHookKeywords(reporterId);
			}
			if ("number_of_articles".equals(apiName)) {
				String by = request.getParameter("by");

				// when by is null
				if (by == null) {
					// by = "I am not null any more";
					// default값 수정 필요
					by = "section";
				}
				
				if ("section".equals(by)) {
					result = dummy.getJsonWithNumberOfArticlesBy(reporterId, by);
				} else if ("day".equals(by)) {
					result = dummy.getJsonWithNumberOfArticleByDay(reporterId);
				}
			}
		}
		out.println(result.toString());
		logger.debug(result.toString());
	}
}
