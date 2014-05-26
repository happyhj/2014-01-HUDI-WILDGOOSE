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
import next.wildgoose.service.Daction;
import next.wildgoose.service.DactionResult;
import next.wildgoose.service.Error;
import next.wildgoose.service.ReporterCardService;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

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

		// back컨트롤러를 가져온다.
		BackController backController = getBackController(context, reqPath);

		/// 가져온 back 컨트롤러에 일을 시키기 위해 request 객체를 인자로 넣어 일 수행 메서드를 실행한다.
		// 응답으로 OBJECT를 가져온다.
		Object resultData = backController.execute(request);
		
		// 요청(request path)를 View Picker 에 전달해서 대응하는 View 인터페이스의 구현체(JSPView or JSONView)를 가져온다.
		View view = getView(context, reqPath);
		
		// View객체에 OBJECT와 http response를 인자로 넘겨서 응답을 하도록 시킨다.	
		view.show(resultData, response);
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

		BackController defaultController = (ErrorController) context.getAttribute("Error");
		Map<String, BackController> controllerMap = WebListener.controllerMap;
		result = controllerMap.get(primeResource);
		if (result == null) {
			result = defaultController;
		}
		return result;
	}
	
	private String getPrimeResource(String uri) {
		String result = uri;
		
		if (uri.startsWith("/api/v1/")) {
			result = uri.replaceFirst("/api/v1/", "");
		}
		result.substring(0, result.indexOf("/"));
		return result;
	}
		
		
	private View getView(ServletContext context, String reqPath) {
		String target = null;
		
		// 요청종류에 따라 뷰 구현체의 인스턴스를 마련한다.
		if (reqPath.startsWith("/api/v1/")) {
			JSONView view = new JSONView();
			return view;
		} 
		
		JSPView view = new JSPView();
		//// JSPView의 경우 이 과정에서 내부적으로 대응하는 .jsp 파일을 멤버로 확보하도록 한다.
		Map<String, String> jspMap = WebListener.jspMap;
		target = jspMap.get(reqPath);
		view.setTarget(target);	
		return view;
	}
		
		
		
		
		
	void oldFunction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// 요청에 해당하는 action 인터페이스 구현체를 받아오기
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
