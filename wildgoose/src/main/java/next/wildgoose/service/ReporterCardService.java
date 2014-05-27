package next.wildgoose.service;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ReporterDAO;
import next.wildgoose.dto.Reporter;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Utility;

public class ReporterCardService implements Action {
	
	public ActionResult execute(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		ActionResult ar = new ActionResult();
		String searchQuery = request.getParameter("q");
		boolean hasMoreCards = false;
		List<Reporter> reporterCards = null;
		
		if ("%".equals(searchQuery)) {
			searchQuery = null;
		}

		setForwardingOption(ar, searchQuery);
		if (searchQuery != null) {
			// 25개를 가져온 후, 마지막 카드를 지움.
			ReporterDAO rcardDao = (ReporterDAO) context.getAttribute("ReporterCardDAO");
			reporterCards = getReporterCards(rcardDao, searchQuery, 0, Constants.NUM_OF_CARDS + 1);
			if (reporterCards.size() > Constants.NUM_OF_CARDS) {
				hasMoreCards = true;
				reporterCards.remove(Constants.NUM_OF_CARDS);
			}
		}
		request.setAttribute("totalNum", Constants.NUM_OF_CARDS);
		request.setAttribute("hasMoreCards", hasMoreCards);
		request.setAttribute("reporterCards", reporterCards);
		request.setAttribute("searchQuery", searchQuery);

		return ar;
	}
	
	private void setForwardingOption(ActionResult ar, String searchQuery) {
		if (searchQuery == null) {
			ar.setForwardingOption(false, Constants.PAGE_SEARCH_REPORTER);
			return;
		}
		
		String trimmedQuery = searchQuery.trim();
		if ("".equals(trimmedQuery)) {
			ar.setForwardingOption(true, Constants.RESOURCE_ERROR);
		} else {
			ar.setForwardingOption(false, Constants.PAGE_SEARCH_REPORTER);
		}
	}
	
	private List<Reporter> getReporterCards(ReporterDAO rcardDao, String searchQuery, int start, int end) {
		List<Reporter> reporterCards = null;
		String type = null;
		
		// searchQuery의 검색 type설정
		type = (Utility.isURL(searchQuery))? "url" : "name";
		reporterCards = rcardDao.findReportersByType(type, searchQuery, start, end);

		return reporterCards;
	}

}
