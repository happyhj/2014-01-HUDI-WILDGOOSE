package next.wildgoose.framework.support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class.getName());
	private ResourceLoader() {
		
	}
	public static StringBuilder load(String path) {
		BufferedReader in = null;
		StringBuilder sb = null;
		try {
			in = new BufferedReader(new FileReader(path));
			sb = new StringBuilder();
			String temp = in.readLine();
			while (temp != null) {
		        sb.append(temp);
		        temp = in.readLine();
		    }
			
		} catch (FileNotFoundException fnfe) {
			LOGGER.debug(fnfe.getMessage(), fnfe);
		} catch (IOException ioe) {
			LOGGER.debug(ioe.getMessage(), ioe);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ioe) {
				 LOGGER.debug(ioe.getMessage(), ioe);
			}
		}
		return sb;
		
	}

}
