package next.wildgoose.dto.result;

import java.util.List;

import next.wildgoose.dto.Reporter;
import next.wildgoose.framework.Result;

public class FavoriteResult extends Result {

	public FavoriteResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Reporter> getFavorites() {
		return (List<Reporter>) super.getData("reporterCards");
	}
	public void setFavorites(String string, List<Reporter> reporters) {
		super.setData("reporterCards", reporters);
	}

}
