package next.wildgoose.framework.support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class.getName());
	
	public static StringBuilder load(String path) {
//		String message = null;
		BufferedReader in = null;
		StringBuilder sb = null;
		try {
			in = new BufferedReader(new FileReader(path));
			sb = new StringBuilder();
			String temp;
			while ((temp = in.readLine()) != null) {
		        sb.append(temp);
		    }
			
		} catch (FileNotFoundException fnfe) {
			LOGGER.debug(fnfe.getMessage(), fnfe);
//			message = "request file is not exist";
		} catch (IOException ioe) {
			LOGGER.debug(ioe.getMessage(), ioe);
//			message = "can't read file";
		} finally {
			try {
				in.close();
			} catch (IOException ioe) {
				 LOGGER.debug(ioe.getMessage(), ioe);
//				message = "can't read file";
			} catch (NullPointerException ioe) {
				 LOGGER.debug(ioe.getMessage(), ioe);
//					message = "can't read file";
				}
		}
		return sb;
		
	}

}
