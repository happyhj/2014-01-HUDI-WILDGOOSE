package next.wildgoose.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlReader {
	private static HtmlReader htmlReader;
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlReader.class.getName());
	
	public static HtmlReader getInstance() {
		if (htmlReader == null) {
			htmlReader = new HtmlReader();
		}
		return htmlReader;
	}
	
	public JSONObject read(String path) {
		JSONObject result = new JSONObject();
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
		
		result.put("html", htmlDocument);
		return result;
	}
}
