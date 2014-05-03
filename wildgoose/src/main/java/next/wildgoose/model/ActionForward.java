package next.wildgoose.model;

import next.wildgoose.utility.Wildgoose;

public class ActionForward {
	private boolean isRedirect = false;
	private String path = null;
	
	public boolean isRedirect() {
		return this.isRedirect;
	}
	
	// 절대경로로 출력
	public String getPath() {
		return Wildgoose.RESOURCE_ROOT + this.path;
	}
	
	public void setRedirect(boolean flag) {
		this.isRedirect = flag;
	}
	
	public void setPath(String newPath) {
		this.path = newPath;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("redirect: ");
		sb.append(this.isRedirect);
		sb.append(",  path: ");
		sb.append(this.path);
		
		return sb.toString(); 
	}
	
	
}
