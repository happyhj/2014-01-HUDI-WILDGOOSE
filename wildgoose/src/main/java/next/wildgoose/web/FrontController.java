package next.wildgoose.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.model.Action;
import next.wildgoose.model.ActionForward;
import next.wildgoose.utility.UriHandler;
import next.wildgoose.utility.Wildgoose;

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

		Action action = null;
		ActionForward forward = null;
		
		try {
			if (uriHandler.check(0, Wildgoose.RESOURCE_INDEX)) {
				action = new SearchReporter(request, response, uriHandler);
				forward = action.execute();
			}
			else if (uriHandler.check(0, Wildgoose.RESOURCE_REPORTERS)) {
				action = new ShowReporter(request, response, uriHandler);
				forward = action.execute();
			}
			else if (uriHandler.check(0, Wildgoose.RESOURCE_ERROR)) {
				action = new Error(request, Wildgoose.PAGE_ERROR_SEARCH_REPORTER, Wildgoose.MSG_ERROR);
				forward = action.execute();
			}
			else {
				action = new Error(request, Wildgoose.PAGE_ERROR_SEARCH_REPORTER, Wildgoose.MSG_WENT_WRONG);
				forward = action.execute();
			}
		}
		catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			
			forward.setRedirect(true);
			forward.setPath(Wildgoose.RESOURCE_ERROR);
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
