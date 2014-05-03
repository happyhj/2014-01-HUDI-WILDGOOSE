package next.wildgoose.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	
	public ActionForward execute (HttpServletRequest request, HttpServletResponse response, UriHandler uriHandler) throws Exception;

}
