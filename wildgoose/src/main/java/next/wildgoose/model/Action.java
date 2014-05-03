package next.wildgoose.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.utility.UriHandler;

public interface Action {
	public ActionForward execute () throws Exception;
//	public ActionForward execute (HttpServletRequest request, HttpServletResponse response, UriHandler uriHandler) throws Exception;

}
