package next.wildgoose.service;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Account;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;

public class AccountService implements Daction {

	public boolean isJoinedEmail(String email) {
//		SignDAO signDAO = SignDAO.getInstance();
		if (isValidEmail(email)) {
			// signDao email을 검색하는 부분 실행
			// result = singDao.findByEmail(email);
			return "hello@world.com".equals(email);
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
		
		if (data.length() > 0) {
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
		
		if(uri.check(3, "up")){
			if (isJoinedEmail(email) == false && isHashedPassword(password) == true) {
				if (signDao.joinAccount(account) == true) {
					json = success();
				}
			}
		} else if (uri.check(3, "email")) {
			if(!isJoinedEmail(email)){
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
