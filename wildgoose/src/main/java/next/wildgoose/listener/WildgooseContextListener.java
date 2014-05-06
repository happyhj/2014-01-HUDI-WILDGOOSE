package next.wildgoose.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.pool.DataSource;

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
		
		// DUMMY DATA
		//	sc.setAttribute("dummy", new DummyData());
	}

	public void contextDestroyed(ServletContextEvent event) {
		// 컨텍스트가 종료될 때 처리해야할 일
		// do-nothing
	}

}
