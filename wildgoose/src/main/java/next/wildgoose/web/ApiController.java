package next.wildgoose.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.dao.DataSource;
import next.wildgoose.dao.SignDAO;
import next.wildgoose.dao.SqlUtil;
import next.wildgoose.model.JsonConverter;
import next.wildgoose.model.PartialHtml;
import next.wildgoose.utility.Validation;
import next.wildgoose.utility.Wildgoose;

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
			result = getSimilarNames(name);
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
			JsonConverter jsonConverter = new SearchReporter(request);
			result = jsonConverter.toJsonString();
		} else {
			int reporterId = Integer.parseInt(uri.get(3));
			LOGGER.debug("reporter id request: " + reporterId);
			
			String apiName = uri.get(4);
			String by = request.getParameter("by");
			result = reporter.getJSON(request, reporterId, apiName, by).toString();
		}
		return result;
	}
	
	private String getSimilarNames(String name) {
		JSONObject result = new JSONObject();
		String query = "SELECT name FROM author WHERE name LIKE ? ORDER BY name LIMIT 0, 5 ";
		PreparedStatement psmt = null;
		Connection conn = null;
		ResultSet rs = null;
		conn = DataSource.getInstance().getConnection();
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, name + "%");
			LOGGER.debug(psmt.toString());
			rs = psmt.executeQuery();
			while (rs.next()) {
				JSONObject data = new JSONObject().put("name", rs.getString("name"));
				result.append("data", data);
			}
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		return result.toString();
	}

	private String htmlRequest(HttpServletRequest request, UriHandler uri, String path) {
		String result = null;
		PartialHtml phtml = null;
		String resourceName = uri.get(3);
		LOGGER.debug("resourceName: " + resourceName);
		
		if ("create_account".equals(resourceName)) {
			phtml = new PartialHtml(path + Wildgoose.PAGE_STATIC_ACCOUNT);
			result = phtml.read();
		} else if ("create_reporter_card".equals(resourceName)) {
			phtml = new PartialHtml(path + Wildgoose.PAGE_STATIC_REPORTER_CARD);
			result = phtml.read();	
		}
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
