package next.wildgoose.backcontroller;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.AccountResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.utility.Uri;

public class AccountController implements BackController {

	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		Uri uri = new Uri(request);
		String method = request.getMethod();
		String email;
		
		if("POST".equals(method)){
			// 체크하고 유효한 경우 가입
			result = join(request);
		} else if("GET".equals(method)){
			email = request.getParameter("email");
			result = usedEmail(request, email);
		} else if("DELETE".equals(method)){
			result = leave(request);
		}
		
		return result;
	}

	private Result leave(HttpServletRequest request) {
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
