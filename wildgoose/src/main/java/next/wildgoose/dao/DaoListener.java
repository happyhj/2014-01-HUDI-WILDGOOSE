package next.wildgoose.dao;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DaoListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		
		// DAO
		sc.setAttribute("ArticleCardDAO", new ArticleCardDAO());
		sc.setAttribute("HookingKeywordDAO", new HookingKeywordDAO());
		sc.setAttribute("NumberOfArticlesDAO", new NumberOfArticlesDAO());
		sc.setAttribute("ReporterCardDAO", new ReporterCardDAO());
		sc.setAttribute("JsonDAO", new JsonDAO());
		sc.setAttribute("SignDAO", new SignDAO());
		sc.setAttribute("FavoriteDAO", new FavoriteDAO());
		sc.setAttribute("SearchKeywordDAO", new SearchKeywordDAO());
		sc.setAttribute("DummyData", new DummyData());
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}

}
