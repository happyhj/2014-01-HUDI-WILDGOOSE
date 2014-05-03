package next.wildgoose.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.model.PartialHtml;
import next.wildgoose.utility.Wildgoose;
import next.wildgoose.web.UriHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiMapper extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiMapper.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = null;
		
		// servlet context
		ServletContext context = request.getSession().getServletContext();
		response.setCharacterEncoding(context.getInitParameter("encoding"));
		
		PrintWriter out = response.getWriter();
		String requestURI = request.getRequestURI();
		UriHandler uriHandler = new UriHandler (requestURI);
		ReporterData reporter = new ReporterData();
		
		try {
			if (uriHandler.check(2, Wildgoose.RESOURCE_REPORTERS)) {
				response.setContentType(Wildgoose.HEADER_CON_TYPE_JSON);
				
				LOGGER.debug("reporter id request: " + uriHandler.get(3));
				int reporterId = Integer.parseInt(uriHandler.get(3));
				String apiName = uriHandler.get(4);
				
				String by = request.getParameter("by");
				result = reporter.getJSON(request, reporterId, apiName, by).toString();
			}
			else if (uriHandler.check(2,  Wildgoose.RESOURCE_HTML)) {
				response.setContentType(Wildgoose.HEADER_CON_TYPE_HTML);
				
				String path = context.getRealPath(Wildgoose.RESOURCE_ROOT);
				PartialHtml phtml = new PartialHtml(path + Wildgoose.PAGE_STATIC_ACCOUNT);
				result = phtml.read();
			}
			
			// response to client
			if (result != null) {
				LOGGER.debug(result.toString());
				out.println(result.toString());
				
				return;
			}
		}
		catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			out.println("error");
		}
	}
}
