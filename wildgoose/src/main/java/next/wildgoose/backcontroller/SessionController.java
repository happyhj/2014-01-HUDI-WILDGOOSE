package next.wildgoose.backcontroller;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.AccountResult;
import next.wildgoose.dto.SimpleResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.framework.security.RandomNumber;
import next.wildgoose.framework.security.SHA256;
import next.wildgoose.framework.utility.Uri;
import next.wildgoose.utility.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionController implements BackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class.getName());

	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		Uri uri = new Uri(request);
		String method = request.getMethod();
		
		if (uri.check(1, null)) {
			if ("POST".equals(method)) {
				result = login(request);
			}
			else if ("DELETE".equals(method)) {
				result = logout(request);
			}
			else if ("GET".equals(method)) {
				String email = request.getParameter("email");
				result = joinedEmail(request, email);
			}
		}
		else if (uri.check(1, "rand")) {
			result = getRanomNumber(request);
		}
		
		return result;
	}
	private AccountResult getRanomNumber(HttpServletRequest request) {
		AccountResult accountResult = new AccountResult();
		accountResult.setRand(RandomNumber.set(request.getSession()));
		return accountResult; 
	}
	
	private AccountResult joinedEmail(HttpServletRequest request, String email) {
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		AccountResult accountResult = new AccountResult();
		
		if(isJoinable(signDao, email)){
			accountResult.setStatus(500);
			accountResult.setMessage("fetching email info failed");
		} else {
			accountResult.setStatus(200);
			accountResult.setMessage("fetching email info succeed");
		}
		accountResult.setEmail(email);

		return accountResult;
	}
	
	private boolean isJoinable(SignDAO signDao, String email) {
		if (isValidEmail(email)) {
			return !signDao.findEmail(email);
		}
		return false;
	}
	
	private boolean isValidEmail(String email) {
		String regex = "^[\\w\\.-_\\+]+@[\\w-]+(\\.\\w{2,4})+$";

		return isFilled(email) && Pattern.matches(regex, email);
	}
	
	private boolean isFilled(String data) {
		
		if (data != null && data.length() > 0) {
			return true;
		}
		
		return false;
		
	}
	
	private SimpleResult login(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		HttpSession session = request.getSession();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		
		String email = request.getParameter("email");
		String hashedPassword = request.getParameter("password");
		String randNum = RandomNumber.get(session);

		SimpleResult simpleResult = new SimpleResult();
		LOGGER.debug("email: " + email + ", passw: " + hashedPassword);
		LOGGER.debug(randNum);
		
		String accountPw = signDao.findAccount(email);
		LOGGER.debug(accountPw + randNum);
		LOGGER.debug(SHA256.testSHA256(accountPw + randNum));
		if (accountPw == null) {
			// 가입되지 않은 아이디입니다. 다시 확인해주세요.
			return simpleResult;
		}
		// H(db_password+random)
		if(SHA256.testSHA256(accountPw + randNum).equals(hashedPassword)){
			
			simpleResult.setStatus(200);
			simpleResult.setMessage("getting user authentication succeed");
			simpleResult.setData("userId", email);
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
