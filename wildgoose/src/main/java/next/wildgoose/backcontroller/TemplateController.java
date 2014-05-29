package next.wildgoose.backcontroller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dto.TemplateResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

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
		String root = context.getRealPath(Constants.RESOURCE_ROOT);
		String path = root +"html_templates/"+ templateFileName;
		TemplateResult result = new TemplateResult(request.getParameterMap());
		String htmlDocument = null;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String temp;
			while ((temp = in.readLine()) != null) {
		        sb.append(temp);
		    }
			in.close();
			htmlDocument = sb.toString();
			result.setMessage("Loading template string success");
			result.setStatus(200);
			result.setTemplate(htmlDocument);
		} catch (FileNotFoundException e) {
			LOGGER.debug(e.getMessage(), e);
			result.setMessage("request file is not exist");
		} catch (IOException e) {
			LOGGER.debug(e.getMessage(), e);
			result.setMessage("can't read file");
		}
		LOGGER.debug(result.getMessage());
		LOGGER.debug(""+result.getStatus());
		LOGGER.debug(result.getTemplate());
		return result;
	}
}
