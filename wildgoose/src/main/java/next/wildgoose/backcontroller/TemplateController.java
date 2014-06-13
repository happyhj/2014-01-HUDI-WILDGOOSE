package next.wildgoose.backcontroller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dto.TemplateResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.framework.security.RandomNumber;
import next.wildgoose.framework.support.ResourceLoader;
import next.wildgoose.framework.utility.Uri;
import next.wildgoose.utility.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateController implements BackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class.getName());
	
	@Override
	public Result execute(HttpServletRequest request) {
		Result result = readTemplate(request);
		return result;
	}
	
	private TemplateResult readTemplate(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		String templateFileName = uri.get(1);
		LOGGER.debug("templateFileName: " + templateFileName);
		String root = context.getRealPath(Constants.RESOURCE_ROOT);
		String path = root +"html_templates/"+ templateFileName;
		TemplateResult result = new TemplateResult();
	
		if ("account.html".equals(templateFileName) || "login.html".equals(templateFileName) || "withdraw.html".equals(templateFileName) || "changePassword.html".equals(templateFileName)  || "setting.html".equals(templateFileName) || "reporterCard.html".equals(templateFileName)) {
			LOGGER.debug(templateFileName + " template");
			
			StringBuilder htmlDocumentSB = ResourceLoader.load(path);
			LOGGER.debug(htmlDocumentSB.toString());
			result.setTemplate(htmlDocumentSB.toString());
			result.setMessage("Loading template string success");
			result.setStatus(200);
		}
		
		return result;
	}
}
