package next.wildgoose.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiMapper extends HttpServlet {
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			String reporterId = requestApi.substring(0, requestApi.indexOf('/'));
			String apiName = requestApi.substring(requestApi.indexOf('/')+1);
			if ("number_of_hook_keywords".equals(apiName)) {
				JSONObject result = HookKeyword.getJson(reporterId);
				out.println(result.toString());
				logger.debug(result.toString());
			}
		}
	}
}
