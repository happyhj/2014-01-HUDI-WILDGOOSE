package next.wildgoose.backcontroller;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dto.Result;

public interface BackController {
	public Result execute(HttpServletRequest request);
}
