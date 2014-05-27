package next.wildgoose.dto;

import java.util.Map;

public class AccountResult extends Result{
	
	public AccountResult(Map<String, String[]> parameters) {
		super(parameters);
		
	}
	
	public String getEmail() {
		return (String)super.getData("email");
	}
	
	public void setEmail(String email) {
		super.setData("email", email);
	}

}
