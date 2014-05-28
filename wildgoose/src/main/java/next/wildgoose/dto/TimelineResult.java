package next.wildgoose.dto;

import java.util.List;
import java.util.Map;

import next.wildgoose.framework.Result;

public class TimelineResult extends Result {

	public TimelineResult(Map<String, String[]> parameters) {
		super(parameters);
	}
	
	public List<Article> getAtricles() {
		return (List<Article>) super.getData("articles");
	}
	public void setArticles(String string, List<Article> articles) {
		super.setData("articles", articles);
	}

}
