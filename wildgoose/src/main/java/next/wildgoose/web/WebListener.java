package next.wildgoose.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.service.AccountService;
import next.wildgoose.service.Action;
import next.wildgoose.service.ArticleCardService;
import next.wildgoose.service.Daction;
import next.wildgoose.service.GraphDataService;
import next.wildgoose.service.HtmlDocService;
import next.wildgoose.service.JsonDataService;
import next.wildgoose.service.ReporterCardService;
import next.wildgoose.utility.Constants;

public class WebListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent e) {
		ServletContext context = e.getServletContext();
		
		Map<String, Action> actionMap = new HashMap<String, Action>();
		actionMap.put(Constants.RESOURCE_INDEX, (ReporterCardService) context.getAttribute("ReporterCardService"));
		actionMap.put(Constants.RESOURCE_REPORTERS, (ArticleCardService) context.getAttribute("ArticleCardService"));
		
		context.setAttribute("ActionMap", actionMap);
		
		Map<String, Daction> dactionMap = new HashMap<String, Daction>();
		dactionMap.put(Constants.RESOURCE_REPORTERS, (GraphDataService) context.getAttribute("GraphDataService"));
		dactionMap.put(Constants.RESOURCE_SEARCH, (JsonDataService) context.getAttribute("JsonDataService"));
		dactionMap.put(Constants.RESOURCE_MORE_RPT_CARD, (JsonDataService) context.getAttribute("JsonDataService"));
		dactionMap.put(Constants.RESOURCE_HTML, (HtmlDocService) context.getAttribute("HtmlDocService"));
		dactionMap.put(Constants.RESOURCE_ACCOUNT, (AccountService) context.getAttribute("AccountService"));
		
		context.setAttribute("DactionMap", dactionMap);

	}
	
	public void contextDestroyed(ServletContextEvent e) {
		
	}

}
