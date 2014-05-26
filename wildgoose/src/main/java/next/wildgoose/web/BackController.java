package next.wildgoose.web;

import javax.servlet.http.HttpServletRequest;

public interface BackController {
	public Object execute(HttpServletRequest request);
}
