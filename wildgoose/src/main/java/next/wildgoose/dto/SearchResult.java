package next.wildgoose.dto;

import java.util.List;

public class SearchResult {
	public List<ReporterCard> reporterCards;
	public String searchQuery;
	
	public List<ReporterCard> getReporterCards() {
		return this.reporterCards;
	}

	public void setReporterCards(List<ReporterCard> list) {
		this.reporterCards = list;
	}

	public String getSearchQuery() {
		System.out.println("this!!!!");
		return this.searchQuery;
	}
	
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

}
