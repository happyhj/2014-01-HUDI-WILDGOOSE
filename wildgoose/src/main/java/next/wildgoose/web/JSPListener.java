package next.wildgoose.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JSPListener implements ServletContextListener {
	
	
	public void contextInitialized(ServletContextEvent event) {
		// JSP MAP은 JSP Picker로 분리되어야 함.
		// 단순한 Map으로 처리할 수 없음.
		ServletContext context = event.getServletContext();
		
		Map<String, String> jspMap;

		jspMap = new HashMap<String, String>();
		jspMap.put("search", "searchReporter.jsp");
		jspMap.put("reporters", "showReporter.jsp");
		jspMap.put("users/favorites", "favorite_page.jsp");
		jspMap.put("users/timeline", "timeline.jsp");
		
		context.setAttribute("jspMap", jspMap);
	}
	
	public void contextDestroyed(ServletContextEvent e) {
		
	}

}
