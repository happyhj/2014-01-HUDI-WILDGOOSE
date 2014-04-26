package next.wildgoose.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
					
		RestfulURI restful = new RestfulURI (requestURI);
		LOGGER.debug(restful.toString());

		Action action = null;
		ActionForward forward = null;
		
		try {
			if (restful.check(0, "")) {
				action = new SearchReporterAction();
				forward = action.execute(request, response, restful);
			}
			else if (restful.check(0, "reporters")) {
				action = new ShowReporterAction();
				forward = action.execute(request, response, restful);
			}
		}
		catch (Exception e) {
			forward.setPath(Wildgoose.PAGE_ERROR);
		}
		finally {
			LOGGER.debug(forward.getPath());
			
			reqDispatcher = request.getRequestDispatcher("/" + forward.getPath());
			reqDispatcher.forward(request, response);
		}
		
		
	}

}
