package next.wildgoose.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlDocService implements Daction {
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlDocService.class.getName());
	
	public DactionResult execute(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		
		String resourceName = uri.get(3);
		String root = context.getRealPath(Constants.RESOURCE_ROOT);
		
		String path = root + requestResourceMapper(resourceName);
		String htmlDoc = read(path);
		
		// IF login page, put random number
		if ("authenticate_user".equals(resourceName)) {
			HttpSession session = request.getSession();
			Random random = new Random();
			String rand = Double.toString(random.nextDouble());
			rand = rand.replace("0.", "");
			session.setAttribute("randNum", rand);

			LOGGER.debug(""+htmlDoc.indexOf("{$}"));
			htmlDoc = htmlDoc.replace("{$}", rand);
			LOGGER.debug(htmlDoc);
		}
		
		DactionResult result = new DactionResult("html", htmlDoc);
		return result;
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
	
	private String requestResourceMapper(String resourceName) {
		
		if ("create_reporter_card".equals(resourceName)) {
			return Constants.ABSOLUTE_RESOURECE_TEMPLATE + Constants.PAGE_STATIC_REPORTER_CARD;
		} else if ("create_account".equals(resourceName)) {
			return Constants.ABSOLUTE_RESOURECE_TEMPLATE + Constants.PAGE_STATIC_ACCOUNT;
		} else if ("authenticate_user".equals(resourceName)) {
			return Constants.ABSOLUTE_RESOURECE_TEMPLATE + Constants.PAGE_STATIC_LOGIN;
		}
		return null;
	}
}
