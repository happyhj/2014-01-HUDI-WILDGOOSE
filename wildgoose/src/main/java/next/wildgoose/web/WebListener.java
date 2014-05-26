package next.wildgoose.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.backcontroller.BackController;
import next.wildgoose.backcontroller.ErrorController;
import next.wildgoose.backcontroller.SearchController;
import next.wildgoose.service.Action;
import next.wildgoose.service.Daction;

public class WebListener implements ServletContextListener {
	
	public static Map<String, Action> actionMap;
	public static Map<String, Daction> dactionMap;
	public static Map<String, BackController> controllerMap;
	public static Map<String, String> jspMap;
	public void contextInitialized(ServletContextEvent e) {
		ServletContext context = e.getServletContext();
		
		context.setAttribute("Error", new ErrorController());
		
		controllerMap = new HashMap<String, BackController>();
		controllerMap.put("search", new SearchController());

		jspMap = new HashMap<String, String>();
		jspMap.put("search", "SearchReporter.jsp");
	}
	
	public void contextDestroyed(ServletContextEvent e) {
		
	}

}
