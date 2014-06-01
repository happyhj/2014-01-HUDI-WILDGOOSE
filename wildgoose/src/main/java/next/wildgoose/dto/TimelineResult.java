package next.wildgoose.dto;

import java.util.List;
import java.util.Map;

import next.wildgoose.framework.Result;

public class TimelineResult extends Result {

	public TimelineResult() {
		super();
	}
	
	public List<Article> getArticles() {
		return (List<Article>) super.getData("articles");
	}
	public void setArticles(String string, List<Article> articles) {
		super.setData("articles", articles);
	}

}
