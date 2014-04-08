package next.wildgoose.model;

public class WebError {
	private String cause;
	private String notice;
	
	public WebError(String cause) {
		this.cause = cause;
		this.setNotice();
	}
	
	public String getCause() {
		
		return this.cause;
	}
	
	public String getNotice() {
		
		return this.notice;
	}
	
	private void setNotice() {
		
		if (cause == "whitespace") {
			this.notice = "검색할 수 없는 키워드 입니다.";
		}
	}
	
	
}
