package next.wildgoose.utility;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UriHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UriHandler.class.getName());
	
	private List<String> resources;

	public UriHandler (String requestURI) {
		this.resources = separateResource(requestURI);
	}
	
	private List<String> separateResource (String requestURI) {
		
		requestURI = extractValidRequestURI(requestURI);
		LOGGER.debug(requestURI);
		
		return Arrays.asList(requestURI.split("/"));
	}
	
	private String extractValidRequestURI (String requestURI) {
		int startIdx = 0;
		int endIdx = requestURI.length();
		
		if (requestURI.length() == 1) {
			return requestURI.substring(1);
		}
		
		// uri 시작할 때 "/" 존재시 startIdx 변경
		if (requestURI.indexOf("/") == 0) {
			startIdx++;
		}

		// uri 마지막에 "/" 존재시 endIdx 변경
		if (requestURI.lastIndexOf("/") == endIdx-1) {
			endIdx--;
		}
		
		return requestURI.substring(startIdx, endIdx);
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
	public int size() {
		return this.resources.size();
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> ir = this.resources.iterator();
		
		while (ir.hasNext()) {
			sb.append("/");
			sb.append(ir.next());
		}
		
		return sb.toString();
	}
	
	
}
