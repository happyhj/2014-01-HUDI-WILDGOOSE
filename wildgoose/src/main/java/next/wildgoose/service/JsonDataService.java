package next.wildgoose.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.JsonDAO;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDataService implements Daction {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonDataService.class.getName());
	public DactionResult execute(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		
		JsonDAO jsonDao = (JsonDAO) context.getAttribute("JsonDAO");
		JSONObject json = null;
		LOGGER.debug(uri.toString());
		if (uri.check(3, Constants.RESOURCE_MOST_SMR_NAME)) {
			String name = request.getParameter("name");
			json = jsonDao.getSimilarNames(name);
		} else if (uri.check(2, Constants.RESOURCE_MORE_RPT_CARD)) {
			String name = request.getParameter("q");
			int start = Integer.parseInt(request.getParameter("last"));
			int num = Integer.parseInt(request.getParameter("req"));
			json = jsonDao.moreReporterCard(name, start, num);
		}
		
		DactionResult result = new DactionResult("json", json);
		LOGGER.debug(json.toString());
		return result;
	}

}
