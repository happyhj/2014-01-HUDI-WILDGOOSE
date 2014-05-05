package next.wildgoose.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignDAO.class.getName());
	
	public boolean findEmail (String email) {
		
		return "hello@wildgoose.com".equals(email);
	}
	
}
