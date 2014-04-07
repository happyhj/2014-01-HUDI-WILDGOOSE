package next.wildgoose.model;

public class Reporter {
	private String email;
	private String authorInfo;
	private String articleURL;
	private String articleTitle;
	private String pressName;

	public String getEmail() {
		return this.email;
	}
	public String getAuthorInfo() {
		return this.authorInfo;
	}
	public String getArticleURL() {
		return this.articleURL;
	}
	public String getArticleTitle() {
		return this.articleTitle;
	}
	public String getPressName() {
		return this.pressName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setAuthorInfo(String authorInfo) {
		this.authorInfo = authorInfo;
	}
	public void setArticleURL(String articleURL) {
		this.articleURL = articleURL;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public void setPressName(String pressName) {
		this.pressName = pressName;
	}
	
	
}
