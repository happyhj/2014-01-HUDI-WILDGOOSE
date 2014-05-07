package next.wildgoose.service;

import next.wildgoose.dao.JsonDAO;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.HtmlReader;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;

public class GetJsonData {
	private static GetJsonData textData;
	
	public static GetJsonData getInstance() {
		if (textData == null) {
			textData = new GetJsonData();
		}
		return textData;
	}
	
	public JSONObject getData(Uri uri) {
		JSONObject result = null;
		JsonDAO jsonDao = new JsonDAO();
		String name = uri.getParameter("name");
		result = jsonDao.getSimilarNames(name);
		return result;
	}
	/*
	public String getPartialHtml(Uri uri, String root) {
		PartialHtml phtml = null;
		String result = null;
		if ("create_account".equals(resourceName)) {
			phtml = new PartialHtml(root + Constants.PAGE_STATIC_ACCOUNT);
			result = phtml.read();
		} else if ("create_reporter_card".equals(resourceName)) {
			phtml = new PartialHtml(root + Constants.PAGE_STATIC_REPORTER_CARD);
			result = phtml.read();	
		}
		return result;
	}*/
}
