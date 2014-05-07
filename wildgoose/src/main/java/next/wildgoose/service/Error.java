package next.wildgoose.service;

import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Error implements Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(Error.class.getName());
	private static Error error;
	private ActionResult ar;
	
	private Error() {
		
	}
	
	public static Error getInstance() {
		if (error == null) {
			error = new Error();
		}
		return error;
	}

	public ActionResult execute(Uri uri) {
		this.ar.setForwardingOption(false, Constants.PAGE_ERROR_SEARCH_REPORTER);
		
		if (Constants.RESOURCE_ERROR.equals(uri.get(0))) {
			this.ar.setAttribute(Constants.KEYWORD_MSG, Constants.MSG_ERROR);
		} else {
			this.ar.setAttribute(Constants.KEYWORD_MSG, Constants.MSG_WENT_WRONG);
		}
		return this.ar;
	}

}
