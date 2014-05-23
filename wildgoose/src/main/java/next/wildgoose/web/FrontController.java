package next.wildgoose.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import next.wildgoose.service.Action;
import next.wildgoose.service.ActionResult;
import next.wildgoose.service.ArticleCardService;
import next.wildgoose.service.Error;
import next.wildgoose.service.ReporterCardService;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class.getName());
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
//		로그인 상태 테스트
//		session.setAttribute("userId", "hello@world.com");
		if (session.getAttribute("userId") != null) {
			session.setMaxInactiveInterval(Constants.SESSION_EXPIRING_TIME);
			Cookie[] cookies = request.getCookies();
			Cookie jsessionid = null;

			for (int i=0; i<cookies.length; i++) {
				Cookie cookie = cookies[i];
				if(cookie.getName().equals("JSESSIONID")) {
					jsessionid = cookie;
				}
			}
			if(jsessionid != null) {
				jsessionid.setMaxAge(Constants.SESSION_EXPIRING_TIME);
			}
			response.addCookie(jsessionid);
		}
		
		Action action = getProperAction(request);
		ActionResult result = action.execute(request);
		
		if (result.isRedirect()) {
			response.sendRedirect(result.getPath());
			return;
		}
		
		RequestDispatcher reqDispatcher = request.getRequestDispatcher(result.getPath());
		reqDispatcher.forward(request, response);
	}
	
	private Action getProperAction(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		LOGGER.debug(uri.get(0));

		Action defaultAction = (Error) context.getAttribute("Error");
		Map<String, Action> actionMap = WebListener.actionMap;
		Action result = actionMap.get(uri.get(0));
		if (result == null) {
			result = defaultAction;
		}
		return result;
	}
}
