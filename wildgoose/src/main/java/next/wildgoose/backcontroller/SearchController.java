package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ReporterDAO;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.SearchResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchController implements BackController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class.getName());
	
	@Override
	public Result execute(HttpServletRequest request) {
		String searchQuery = request.getParameter("q");
		boolean autoComplete = (request.getParameter("autocomplete") != null)? Boolean.parseBoolean(request.getParameter("autocomplete")) : false;
		int howMany = (request.getParameter("how_many") != null)? Integer.parseInt(request.getParameter("how_many")) : Constants.NUM_OF_CARDS;
//		int startPage = (request.getParameter("start_page") != null)? Integer.parseInt(request.getParameter("start_page")) : -1;
		int startItem = (request.getParameter("start_item") != null)? Integer.parseInt(request.getParameter("start_item")) : -1;
		SearchResult searchResult = checkQuery(request, searchQuery);
		
		
		// 결과 반환
		// 에러 혹은 root인 경우 반환
		if (searchResult != null) {
			return searchResult;
		}
		// 자동완성 반환
		if (autoComplete) {
			LOGGER.debug("searchQuery: " + searchQuery + ", autocompete: " + request.getParameter("autocomplete"));
			return getAutoCompleteResult(request, searchQuery, howMany);
		}
		
		// 결과를 특정 부분부터 반환
		if (startItem != -1) {
			return getSearchResult(request, searchQuery, startItem, howMany);
		}
		
		// 결과를 처음부터 반환
		return getSearchResult(request, searchQuery, howMany);
	}
	
	
	private SearchResult getAutoCompleteResult(HttpServletRequest request, String searchQuery, int howMany) {
		SearchResult searchResult = new SearchResult();
		ServletContext context = request.getServletContext();
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		
		List<Reporter> reporters = reporterDao.getSimilarNames(searchQuery, howMany);
		
		searchResult.setStatus(200);
		searchResult.setMessage("getting similar names result success");
		searchResult.setReporters(reporters);
		searchResult.setSearchQuery(searchQuery);
		
		return searchResult;
		
	}
	
	private SearchResult getSearchResult (HttpServletRequest request, String searchQuery, int start, int howMany) {
		SearchResult searchResult = new SearchResult();
		ServletContext context = request.getServletContext();
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		List<Reporter> reporters = null;
		
		if (start == 0) {
			int numOfResult = getNumOfResult(reporterDao, searchQuery);
			searchResult.setTotalNum(numOfResult);
		}
		
		reporters = getReporters(reporterDao, searchQuery, start, howMany);
		searchResult.setStatus(200);
		searchResult.setMessage("getting search result success");
		searchResult.setReporters(reporters);
		searchResult.setSearchQuery(searchQuery);
		
		return searchResult;
	}

	private SearchResult getSearchResult(HttpServletRequest request, String searchQuery, int howMany) {
		return getSearchResult(request, searchQuery, 0, howMany);
	}
	
	
	private int getNumOfResult(ReporterDAO reporterDao, String searchQuery) {
		String type = null;
		
		// searchQuery의 검색 type설정
		type = Utility.isURL(searchQuery) ? "url" : "name";

		return reporterDao.findNumberOfReportersByType(type, searchQuery);
	}
	

	private List<Reporter> getReporters(ReporterDAO reporterDao, String searchQuery, int start, int howMany) {
		String type = null;
		
		// searchQuery의 검색 type설정
		type = Utility.isURL(searchQuery) ? "url" : "name";

		return reporterDao.findReportersByType(type, searchQuery, start, howMany);
	}
	
	private SearchResult checkQuery(HttpServletRequest request, String searchQuery) {
		SearchResult searchResult = null;
		
		if (searchQuery == null) {
			searchResult = new SearchResult();
			searchResult.setStatus(200);
			searchResult.setMessage("welcome to search page! This path is not provided as API.");
			return searchResult;
		}
				
		// searchQuery 에러 검사
		searchQuery.replaceAll("%", "");
		String trimmedQuery = searchQuery.trim();
		if ("".equals(trimmedQuery)) {
			searchResult = new SearchResult();
			searchResult.setMessage("You can not search with whitespace");
			return searchResult;
		}
		return null;
	}
}
