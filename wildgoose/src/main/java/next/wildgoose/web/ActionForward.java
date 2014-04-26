package next.wildgoose.web;

public class ActionForward {
	private boolean isRedirect = false;
	private String path = null;
	
	public boolean isRedirect() {
		return this.isRedirect;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setRedirect(boolean flag) {
		this.isRedirect = flag;
	}
	
	public void setPath(String newPath) {
		this.path = newPath;
	}
}
