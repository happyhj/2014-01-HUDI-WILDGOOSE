package next.wildgoose.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.dao.FavoriteDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncodingFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(EncodingFilter.class.getName());
	
	private String encodingType = null;
	private FilterConfig filterConfig;
	
	
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encodingType = filterConfig.getServletContext().getInitParameter("encoding");
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		// post
		if (request.getCharacterEncoding() == null) {
	        request.setCharacterEncoding(encodingType);
	    }
		// get
		EncodingRequestWrapper wrappedRequest = new EncodingRequestWrapper(request, encodingType);
		
		chain.doFilter(wrappedRequest, response);
	}
	
	
	public void destroy() {
		this.encodingType = null;
	}
}
