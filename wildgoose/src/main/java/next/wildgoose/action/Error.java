package next.wildgoose.action;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.utility.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Error implements Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(Error.class.getName());
	
	private HttpServletRequest request;
	private String path;
	private String message;
	
	public Error(HttpServletRequest request, String message) {
		this(request, Wildgoose.PAGE_ERROR, message);
	}

	public Error(HttpServletRequest request, String forwardingPath, String message) {
		this.request = request;
		this.path = forwardingPath;
		this.message = message;
	}

	@Override
	public ActionForward execute() {
		
		ActionForward forward = new ActionForward();
		forward.setPath(this.path);
		
		this.request.setAttribute(Wildgoose.KEYWORD_MSG, this.message);
		
		return forward;
	}

}
