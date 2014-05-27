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
		Map<String, Action> actionMap;
		Map<String, Daction> dactionMap;
		Map<String, ExtractDAO> extractMap;
		
		ServletContext context = event.getServletContext();
		
		actionMap = new HashMap<String, Action>();
		actionMap.put(Constants.RESOURCE_INDEX, new ReporterCardService());
		actionMap.put(Constants.RESOURCE_REPORTERS, new ArticleCardService());
		actionMap.put(Constants.RESOURCE_TIMELINE, new TimeLineService());
		actionMap.put(Constants.RESOURCE_FAVORITE_PAGE, new FavoritePageService());
		
		dactionMap = new HashMap<String, Daction>();
		dactionMap.put(Constants.RESOURCE_REPORTERS, new GraphDataService());
		dactionMap.put(Constants.RESOURCE_SEARCH, new JsonDataService());
		dactionMap.put(Constants.RESOURCE_MORE_RPT_CARD, new JsonDataService());
		dactionMap.put(Constants.RESOURCE_HTML, new HtmlDocService());
		dactionMap.put(Constants.RESOURCE_ACCOUNT, new AccountService());
		dactionMap.put(Constants.RESOURCE_SESSION, new SessionService());
		dactionMap.put(Constants.RESOURCE_USER, new UserService());
		dactionMap.put(Constants.RESOURCE_FAVORITE, new FavoriteService());
		
		
		// IN Service
		 extractMap = new HashMap<String, ExtractDAO>();
		 extractMap.put("number_of_articles", (NumberOfArticlesDAO) context.getAttribute("NumberOfArticlesDAO"));
		 extractMap.put("stat_points", (DummyData) context.getAttribute("DummyData"));
		
		context.setAttribute("actionMap", actionMap);
		context.setAttribute("dactionMap", dactionMap);
		context.setAttribute("extractMap", extractMap);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}
}
