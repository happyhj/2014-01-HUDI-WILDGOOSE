package next.wildgoose.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.service.Daction;
import next.wildgoose.service.DactionResult;
import next.wildgoose.service.ErrorDaction;
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
		send(request, response, result);
	}
	
	private void send(HttpServletRequest request, HttpServletResponse response, DactionResult result) {
		ServletContext context = request.getServletContext();
		String type = result.getDataType();
		PrintWriter out = null;
		
		response.setCharacterEncoding(context.getInitParameter("encoding"));
		if ("json".equals(type)) {
			response.setContentType(Constants.HEADER_CON_TYPE_JSON);
		} else if ("html".equals(type)) {
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
		String resourceName = uri.get(2);
		
		Daction defaultDaction = (ErrorDaction) context.getAttribute("ErrorDaction");
		Map<String, Daction> dactionMap = WebListener.dactionMap;
		Daction result = dactionMap.get(resourceName);
		if (result == null) {
			result = defaultDaction;
		}
		return result;
	}
}


