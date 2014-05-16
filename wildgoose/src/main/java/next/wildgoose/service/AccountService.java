package next.wildgoose.service;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dto.Account;
import next.wildgoose.utility.Uri;
import next.wildgoose.utility.Validation;

import org.json.JSONObject;

public class AccountService implements Daction {

	public JSONObject isValid(Uri uri, String email, String password) {
		JSONObject result = new JSONObject();
		String resultString = null;
		Account account = new Account(email, password);
		String option = uri.get(3);
		
		if ("up".equals(option)) {
			resultString = "Validation Failure";
			if (up(account)) {
				resultString = "Validation Success";
			}
		} else if ("email".equals(option)) {
			resultString = "true";
			if (hasEmail(email)) {
				resultString = "false";
			}
		}
		result.put("text", resultString);
		return result;
	}

	public boolean up (Account account) {
//		SignDAO signDAO = context.getAttribute("SignDAO");
		// validation 성공시
		if (Validation.isEmail(account.getEmail()) && Validation.isPassword(account.getPassword())) {
			// signDao 에서 회원등록하는 부분 실행 
			return true;
		}	
		
		// validation 실패시
		return false;
	}
	
	public boolean hasEmail(String email) {
//		SignDAO signDAO = SignDAO.getInstance();
		if (Validation.isEmail(email)) {
			// signDao email을 검색하는 부분 실행
			// result = singDao.findByEmail(email);
			return "hello@world.com".equals(email);
		}
		
		return false;
	}

	public DactionResult execute(HttpServletRequest request) {
		Uri uri = new Uri(request);
		String email = uri.get(4);
		String password = request.getParameter("password");
		JSONObject json = isValid(uri, email, password);
		DactionResult result = new DactionResult("text", json);
		return result;
	}
	
}
