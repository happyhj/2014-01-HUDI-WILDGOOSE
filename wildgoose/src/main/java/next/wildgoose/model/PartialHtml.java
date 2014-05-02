package next.wildgoose.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartialHtml {
	private static final Logger LOGGER = LoggerFactory.getLogger(PartialHtml.class.getName());
	String path;
	
	public PartialHtml(String path) {
		this.path = path;
	}
	
	public String read() {
		String result = null;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.path));
			StringBuilder sb = new StringBuilder();
			String temp;
			while ((temp = in.readLine()) != null) {
		        sb.append(temp);
		    }
			in.close();
			result = sb.toString();
		} catch (FileNotFoundException e) {
			LOGGER.debug(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.debug(e.getMessage(), e);
		}
		
		return result;
	}
}
