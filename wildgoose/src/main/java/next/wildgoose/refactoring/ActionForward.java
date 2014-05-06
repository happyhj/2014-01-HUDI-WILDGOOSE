package next.wildgoose.refactoring;

public class ActionForward {
	private boolean isRedirect = false;
	private String path = null;
	
	public ActionForward(boolean isRedirect, String path) {
		this.isRedirect = isRedirect;
		this.path = path;
	}
	
	public boolean isRedirect() {
		return this.isRedirect;
	}
	public String getPath() {
		return Constants.RESOURCE_ROOT + this.path;
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
