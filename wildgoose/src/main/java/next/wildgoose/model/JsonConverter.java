package next.wildgoose.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.utility.UriHandler;

public interface JsonConverter {
	public String toJsonString () throws Exception;
//	public String toJsonString (HttpServletRequest request, HttpServletResponse response, UriHandler uriHandler) throws Exception;
}
