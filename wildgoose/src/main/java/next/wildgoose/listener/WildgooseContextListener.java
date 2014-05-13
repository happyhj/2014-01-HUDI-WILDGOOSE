package next.wildgoose.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.DummyData;
import next.wildgoose.dao.HookingKeywordDAO;
import next.wildgoose.dao.JsonDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dao.SearchKeywordDAO;
import next.wildgoose.dao.SignDAO;
import next.wildgoose.pool.DataSource;
import next.wildgoose.service.AccountService;
import next.wildgoose.service.ArticleCardService;
import next.wildgoose.service.Error;
import next.wildgoose.service.ErrorDaction;
import next.wildgoose.service.GraphDataService;
import next.wildgoose.service.HtmlDocService;
import next.wildgoose.service.JsonDataService;
import next.wildgoose.service.ReporterCardService;

public class WildgooseContextListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		
		// DATABASE
		String dbDriver = sc.getInitParameter("driverClassName");
		String dbUrl = sc.getInitParameter("dbUrl");
		String userName = sc.getInitParameter("userName");
		String userPassword = sc.getInitParameter("userPassword");
		DataSource.init(dbDriver, userName, userPassword, dbUrl);
		
		// SERVICE
		sc.setAttribute("ReporterCardService", new ReporterCardService());
		sc.setAttribute("ArticleCardService", new ArticleCardService());
		sc.setAttribute("GraphDataService", new GraphDataService());
		sc.setAttribute("JsonDataService", new JsonDataService());
		sc.setAttribute("AccountService", new AccountService());
		sc.setAttribute("HtmlDocService", new HtmlDocService());
		sc.setAttribute("Error", new Error());
		sc.setAttribute("ErrorDaction", new ErrorDaction());
		
		// DAO
		sc.setAttribute("ArticleCardDAO", new ArticleCardDAO());
		sc.setAttribute("HookingKeywordDAO", new HookingKeywordDAO());
		sc.setAttribute("NumberOfArticlesDAO", new NumberOfArticlesDAO());
		sc.setAttribute("ReporterCardDAO", new ReporterCardDAO());
		sc.setAttribute("JsonDAO", new JsonDAO());
		sc.setAttribute("SignDAO", new SignDAO());
		sc.setAttribute("SearchKeywordDAO", new SearchKeywordDAO());
		sc.setAttribute("DummyData", new DummyData());
		
		
	}

	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}

}
