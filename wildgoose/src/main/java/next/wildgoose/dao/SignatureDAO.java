package next.wildgoose.dao;

import next.wildgoose.dummy.DummyData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignatureDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignatureDAO.class.getName());
	DummyData dummy = new DummyData();
	
	public boolean findEmail (String email) {
		
		return dummy.getEmail().equals(email);
	}
	
}
