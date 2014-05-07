package next.wildgoose.service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Account;
import next.wildgoose.utility.Uri;
import next.wildgoose.utility.Validation;

public class SignAccount implements Daction {

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
			resultString = "OK";
			if (hasEmail(email)) {
				resultString = "";
			}
		}
		result.put("result", resultString);
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

	@Override
	public DactionResult execute(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
