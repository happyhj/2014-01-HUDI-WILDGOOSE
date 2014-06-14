package next.wildgoose.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.framework.utility.Uri;
import next.wildgoose.utility.Constants;

public class WebListener implements ServletContextListener {
	
	
	public void contextInitialized(ServletContextEvent event) {
		// JSP MAP은 JSP Picker로 분리되어야 함.
		// 단순한 Map으로 처리할 수 없음.
		ServletContext context = event.getServletContext();
		
		Map<Uri, String> jspMap;

		jspMap = new HashMap<Uri, String>();
		jspMap.put(new Uri(""), Constants.PAGE_SEARCH);
		jspMap.put(new Uri("search"), Constants.PAGE_SEARCH);
		jspMap.put(new Uri("reporters/[reporter_id]"), Constants.PAGE_REPORTER);
		jspMap.put(new Uri("me/[user_id]"), Constants.PAGE_ME);
		
		jspMap.put(null, Constants.PAGE_ERROR);
		
		context.setAttribute("jspMap", jspMap);
	}
	
	public void contextDestroyed(ServletContextEvent e) {
		
	}

}
