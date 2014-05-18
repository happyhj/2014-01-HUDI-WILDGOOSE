package next.wildgoose.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServiceListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		
		// SERVICE
		sc.setAttribute("ReporterCardService", new ReporterCardService());
		sc.setAttribute("ArticleCardService", new ArticleCardService());
		sc.setAttribute("GraphDataService", new GraphDataService());
		sc.setAttribute("JsonDataService", new JsonDataService());
		sc.setAttribute("AccountService", new AccountService());
		sc.setAttribute("HtmlDocService", new HtmlDocService());
		sc.setAttribute("Error", new Error());
		sc.setAttribute("ErrorDaction", new ErrorDaction());
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}
}
