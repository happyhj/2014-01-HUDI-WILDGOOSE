package next.wildgoose.backcontroller;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Account;
import next.wildgoose.utility.Uri;

public class AccountController implements BackController {

	@Override
	public Object execute(HttpServletRequest request) {
		Uri uri = new Uri(request);
		String feature = uri.get(1);
		
		if("new".equals(feature)){
			// 체크하고 유효한 경우 가입
			return join(request);
		} else if("email".equals(feature)){
			String email = uri.get(2);
			return usedEmail(request, email);
		}
		
		return null;
	}

	private Object usedEmail(HttpServletRequest request, String email) {
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		JSONObject json = failed();
		
		if(isJoinable(signDao, email)){
			json = success();
		}
		return json;
	}

	private Object join(HttpServletRequest request) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		ServletContext context = request.getServletContext();
		
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		Account account = new Account(email, password);
		JSONObject json = failed();
		
		if (isJoinable(signDao, email) == true && isHashedPassword(password) == true) {
			if (signDao.joinAccount(account) == true) {
				json = success();
				
				HttpSession session = request.getSession();
				session.setAttribute("userId", email);
			}
		}
		return json;
	}
	
	private boolean isJoinable(SignDAO signDao, String email) {
		if (isValidEmail(email)) {
			return signDao.findEmail(email);
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
