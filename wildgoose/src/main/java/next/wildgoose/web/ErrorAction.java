package next.wildgoose.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.utility.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorAction implements Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAction.class.getName());

	private String path;
	private String message;
	
	public ErrorAction(String message) {
		this(Wildgoose.PAGE_ERROR, message);
	}

	public ErrorAction(String forwardingPath, String message) {
		this.path = forwardingPath;
		this.message = message;
	}

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response, UriHandler restful) throws Exception {
		
		ActionForward forward = new ActionForward();
		forward.setPath(this.path);
		
		request.setAttribute(Wildgoose.KEYWORD_MSG, this.message);
		
		return forward;
	}

}
