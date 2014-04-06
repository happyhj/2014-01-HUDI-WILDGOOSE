package next.wildgoose.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.model.DatabaseConnector;

public class DatabaseContextListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		String dbURL = sc.getInitParameter("dbURL");
		String userName = sc.getInitParameter("userName");
		String userPassword = sc.getInitParameter("userPassword");
		// Static한 정보는 Parameter에서 가져옴
		
		DatabaseConnector.connect(dbURL, userName, userPassword);
	}

	public void contextDestroyed(ServletContextEvent event) {
		DatabaseConnector.close();
		// 컨텍스트가 종료될 때 처리해야할 일
	}

}
