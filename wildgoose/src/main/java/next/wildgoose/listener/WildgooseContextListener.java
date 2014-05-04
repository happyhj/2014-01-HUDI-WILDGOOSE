package next.wildgoose.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.HookingKeywordDAO;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dao.SignatureDAO;
import next.wildgoose.dummy.DummyData;
import next.wildgoose.model.DataSource;

public class WildgooseContextListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		
		// DATABASE
		String dbDriver = sc.getInitParameter("driverClassName");
		String dbUrl = sc.getInitParameter("dbUrl");
		String userName = sc.getInitParameter("userName");
		String userPassword = sc.getInitParameter("userPassword");
		// Static한 정보는 Parameter에서 가져옴
		
		// DATASOURCE init
		DataSource.init(dbDriver, userName, userPassword, dbUrl);
		
		// DAO
		sc.setAttribute("articleCardDAO", new ArticleCardDAO());
		sc.setAttribute("hookingKeywordDAO", new HookingKeywordDAO());
		sc.setAttribute("numberOfArticlesDAO", new NumberOfArticlesDAO());
		sc.setAttribute("reporterCardDAO", new ReporterCardDAO());
		sc.setAttribute("signatureDAO", new SignatureDAO());
		
		// DUMMY DATA
		sc.setAttribute("dummy", new DummyData());
	}

	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}

}
