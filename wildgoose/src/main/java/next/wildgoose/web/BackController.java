package next.wildgoose.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface BackController {
	public Map<String, Object> execute(HttpServletRequest request);
}
