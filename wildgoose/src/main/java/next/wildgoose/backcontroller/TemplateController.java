package next.wildgoose.backcontroller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
		TemplateResult result = new TemplateResult();
		String htmlDocument = null;
	
		if ("login.html".equals(templateFileName)) {
			HttpSession session = request.getSession();
			Random random = new Random();
			String rand = Double.toString(random.nextDouble());
			rand = rand.replace("0.", "");
			session.setAttribute("randNum", rand);
			result.setRand(rand);
		}
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
			result.setMessage("request file is not exist");
		} catch (IOException e) {
			result.setMessage("can't read file");
		}
		
		return result;
	}
}
