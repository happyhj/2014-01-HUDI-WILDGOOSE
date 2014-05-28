package next.wildgoose.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServiceListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		Map<String, Object> extractMap;
		
		ServletContext context = event.getServletContext();
		
		
		// IN Service
		 extractMap = new HashMap<String, Object>();
		 extractMap.put("number_of_articles", context.getAttribute("NumberOfArticlesDAO"));
		 extractMap.put("stat_points", context.getAttribute("DummyData"));
		
		context.setAttribute("extractMap", extractMap);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}
}
