package next.wildgoose.dto;

import next.wildgoose.framework.Result;

public class AccountResult extends Result{
	
	public AccountResult() {
		super();
	}
	
	public String getEmail() {
		return (String)super.getData("email");
	}
	
	public void setEmail(String email) {
		super.setData("email", email);
	}

}
