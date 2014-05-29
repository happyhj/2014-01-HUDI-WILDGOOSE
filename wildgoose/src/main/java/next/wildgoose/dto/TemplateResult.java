package next.wildgoose.dto;

import java.util.Map;

import next.wildgoose.framework.Result;

public class TemplateResult extends Result {
	public TemplateResult(Map<String, String[]> parameters) {
		super(parameters);
	}

	public String getTemplate() {
		return (String) super.getData("template");
	}

	public void setTemplate(String templateString) {
		super.setData("template", templateString);
	}	
}
