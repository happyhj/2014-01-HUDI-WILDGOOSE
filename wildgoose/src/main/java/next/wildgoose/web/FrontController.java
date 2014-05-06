package next.wildgoose.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.accessdao.ActionForward;
import next.wildgoose.accessdao.Error;
import next.wildgoose.accessdao.SearchReporter;
import next.wildgoose.accessdao.ShowReporter;
import next.wildgoose.accessdao.UriHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class.getName());
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher reqDispatcher = null;
		String requestURI = request.getRequestURI();
					
		UriHandler uriHandler = new UriHandler (requestURI);
		LOGGER.debug(uriHandler.toString());

		ActionForward forward = null;
		
		if (uriHandler.check(0, Wildgoose.RESOURCE_INDEX)) {
			forward = new SearchReporter(request).execute();
		} else if (uriHandler.check(0, Wildgoose.RESOURCE_REPORTERS)) {
			forward = new ShowReporter(request, uriHandler).execute();
		} else if (uriHandler.check(0, Wildgoose.RESOURCE_ERROR)) {
			forward = new Error(request, Wildgoose.PAGE_ERROR_SEARCH_REPORTER, Wildgoose.MSG_ERROR).execute();
		} else {
			forward = new Error(request, Wildgoose.PAGE_ERROR_SEARCH_REPORTER, Wildgoose.MSG_WENT_WRONG).execute();
		}
		
		LOGGER.debug(forward.toString());
		
		// redirect
		if (forward.isRedirect()) {
			response.sendRedirect(forward.getPath());
			return;
		}
		
		// forward
		reqDispatcher = request.getRequestDispatcher(forward.getPath());
		reqDispatcher.forward(request, response);
	}
}
