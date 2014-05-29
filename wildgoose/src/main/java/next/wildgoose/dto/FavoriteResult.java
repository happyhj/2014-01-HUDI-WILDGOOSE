package next.wildgoose.dto;

import java.util.List;
import java.util.Map;

import next.wildgoose.framework.Result;

public class FavoriteResult extends Result {

	public FavoriteResult(Map<String, String[]> parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	public List<Reporter> getFavorites() {
		return (List<Reporter>) super.getData("reporterCards");
	}
	public void setFavorites(String string, List<Reporter> reporters) {
		super.setData("reporterCards", reporters);
	}

}
