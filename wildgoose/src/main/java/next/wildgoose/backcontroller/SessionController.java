package next.wildgoose.backcontroller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.SimpleResult;
import next.wildgoose.framework.Result;
import next.wildgoose.framework.BackController;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.SHA256;
import next.wildgoose.utility.Uri;

public class SessionController implements BackController {

	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		String method = request.getMethod();
		Uri uri = new Uri(request);
		
		if ("POST".equals(method) && uri.check(1, "new")) {
			result = login(request);
		} else if ("DELETE".equals(method)) {
			result = logout(request);
		}
		
		return result;
	}

	private SimpleResult login(HttpServletRequest request) {
		SimpleResult simpleResult = new SimpleResult();
		
		String email = request.getParameter("email");
		String hashedPassword = request.getParameter("password");
		
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		
		HttpSession session = request.getSession();
		String randNum = (String) session.getAttribute("randNum");

		String accountPw = signDao.findAccount(email);
		if (accountPw == null) {
			// 가입되지 않은 아이디입니다. 다시 확인해주세요.
			return simpleResult;
		}
		// H(db_password+random)
		if(SHA256.testSHA256(accountPw + randNum).equals(hashedPassword)){
			simpleResult.setStatus(200);
			simpleResult.setMessage("getting user authentication succeed");
			session.setAttribute("userId", email);
			session.setMaxInactiveInterval(Constants.SESSION_EXPIRING_TIME);
		} else {
			simpleResult.setMessage("getting user authentication failed");
		}
		return simpleResult;
	}

	private SimpleResult logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("userId");
		
		SimpleResult simpleResult = new SimpleResult();
		simpleResult.setStatus(200);
	    simpleResult.setMessage("removing user authentication succeed");
		return simpleResult;
	}
}
