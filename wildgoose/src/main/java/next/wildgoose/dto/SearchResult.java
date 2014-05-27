package next.wildgoose.dto;

import java.util.List;
import java.util.Map;

public class SearchResult extends Result {
	public SearchResult(Map<String, String[]> parameters) {
		super(parameters);
	}
	public List<ReporterCard> getReporterCards() {
		return (List<ReporterCard>) super.getData("reporterCards");
	}

	public void setReporterCards(List<ReporterCard> ReporterCards) {
		super.setData("reporterCards", ReporterCards);
	}

	public String getSearchQuery() {
		return (String) super.getData("searchQuery");

	}
	public void setSearchQuery(String searchQuery) {
		super.setData("searchQuery", searchQuery);
	}
}
