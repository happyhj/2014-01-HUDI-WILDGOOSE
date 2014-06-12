package next.wildgoose.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import next.wildgoose.framework.utility.Uri;

public class WebListener implements ServletContextListener {
	
	
	public void contextInitialized(ServletContextEvent event) {
		// JSP MAP은 JSP Picker로 분리되어야 함.
		// 단순한 Map으로 처리할 수 없음.
		ServletContext context = event.getServletContext();
		
		Map<Uri, String> jspMap;

		jspMap = new HashMap<Uri, String>();
		jspMap.put(new Uri(""), "search.jsp");
//		jspMap.put(new Uri(""), "error.jsp");

		jspMap.put(new Uri("search"), "search.jsp");
		jspMap.put(new Uri("reporters/[reporter_id]"), "reporters.jsp");
//		jspMap.put(new Uri("users/[user_id]/favorites"), "favorites.jsp");
//		jspMap.put(new Uri("users/[user_id]/timeline"), "timeline.jsp");
//		jspMap.put(new Uri("users/[user_id]/mypage"), "mypage.jsp");
		jspMap.put(new Uri("me/[user_id]"), "me.jsp");
//		jspMap.put(new Uri("accounts"), "search.jsp");
//		jspMap.put(new Uri("accounts/login"), "login.jsp");
//		jspMap.put(new Uri("accounts/signup"), "signup.jsp");
		jspMap.put(null, "error.jsp");
		
		context.setAttribute("jspMap", jspMap);
	}
	
	public void contextDestroyed(ServletContextEvent e) {
		
	}

}
