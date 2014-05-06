package next.wildgoose.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.accessdao.GetGraphData;
import next.wildgoose.accessdao.GetTextData;
import next.wildgoose.accessdao.SearchReporter;
import next.wildgoose.accessdao.SignAccount;
import next.wildgoose.accessdao.UriHandler;
import next.wildgoose.dao.JsonDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = null;
		
		// servlet context
		ServletContext context = request.getSession().getServletContext();
		response.setCharacterEncoding(context.getInitParameter("encoding"));
		
		PrintWriter out = response.getWriter();
		String requestURI = request.getRequestURI();
		UriHandler uri = new UriHandler (requestURI);
		
		if (uri.check(2, Wildgoose.RESOURCE_REPORTERS)) {
			// reporters 자원으로 요청시
			response.setContentType(Wildgoose.HEADER_CON_TYPE_JSON);
			result = jsonRequest(request, uri);
		} else if (uri.check(2, "most_similar_names")) {
			// ex) /api/v1/most_similar_names?name=김
			String name = request.getParameter("name");
			GetTextData getText = new GetTextData(request);
			result = getText.getJsonString(name);
		} else if (uri.check(2,  Wildgoose.RESOURCE_HTML)) {
			// HTML 자원을 요청시
			response.setContentType(Wildgoose.HEADER_CON_TYPE_HTML);
			String path = context.getRealPath(Wildgoose.RESOURCE_ROOT);
			result = htmlRequest(request, uri, path);
		} else if (uri.check(2, Wildgoose.RESOURCE_SIGN)) {
			// Sign 자원 요청시
			SignAccount signAccount = new SignAccount(request);
			result = signRequest(request, uri, signAccount);
		}
		
		out.println(result);
	}
	
	private String jsonRequest(HttpServletRequest request, UriHandler uri) {
		GetGraphData reporter = new GetGraphData();
		String result = null;
		// ~/reporters 요청시 ajax로 reporterCards 반환
		if (uri.get(3) == null) {
			SearchReporter sr = new SearchReporter(request);
			result = sr.toJsonString();
		} else {
			int reporterId = Integer.parseInt(uri.get(3));
			LOGGER.debug("reporter id request: " + reporterId);
			
			String apiName = uri.get(4);
			String by = request.getParameter("by");
			result = reporter.getJSON(request, reporterId, apiName, by).toString();
		}
		return result;
	}

	private String htmlRequest(HttpServletRequest request, UriHandler uri, String path) {
		String result = null;
		String resourceName = uri.get(3);
		GetTextData getText = new GetTextData(request);
		result = getText.getPartialHtml(resourceName);
		return result;
	}
	
	private String signRequest(HttpServletRequest request, UriHandler uri, SignAccount signAccount) {
		String subResource = uri.get(3);
		String result = null;
		// sign/up
		if ("up".equals(subResource)) {
			result = "Validation Failure";
			if (signAccount.up()) {
				result = "Validation Success";
			}
		} else if ("email".equals(subResource)) {
		// sign/email,  email 자원 요청시
			String email = uri.get(4);
			result = "OK";
			if (signAccount.hasEmail(email)) {
				result = "";
			}
		}	
		return result;
	}
}
