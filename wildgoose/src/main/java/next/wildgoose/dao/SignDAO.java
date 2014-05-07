package next.wildgoose.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignDAO {
	private static SignDAO signDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(SignDAO.class.getName());
	
	public static SignDAO getInstance() {
		if (signDao == null) {
			signDao = new SignDAO();
		}
		return signDao;
	}
	
	public boolean findEmail (String email) {
		
		return "hello@wildgoose.com".equals(email);
	}
}
