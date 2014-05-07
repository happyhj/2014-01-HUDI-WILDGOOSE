package next.wildgoose.utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Uri {
	private static final Logger LOGGER = LoggerFactory.getLogger(Uri.class.getName());
	private List<String> resources;
	
	public Uri(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String trimmedUri = trimUri(uri);
		this.resources = Arrays.asList(trimmedUri.split("/"));
	}
	
	private String trimUri (String uri) {
		int startIdx = 0;
		int endIdx = uri.length();
		
		if (uri.length() == 1) {
			return uri.substring(1);
		}
		if (uri.indexOf('/') == 0) {
			startIdx++;
		}
		if (uri.lastIndexOf('/') == endIdx-1) {
			endIdx--;
		}
		return uri.substring(startIdx, endIdx);
	}
	
	public String get(int position) {
		if (position < 0 || position >= this.resources.size() ) {
			return null;
		}
		return this.resources.get(position);
	}
	
	public int size() {
		return this.resources.size();
	}
	
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
