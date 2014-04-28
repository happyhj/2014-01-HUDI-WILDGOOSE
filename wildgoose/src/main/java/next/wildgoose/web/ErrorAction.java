package next.wildgoose.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.utility.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorAction implements Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAction.class.getName());
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response, RestfulURI restful) throws Exception {
		
		ActionForward forward = new ActionForward();
		forward.setPath(Wildgoose.PAGE_ERROR);
		
		return forward;
	}

}
