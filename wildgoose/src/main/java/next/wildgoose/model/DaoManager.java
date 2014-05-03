package next.wildgoose.model;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.utility.UriHandler;


public abstract class DaoManager {
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected UriHandler uriHandler;
	protected ServletContext context;
	
	protected DaoManager (HttpServletRequest request) {
		this (request, null, null);
	}
	
	protected DaoManager (HttpServletRequest request, UriHandler uriHandler) {
		this (request, null, uriHandler);
	}
	
	
	protected DaoManager (HttpServletRequest request, HttpServletResponse response, UriHandler uriHandler) {
		this.request = request;
		this.response = response;
		this.uriHandler = uriHandler;
		
		this.context = request.getServletContext();
	}

}