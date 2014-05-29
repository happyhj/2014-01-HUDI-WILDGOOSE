package next.wildgoose.dto;

import java.util.List;
import java.util.Map;

import next.wildgoose.framework.Result;

public class SearchResult extends Result {
	public SearchResult(Map<String, String[]> parameters) {
		super(parameters);
	}
	public List<Reporter> getReporters() {
		return (List<Reporter>) super.getData("reporterCards");
	}

	public void setReporters(List<Reporter> Reporters) {
		super.setData("reporterCards", Reporters);
	}

	public String getSearchQuery() {
		return (String) super.getData("searchQuery");

	}
	public void setSearchQuery(String searchQuery) {
		super.setData("searchQuery", searchQuery);
	}
	
	public Boolean getHasMore() {
		return (Boolean) super.getData("hasMore");

	}
	public void setHasMore(boolean hasMore) {
		super.setData("hasMore", hasMore);
	}
	
	public int getTotalNum() {
		return (Integer) super.getData("totalNum");

	}
	public void setTotalNum(int totalNum) {
		super.setData("totalNum", totalNum);
	}
	
}
