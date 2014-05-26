package next.wildgoose.web;

import javax.servlet.http.HttpServletResponse;

public interface View {
	public void show(Object resultData, HttpServletResponse response);

	public void setTarget(String target);
}
