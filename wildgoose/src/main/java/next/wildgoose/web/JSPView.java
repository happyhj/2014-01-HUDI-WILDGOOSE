package next.wildgoose.web;

import javax.servlet.http.HttpServletResponse;

public class JSPView implements View {

	private String target;

	@Override
	public void show(Object resultData, HttpServletResponse response) {
		
	}

	@Override
	public void setTarget(String target) {
		this.target = target;
		
	}


}
