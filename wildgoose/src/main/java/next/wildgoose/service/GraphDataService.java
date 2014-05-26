package next.wildgoose.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ExtractDAO;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;

public class GraphDataService implements Daction {
	public DactionResult execute(HttpServletRequest request) {
		Uri uri = new Uri(request);
		
		DactionResult result = null;
		JSONObject json = null;
		int reporterId = Integer.parseInt(uri.get(3));
		String apiName = uri.get(4);
		
		if (apiName == null) {
			return null;
		}
		
		Map<String, ExtractDAO> extractMap = (Map<String, ExtractDAO>) request.getServletContext().getAttribute("extractMap");
		ExtractDAO seledtedApi = extractMap.get(apiName);
		json = seledtedApi.getJson(reporterId, request);
		
		result = new DactionResult("json", json);
		return result;
	}
}
