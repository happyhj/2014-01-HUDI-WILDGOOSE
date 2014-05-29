package next.wildgoose.framework;

import javax.servlet.http.HttpServletRequest;

public interface BackController {
	public Result execute(HttpServletRequest request);
}
