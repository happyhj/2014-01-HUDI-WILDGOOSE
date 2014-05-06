package next.wildgoose.refactoring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		if (firstUri == null) {
			firstUri = Constants.RESOURCE_ERROR;
		}
		
		if (firstUri.equals(Constants.RESOURCE_INDEX)) {
			// "/"
		} else if (firstUri.equals(Constants.RESOURCE_REPORTERS)) {
			// "/reporters"
		} else if (firstUri.equals(Constants.RESOURCE_ERROR)) {
			// "/error"
		} else {
			// NON-Existing URI request
		}
	}
}
