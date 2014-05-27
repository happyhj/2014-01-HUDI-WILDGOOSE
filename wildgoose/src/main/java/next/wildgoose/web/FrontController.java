package next.wildgoose.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import next.wildgoose.backcontroller.BackController;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Utility;
import next.wildgoose.view.JSONView;
import next.wildgoose.view.JSPView;
import next.wildgoose.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class.getName());

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();		
		// 로그인 유지(3일)를 위한 쿠키만료기간 재설정 
		if (session.getAttribute("userId") != null) {
			renewAuth(request, response);
		}
		String reqPath = request.getRequestURI();
		ServletContext context = request.getServletContext();
		
		LOGGER.debug("reqPath: " + reqPath);
		BackController backController = getBackController(context, reqPath);
		Object resultData = backController.execute(request);
		
		View view = createView(context, reqPath);
		view.show(resultData, request, response);
	}
	
	private void renewAuth(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();		
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
	
	// 요청(request path)에 해당하는 BackController 구현체를 받아오기
	private BackController getBackController(ServletContext context, String reqPath) {
		String primeResource = getPrimeResource(reqPath);
		BackController result = null;
		Map<String, BackController> controllerMap = (Map<String, BackController>) context.getAttribute("controllerMap");
		result = controllerMap.get(primeResource);
		if (result == null) {
			result = controllerMap.get("error");
		}
		return result;
	}
	
	private String getPrimeResource(String uri) {
		String result = uri;
		
		if (uri.startsWith("/api/v1/")) {
			result = uri.replaceFirst("/api/v1/", "");
		} else {
			result = uri.replaceFirst("/", "");
		}
		
		if( result.indexOf("/") >= 0 ) {
			result = result.substring(0, result.indexOf("/"));
		} 

		return result;
	}
		
		
	private View createView(ServletContext context, String reqPath) {
		String target = null;
		
		// 요청종류에 따라 뷰 구현체의 인스턴스를 마련한다.
		if (reqPath.startsWith("/api/v1/")) {
			JSONView view = new JSONView();
			return view;
		} 
		JSPView view = new JSPView();
		//// JSPView의 경우 이 과정에서 내부적으로 대응하는 .jsp 파일을 멤버로 확보하도록 한다.
		Map<String, String> jspMap = (Map<String, String>) context.getAttribute("jspMap");
		target = jspMap.get(getPrimeResource(reqPath));
		LOGGER.debug("target " + target);

		if(target == null) {
			target = "error.jsp";
		}
		view.setTarget(target);	
		return view;
	}
}
