package next.wildgoose.service;

import javax.servlet.http.HttpServletRequest;

public interface Action {
	public ActionResult execute(HttpServletRequest request);
}
