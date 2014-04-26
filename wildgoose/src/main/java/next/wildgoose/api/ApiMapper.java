package next.wildgoose.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.utility.Wildgoose;
import next.wildgoose.web.RestfulURI;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiMapper extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiMapper.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject result = null;
		// ArticleCount date = new ArticleCount();
		
		response.setContentType("text/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String requestURI = request.getRequestURI();
		LOGGER.debug(requestURI);
		
		// ""/"api"/"v1"/"reporters"/<rep_ID>/"number_of_articles?by=something"
		RestfulURI restful = new RestfulURI (requestURI);
		ReporterData reporter = new ReporterData();
		
		try {
			if (restful.check(2, Wildgoose.API_REPORTERS)) {
				
				int reporterId = Integer.parseInt(restful.get(3));
				String apiName = restful.get(4);
				String by = request.getParameter("by");
				
				result = reporter.getJSON(reporterId, apiName, by);
			}
			
			LOGGER.debug(result.toString());
			out.println(result.toString());
		}
		catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			out.println("error");
		}
	}
}
