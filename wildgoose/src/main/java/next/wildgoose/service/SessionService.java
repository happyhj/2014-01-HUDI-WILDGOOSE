package next.wildgoose.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Account;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.SHA256;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionService implements Daction {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class.getName());

	
	public DactionResult execute(HttpServletRequest request) {
		Uri uri = new Uri(request);
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String method = request.getMethod();
		HttpSession session = request.getSession();

		
		JSONObject json = failed();
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");		
		
		if("DELETE".equals(method)) {
			session.removeAttribute("userId");
		} else if("POST".equals(method)) {
			if(uri.check(3, "new")){
				LOGGER.debug("email: " + email + ", password: " + password);
				// get Random_num in session
				String randNum = (String) session.getAttribute("randNum");
				
				Account account = signDao.findAccount(email);
				if (account != null) {
					// H(db_password+random)
					if(SHA256.testSHA256(account.getPassword() + randNum).equals(password)){
						json = success();
						session.setAttribute("userId", account.getEmail());
						session.setMaxInactiveInterval(Constants.SESSION_EXPIRING_TIME);
						
					}	
				}		
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
