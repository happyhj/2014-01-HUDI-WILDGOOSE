package next.wildgoose.accessdao;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.JsonDAO;
import next.wildgoose.dao.PartialHtml;
import next.wildgoose.web.Wildgoose;

public class GetTextData {
	private HttpServletRequest request;
	public GetTextData(HttpServletRequest request) {
		this.request = request;
	}
	public String getPartialHtml(String resourceName) {
		ServletContext context = this.request.getServletContext();
		PartialHtml phtml = null;
		String path = context.getRealPath(Wildgoose.RESOURCE_ROOT);
		String result = null;
		if ("create_account".equals(resourceName)) {
			phtml = new PartialHtml(path + Wildgoose.PAGE_STATIC_ACCOUNT);
			result = phtml.read();
		} else if ("create_reporter_card".equals(resourceName)) {
			phtml = new PartialHtml(path + Wildgoose.PAGE_STATIC_REPORTER_CARD);
			result = phtml.read();	
		}
		return result;
	}
	
	public String getJsonString(String name) {
		String result = null;
		JsonDAO jsonDao = new JsonDAO();
		result = jsonDao.getSimilarNames(name);
		return result;
	}
}
