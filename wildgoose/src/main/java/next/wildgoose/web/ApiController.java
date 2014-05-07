package next.wildgoose.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.service.Daction;
import next.wildgoose.service.DactionResult;
import next.wildgoose.service.ErrorDaction;
import next.wildgoose.service.GetGraphData;
import next.wildgoose.service.GetJsonData;
import next.wildgoose.service.HtmlReader;
import next.wildgoose.service.SignAccount;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Daction daction = getProperDaction(request);
		DactionResult result = daction.execute(request);
		send(response, result);
	}
	
	private void send(HttpServletResponse response, DactionResult result) {
		PrintWriter out = null;
		
		if ("json".equals(result.getDataType())) {
			response.setContentType(Constants.HEADER_CON_TYPE_JSON);
		} else if ("html".equals(result.getDataType())) {
			response.setContentType(Constants.HEADER_CON_TYPE_HTML);
		} else {
			response.setContentType(Constants.HEADER_CON_TYPE_PLAIN_TEXT);
		}

		try {
			out = response.getWriter();
			out.println(result.getData());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		out.close();
	}
	
	private Daction getProperDaction(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		LOGGER.debug(uri.get(2));
		
		Daction defaultDaction = (ErrorDaction) context.getAttribute("ErrorDaction");
		Map<String, Daction> dactionMap = new HashMap<String, Daction>();
		dactionMap.put(Constants.RESOURCE_REPORTERS, (GetGraphData) context.getAttribute("GraphDataService"));
		dactionMap.put(Constants.RESOURCE_SEARCH, (GetJsonData) context.getAttribute("JsonDataService"));
		dactionMap.put(Constants.RESOURCE_MORE_RPT_CARD, (GetJsonData) context.getAttribute("JsonDataService"));
		dactionMap.put(Constants.RESOURCE_HTML, (HtmlReader) context.getAttribute("HtmlReader"));
		dactionMap.put(Constants.RESOURCE_SIGN, (SignAccount) context.getAttribute("AccountService"));
		Daction result = dactionMap.getOrDefault(uri.get(2), defaultDaction);
		return result;
	}
}


