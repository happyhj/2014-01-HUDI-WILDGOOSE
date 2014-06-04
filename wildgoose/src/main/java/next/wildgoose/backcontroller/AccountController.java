package next.wildgoose.backcontroller;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.AccountResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.utility.SHA256;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountController implements BackController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class.getName());
	
	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		Uri uri = new Uri(request);
		String method = request.getMethod();
		String email;
		LOGGER.debug("uri: " + uri.get(0) + ",  " + uri.get(1));

		if (uri.check(1, null)) {
			if("POST".equals(method)){
				// 체크하고 유효한 경우 가입
				if(request.getParameter("check") == null){
					result = join(request);
				}
				else {
					result = withdraw(request);
				}
			}
			else if("GET".equals(method)){
				email = request.getParameter("email");
				result = usedEmail(request, email);
			}
		}
		else if (uri.check(1, "login")) {
			LOGGER.debug("this is login");
			
			result = new AccountResult();
			result.setStatus(200);
			
		}
		else if (uri.check(1, "signup")) {
			LOGGER.debug("this is signup");
			result = new AccountResult();
			result.setStatus(200);
		}
		return result;
	}

	private Result withdraw(HttpServletRequest request) {
		AccountResult simpleResult = new AccountResult();
		String email = request.getParameter("email");
		String hashedPassword = request.getParameter("password");
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		
		HttpSession session = request.getSession();
		String randNum = (String) session.getAttribute("randNum");
		
		String accountPw = signDao.findAccount(email);
		if (accountPw == null) {
			// 비밀번호 틀려서 탈퇴 못함! 
			return simpleResult;
		}
		// H(db_password+random)
		if(SHA256.testSHA256(accountPw + randNum).equals(hashedPassword)){
			simpleResult = (AccountResult) leave(request);
		} else {
			simpleResult.setMessage("getting user authentication failed");
		}
		return simpleResult;
	}

	private Result leave(HttpServletRequest request) {
		//확인하기 추가
		String email = request.getParameter("email");
		
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		AccountResult accountResult = new AccountResult();
		
		// 기본 세팅 fail
		accountResult.setMessage("withdrawing user account failed");
				
		if (signDao.withdrawAccount(email) == true) {
			// 탈퇴 성공
			accountResult.setStatus(200);
			accountResult.setMessage("withdrawing user account succeed");
			
			HttpSession session = request.getSession();
			session.removeAttribute("userId");
		}
			
		return accountResult;
	}

	private AccountResult usedEmail(HttpServletRequest request, String email) {
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		AccountResult accountResult = new AccountResult();
		
		if(isJoinable(signDao, email)){
			accountResult.setStatus(200);
			accountResult.setMessage("fetching account info succeed");
		} else {
			accountResult.setStatus(500);
			accountResult.setMessage("fetching account info failed");
		}
		accountResult.setEmail(email);

		return accountResult;
	}

	private AccountResult join(HttpServletRequest request) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		ServletContext context = request.getServletContext();
		
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		AccountResult accountResult = new AccountResult();
		
		// 기본 세팅 fail
		accountResult.setMessage("adding user account failed");
		
		if (isJoinable(signDao, email) == true && isHashedPassword(password) == true) {
			if (signDao.joinAccount(email, password) == true) {
				// 가입 성공
				accountResult.setStatus(200);
				accountResult.setMessage("adding user account succeed");
				
				HttpSession session = request.getSession();
				session.setAttribute("userId", email);
			}
		}
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

	private boolean isHashedPassword(String password) {
		if (password.length() == 64) {
			return true;
		}
		else return false;
	}
	
	private boolean isFilled(String data) {
		
		if (data != null && data.length() > 0) {
			return true;
		}
		
		return false;
		
	}
}
