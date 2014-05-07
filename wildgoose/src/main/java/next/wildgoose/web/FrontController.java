package next.wildgoose.web;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.service.ActionResult;
import next.wildgoose.service.Error;
import next.wildgoose.service.GetArticleCard;
import next.wildgoose.service.GetReporterCard;
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
		Uri uri = new Uri(request.getRequestURI());
		String firstUri = uri.get(0);
		ActionResult result = null;
		RequestDispatcher reqDispatcher = null;
		
		if (firstUri == null) {
			uri = new Uri(Constants.RESOURCE_ERROR);
		}
		
		if (firstUri.equals(Constants.RESOURCE_INDEX)) {
			GetReporterCard searchReporter = GetReporterCard.getInstance();
			result = searchReporter.execute(uri);
		} else if (firstUri.equals(Constants.RESOURCE_REPORTERS)) {
			GetArticleCard showReporter = GetArticleCard.getInstance();
			result = showReporter.execute(uri);
		} else {
			Error error = Error.getInstance();
			result = error.execute(uri);
		}
		
		if (result.isRedirect()) {
			response.sendRedirect(result.getPath());
			return;
		}
		
		setAttributeOnRequest(request, result);
		reqDispatcher = request.getRequestDispatcher(result.getPath());
		reqDispatcher.forward(request, response);
		
	}
	
	private void setAttributeOnRequest(HttpServletRequest request, ActionResult result) {
		HashMap<String, Object> map = result.getAttribute();
		for (String key : map.keySet()) {
			request.setAttribute(key, map.get(key));
		}
	}
}
