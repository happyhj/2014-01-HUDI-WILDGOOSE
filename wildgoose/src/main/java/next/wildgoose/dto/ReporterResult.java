package next.wildgoose.dto;

import java.util.List;
import java.util.Map;

public class ReporterResult extends Result {

	public ReporterResult(Map<String, String[]> parameters) {
		super(parameters);
	}

	public List<Article> getArticles() {
		return (List<Article>) super.getData("articles");
	}

	public void setArticles(List<Article> articles) {
		super.setData("articles", articles);
	}

	public Reporter getReporter() {
		return (Reporter) super.getData("reporter");
	}

	public void setReporter(Reporter reporter) {
		super.setData("reporter", reporter);
	}

	public List<NumberOfArticles> getNumberOfArticles() {
		return (List<NumberOfArticles>) super.getData("numberOfArticles");
	}

	public void setNumberOfArticles(List<NumberOfArticles> numberOfArticles) {
		super.setData("numberOfArticles", numberOfArticles);
	}

	public void setStatPoints(StatPoints statPoints) {
		super.setData("statPoints", statPoints);
	}
	
	public StatPoints getStatPoints() {
		return (StatPoints) super.getData("statPoints");
	}

}
