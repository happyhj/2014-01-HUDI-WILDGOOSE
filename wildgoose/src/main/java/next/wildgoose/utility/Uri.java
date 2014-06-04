package next.wildgoose.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Uri {
	private static final Logger LOGGER = LoggerFactory.getLogger(Uri.class.getName());
	private List<String> resources;
	private boolean api = false;
	
	public Uri(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String trimmedUri = trimUri(uri);
		this.resources = Arrays.asList(trimmedUri.split("/"));
	}
	
	public Uri(String uri) {
		this.resources = regularizedList(uri.split("/"));
		LOGGER.debug("uri: " + this.toString());
	}
	
	public String getPrimeResource() {
		return get(0);
	}
	
	public boolean isAPI() {
		return this.api;
	}
	
	private List<String> regularizedList(String[] uriArr) {
		
		List<String> splitedList = null;
		if (uriArr[0] == "") {
			splitedList = new ArrayList<String>();
			splitedList.add("");
			return splitedList;
		}
		
		splitedList = Arrays.asList(uriArr);
		for (int i=0; i<splitedList.size(); ++i) {
			String partialRes = splitedList.get(i);
			if (partialRes.charAt(0) != '[' || partialRes.charAt(partialRes.length()-1) != ']') {
				continue;
			}
			splitedList.set(i, "*");
		}
		
		return splitedList;
	}
	private String trimUri (String uri) {
		int startIdx = 0;
		int endIdx = uri.length();
		
		if ("/".equals(uri)) {
			return "";
		}
		if (uri.indexOf('/') == 0) {
			startIdx++;
		}
		if (uri.startsWith("/api/v1/")) {
			setAPI();
			startIdx += 7;
		}
		if (uri.lastIndexOf('/') == endIdx-1) {
			endIdx--;
		}
		return uri.substring(startIdx, endIdx);
	}
	
	private void setAPI() {
		this.api = true;
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
	
	public Iterator<String> iterator() {
		return this.resources.iterator();
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
	
	public boolean check (int position, String uri) {
		String operand = this.get(position);
		if (operand == null) {
			if (uri != null) {
				return false;
			}
			return true;
		}
		return operand.equals(uri);
	}
}
