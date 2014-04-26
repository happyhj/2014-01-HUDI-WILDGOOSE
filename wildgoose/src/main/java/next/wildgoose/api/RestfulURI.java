package next.wildgoose.api;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestfulURI {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestfulURI.class.getName());
	
	private List<String> resources;
	

	public RestfulURI (String requsetURI, String version) {
		this.resources = separateResource(requsetURI, version);
	}
	
	private List<String> separateResource (String rawRequestURI, String version) {
		// ""/"api"/"v1" 이하 부분 삭제
		int index = rawRequestURI.indexOf(version);
		String requestURI = rawRequestURI.substring(index + version.length());
		
		if (requestURI.indexOf("/") == 0) {
			requestURI = requestURI.substring(1);
		}

		LOGGER.debug(requestURI);

		return Arrays.asList(requestURI.split("/"));
	}
	

	public String get (int position) {
		return this.resources.get(position);
	}
	
	public boolean check (int position, Object obj) {
		
		if (position < 0 || position > this.resources.size() ) {
			return false;
		}

		return this.resources.get(position).equals((String)obj);
		
	}
}
