package next.wildgoose.service;

import javax.servlet.http.HttpServletRequest;

public interface Daction {
	public DactionResult execute(HttpServletRequest request);
}