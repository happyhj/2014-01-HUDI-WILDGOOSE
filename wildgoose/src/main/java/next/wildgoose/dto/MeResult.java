package next.wildgoose.dto;

import java.util.List;

import next.wildgoose.framework.Result;

public class MeResult extends Result{
	
	public MeResult() {
		super(null);
	}
	
	public MeResult(String pageName) {
		super(pageName);
	}
	
	public List<Article> getArticles() {
		return (List<Article>) super.getData("articles");
	}
	public void setArticles(String string, List<Article> articles) {
		super.setData("articles", articles);
	}

	public List<Reporter> getFavorites() {
		return (List<Reporter>) super.getData("reporterCards");
	}
	public void setFavorites(String string, List<Reporter> reporters) {
		super.setData("reporterCards", reporters);
	}

}
