package next.wildgoose.backcontroller;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dto.result.SimpleResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.utility.Constants;

public class ErrorController implements BackController {

	@Override
	public Result execute(HttpServletRequest request) {
		Result result = new SimpleResult();
		result.setMessage(Constants.MSG_WENT_WRONG);
		result.setStatus(404);
		return result;
	}

}