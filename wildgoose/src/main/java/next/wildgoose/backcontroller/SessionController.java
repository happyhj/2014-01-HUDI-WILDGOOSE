package next.wildgoose.backcontroller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Account;
import next.wildgoose.dto.Result;
import next.wildgoose.dto.SimpleResult;
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
		SimpleResult simpleResult = new SimpleResult(request.getParameterMap());
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		
		HttpSession session = request.getSession();
		String randNum = (String) session.getAttribute("randNum");

		Account account = signDao.findAccount(email);
		if (account == null) {
			// 가입되지 않은 아이디입니다. 다시 확인해주세요.
			return simpleResult;
		}
		// H(db_password+random)
		if(SHA256.testSHA256(account.getPassword() + randNum).equals(password)){
			simpleResult.setStatus(200);
			simpleResult.setMessage("getting user authentication succeed");
			session.setAttribute("userId", account.getEmail());
			session.setMaxInactiveInterval(Constants.SESSION_EXPIRING_TIME);
		} else {
			simpleResult.setMessage("getting user authentication failed");
		}
		return simpleResult;
	}

	private SimpleResult logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("userId");
		
		SimpleResult simpleResult = new SimpleResult(request.getParameterMap());
		simpleResult.setStatus(200);
	    simpleResult.setMessage("removing user authentication succeed");
		return simpleResult;
	}
}
