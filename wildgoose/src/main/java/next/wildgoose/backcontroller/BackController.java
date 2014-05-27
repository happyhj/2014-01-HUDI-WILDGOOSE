package next.wildgoose.backcontroller;

import javax.servlet.http.HttpServletRequest;

public interface BackController {
	public Object execute(HttpServletRequest request);
}
