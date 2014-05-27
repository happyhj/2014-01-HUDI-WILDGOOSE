package next.wildgoose.backcontroller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateController implements BackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class.getName());
	
	@Override
	public Object execute(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		
		String templateFileName = uri.get(1);
		String root = context.getRealPath(Constants.RESOURCE_ROOT);
		String path = root + templateFileName;
		
		String templateStr = read(path);
		// TODO: Make Template response DTO and put this str into it
		
		return null;
	}
	
	public String read(String path) {
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
		} catch (FileNotFoundException e) {
			LOGGER.debug(e.getMessage(), e);
			htmlDocument = "request file is not exist";
		} catch (IOException e) {
			LOGGER.debug(e.getMessage(), e);
			htmlDocument = "can't read file";
		}
		
		return htmlDocument;
	}
}
