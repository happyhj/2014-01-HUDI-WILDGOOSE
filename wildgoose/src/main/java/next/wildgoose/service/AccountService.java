package next.wildgoose.service;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Account;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountService implements Daction {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class.getName());

	public boolean isJoinable(SignDAO signDao, String email) {
		if (isValidEmail(email)) {
			return signDao.findEmail(email);
		}
		return false;
	}
	
	
	private static boolean isValidEmail(String email) {
		String regex = "^[\\w\\.-_\\+]+@[\\w-]+(\\.\\w{2,4})+$";

		return isFilled(email) && Pattern.matches(regex, email);
	}

	private static boolean isHashedPassword(String password) {
		if (password.length() == 64) {
			return true;
		}
		else return false;
	}
	
	public static boolean isFilled(String data) {
		
		if (data != null && data.length() > 0) {
			return true;
		}
		
		return false;
		
	}

	public DactionResult execute(HttpServletRequest request) {
		Uri uri = new Uri(request);
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		Account account = new Account(email, password);
		JSONObject json = failed();
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		
		if(uri.check(3, "new")){
			if (isJoinable(signDao, email) == true && isHashedPassword(password) == true) {
				if (signDao.joinAccount(account) == true) {
					json = success();
					HttpSession session = request.getSession();
					session.setAttribute("userId", email);
				}
			}
		} else {
			email = uri.get(3);
			LOGGER.debug("email: " + email);
			if(isJoinable(signDao, email)){
				json = success();
			}
		}
		
		DactionResult result = new DactionResult("text", json);
		return result;
	}
	
	private JSONObject success() {		
		JSONObject result = new JSONObject();
		result.put("text", "success");
		return result;
	}
	
	private JSONObject failed(){
		JSONObject result = new JSONObject();
		result.put("text", "failed");
		return result;
	}
	
	
	
}
