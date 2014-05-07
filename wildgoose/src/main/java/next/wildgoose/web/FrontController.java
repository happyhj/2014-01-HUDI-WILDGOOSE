package next.wildgoose.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.service.Action;
import next.wildgoose.service.ActionResult;
import next.wildgoose.service.Error;
import next.wildgoose.service.ArticleCardService;
import next.wildgoose.service.ReporterCardService;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class.getName());
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Action action = getProperAction(request);
		ActionResult result = action.execute(request);
		
		if (result.isRedirect()) {
			response.sendRedirect(result.getPath());
			return;
		}
		
		RequestDispatcher reqDispatcher = request.getRequestDispatcher(result.getPath());
		reqDispatcher.forward(request, response);
	}
	
	private Action getProperAction(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		LOGGER.debug(uri.get(0));

		Action defaultAction = (Error) context.getAttribute("Error");
		Map<String, Action> actionMap = new HashMap<String, Action>();
		actionMap.put(Constants.RESOURCE_INDEX, (ReporterCardService) context.getAttribute("ReporterCardService"));
		actionMap.put(Constants.RESOURCE_REPORTERS, (ArticleCardService) context.getAttribute("ArticleCardService"));
		Action result = actionMap.getOrDefault(uri.get(0), defaultAction);
		return result;
	}
}
