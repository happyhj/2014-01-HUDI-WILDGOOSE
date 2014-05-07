package next.wildgoose.service;

import java.util.List;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;
import next.wildgoose.utility.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetReporterCards implements Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetReporterCards.class.getName());
	private static GetReporterCards searchReporter;
	
	public static GetReporterCards getInstance() {
		if (searchReporter == null) {
			searchReporter = new GetReporterCards();
		}
		return searchReporter;
	}
	
	public ActionResult execute(Uri uri) {
		ActionResult ar = new ActionResult();
		String searchQuery = uri.getParameter("q");
		LOGGER.debug("searchquery : " + searchQuery);
		boolean hasMoreCards = false;
		List<ReporterCard> reporterCards = null;
		
		setForwardingOption(ar, searchQuery);
		// 25개를 가져온 후, 마지막 카드를 지움.
		reporterCards = this.getReporterCards(searchQuery, 0, Constants.NUM_OF_CARDS + 1);
		if (reporterCards.size() > Constants.NUM_OF_CARDS) {
			hasMoreCards = true;
			reporterCards.remove(Constants.NUM_OF_CARDS);
		}

		ar.setAttribute("totalNum", Constants.NUM_OF_CARDS);
		ar.setAttribute("hasMoreCards", hasMoreCards);
		ar.setAttribute("reporterCards", reporterCards);
		ar.setAttribute("searchQuery", searchQuery);
		LOGGER.debug(""+hasMoreCards);
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
	
	private List<ReporterCard> getReporterCards(String searchQuery, int start, int end) {
		List<ReporterCard> reporterCards = null;
		ReporterCardDAO reporterCardDao = ReporterCardDAO.getInstance();

		if (Validation.isURL(searchQuery)) {
			// URL로 검색
			reporterCards = reporterCardDao.findReportersByURL(searchQuery, start, end);
		} else {
			// 이름으로 검색
			reporterCards = reporterCardDao.findReportersByName(searchQuery, start, end);
		}

		return reporterCards;
	}

}
