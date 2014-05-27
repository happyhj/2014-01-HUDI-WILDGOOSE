package next.wildgoose.dto;

import java.util.List;
import java.util.Map;

public class ReporterResult extends Result{
	
	public ReporterResult(Map<String, String[]> parameters) {
		super(parameters);
	}
	
	public List<ArticleCard> getArticleCards() {
		return (List<ArticleCard>) super.getData("articleCards");
	}
	public void setArticleCards(List<ArticleCard> articleCards) {
		super.setData("articleCards", articleCards);
	}
	public ReporterCard getReporterCard() {
		return (ReporterCard) super.getData("reporterCard");
	}
	public void setReporterCard(ReporterCard reporterCard) {
		super.setData("reporterCard", reporterCard);
	}
	
}
