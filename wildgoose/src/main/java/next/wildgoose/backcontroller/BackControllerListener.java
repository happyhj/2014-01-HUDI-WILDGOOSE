package next.wildgoose.backcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BackControllerListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		Map<String, BackController> controllerMap;
		
		controllerMap = new HashMap<String, BackController>();
		controllerMap.put("search", new SearchController());
		controllerMap.put("reporters", new ReporterController());
		controllerMap.put("users", new UserController());
		controllerMap.put("template", new TemplateController());
		controllerMap.put("accounts", new AccountController());
		controllerMap.put("session", new SessionController());
		controllerMap.put("error", new ErrorController());
		
		context.setAttribute("controllerMap", controllerMap);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
