package next.wildgoose.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.service.GetGraphData;
import next.wildgoose.service.GetJsonData;
import next.wildgoose.service.SignAccount;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.HtmlReader;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request.getRequestURI());
		String firstUri = uri.get(2);
		JSONObject result = null;
		
		if (firstUri == null) {
			uri = new Uri(Constants.RESOURCE_ERROR);
		}
		
		if (Constants.RESOURCE_REPORTERS.equals(firstUri)) {
			// "api/vi/reporters/22/number_of_articles?by=section"
			GetGraphData graphData = GetGraphData.getInstance();
			result = graphData.getData(uri);
		} else if (Constants.RESOURCE_SEARCH.equals(firstUri)) {
			// "/api/v1/search/most_similar_names?name=김"
			GetJsonData jsonData = GetJsonData.getInstance();
			result = jsonData.getData(uri);
		} else if (Constants.RESOURCE_HTML.equals(firstUri)) {
			// "/api/v1/subhtml/account"
			HtmlReader htmlReader = HtmlReader.getInstance();
			String path = context.getRealPath(Constants.RESOURCE_ROOT);
			result = htmlReader.read(path);
		} else if (Constants.RESOURCE_SIGN.equals(firstUri)) {
			// "api/v1/sign/in"
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			SignAccount signAccount = SignAccount.getInstance();
			result = signAccount.isValid(uri, email, password);
		}
		
		send(response, result);
	}
	
	private void send(HttpServletResponse response, JSONObject result) {
		PrintWriter out = null;
		response.setContentType(Constants.HEADER_CON_TYPE_JSON);
		response.setContentType(Constants.HEADER_CON_TYPE_HTML);
		
		try {
			out = response.getWriter();
			out.println(result.toString());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		out.close();
	}
}
