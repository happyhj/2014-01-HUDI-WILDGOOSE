package next.wildgoose.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.dao.DummyData;
import next.wildgoose.dao.ExtractDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.utility.Constants;

public class ServiceListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		Map<String, ExtractDAO> extractMap;
		
		ServletContext context = event.getServletContext();
		
		
		// IN Service
		 extractMap = new HashMap<String, ExtractDAO>();
		 extractMap.put("number_of_articles", (NumberOfArticlesDAO) context.getAttribute("NumberOfArticlesDAO"));
		 extractMap.put("stat_points", (DummyData) context.getAttribute("DummyData"));
		
		context.setAttribute("extractMap", extractMap);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}
}
