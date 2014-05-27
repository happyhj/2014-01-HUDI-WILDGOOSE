package next.wildgoose.dto;

public class Reporter {
	private int id;
	private String email;
	private String name;
	private String pressName;
	private String articleTitle;
	private String articleURL;
	
	public String getArticleURL() {
		return articleURL;
	}

	public void setArticleURL(String articleURL) {
		this.articleURL = articleURL;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPressName() {
		return pressName;
	}
	
	public void setPressName(String pressName) {
		this.pressName = pressName;
	}
	
	public String getArticleTitle() {
		return articleTitle;
	}
	
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
