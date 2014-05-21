package next.wildgoose.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.dao.DummyData;
import next.wildgoose.dao.ExtractDAO;
import next.wildgoose.dao.HookingKeywordDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;

public class ServiceListener implements ServletContextListener {
	public static Map<String, ExtractDAO> extractMap;
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		
		// SERVICE
		sc.setAttribute("ReporterCardService", new ReporterCardService());
		sc.setAttribute("ArticleCardService", new ArticleCardService());
		sc.setAttribute("GraphDataService", new GraphDataService());
		sc.setAttribute("JsonDataService", new JsonDataService());
		sc.setAttribute("AccountService", new AccountService());
		sc.setAttribute("SessionService", new SessionService());
		sc.setAttribute("HtmlDocService", new HtmlDocService());
		sc.setAttribute("UserService", new UserService());
		sc.setAttribute("FavoriteService", new FavoriteService());
		sc.setAttribute("Error", new Error());
		sc.setAttribute("ErrorDaction", new ErrorDaction());
		
		// IN Service
		extractMap = new HashMap<String, ExtractDAO>();
		extractMap.put("number_of_articles", (NumberOfArticlesDAO) sc.getAttribute("NumberOfArticlesDAO"));
		extractMap.put("number_of_hook_keywords", (HookingKeywordDAO) sc.getAttribute("HookingKeywordDAO"));
		extractMap.put("stat_points", (DummyData) sc.getAttribute("DummyData"));
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}
}
