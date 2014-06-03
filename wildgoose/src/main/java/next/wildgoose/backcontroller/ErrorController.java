package next.wildgoose.backcontroller;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dto.SimpleResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;

public class ErrorController implements BackController {

	@Override
	public Result execute(HttpServletRequest request) {
		Result result = new SimpleResult();
		result.setMessage("Wrong path.");
		result.setStatus(404);
		return result;
	}

}