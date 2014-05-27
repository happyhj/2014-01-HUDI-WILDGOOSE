package next.wildgoose.dto;

import java.util.List;
import java.util.Map;

public class AutocompleteResult extends Result {

	public AutocompleteResult(Map<String, String[]> parameters) {
		super(parameters);
	}
	
	public List<String> getNames() {
		return (List<String>) super.getData("names");
	}
	
	public void setNames(List<String> names) {
		super.setData("names", names);
	}

}
