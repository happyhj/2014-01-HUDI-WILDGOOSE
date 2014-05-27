package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ReporterDAO;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.SearchResult;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;
import next.wildgoose.utility.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchController implements BackController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterController.class.getName());
	
	@Override
	public Object execute(HttpServletRequest request) {
		Object result = null;
		Uri uri = new Uri(request);

		if (uri.check(1, "autocomplete")) {
			// TODO: autocomplete API 구현
			result = getAutocompleteResult(request);
		} else if (uri.get(1) == null){
			result = getSearchResult(request);
		}
		return result;
	}
	
	
	private Object getSearchResult(HttpServletRequest request) {
		SearchResult searchResult = new SearchResult(request.getParameterMap());
		boolean hasMore = false;
		List<Reporter> reporters = null;
		ServletContext context = request.getServletContext();
		
		String searchQuery = request.getParameter("q");
		LOGGER.debug("searchQuery: " + searchQuery);

		if (searchQuery == null) {
			searchResult.setStatus(200);
			searchResult.setMessage("welcome to search page! This path is not provided as API.");
			return searchResult;
		}
		
		searchQuery.replaceAll("%", "");
		
		String trimmedQuery = searchQuery.trim();
		if ("".equals(trimmedQuery)) {
			searchResult.setStatus(500);
			searchResult.setMessage("You can not search with whitespace");
			return searchResult;
		}
		
		// 25개를 가져온 후, 마지막 카드를 지움.
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		reporters = getReporters(reporterDao, searchQuery, 0, Constants.NUM_OF_CARDS + 1);
		if (reporters.size() > Constants.NUM_OF_CARDS) {
			hasMore = true;
			reporters.remove(Constants.NUM_OF_CARDS);
		}		
		
		searchResult.setStatus(200);
		searchResult.setMessage("getting search result success");
		searchResult.setTotalNum(Constants.NUM_OF_CARDS);
		searchResult.setReporters(reporters);
		searchResult.setHasMore(hasMore);
		searchResult.setSearchQuery(searchQuery);
		
		return searchResult;
	}


	private Object getAutocompleteResult(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}


	private List<Reporter> getReporters(ReporterDAO reporterDao, String searchQuery, int start, int end) {
		List<Reporter> reporters = null;
		String type = null;
		
		// searchQuery의 검색 type설정
		type = Utility.isURL(searchQuery) ? "url" : "name";
		reporters = reporterDao.findReportersByType(type, searchQuery, start, end);

		return reporters;
	}

}
