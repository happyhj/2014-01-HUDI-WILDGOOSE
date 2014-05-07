package next.wildgoose.service;

import java.util.List;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;
import next.wildgoose.utility.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetReporterCard implements Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetReporterCard.class.getName());
	private static GetReporterCard searchReporter;
	private ActionResult ar;
	
	private GetReporterCard() {
		
	}
	
	public static GetReporterCard getInstance() {
		if (searchReporter == null) {
			searchReporter = new GetReporterCard();
		}
		return searchReporter;
	}
	
	public ActionResult execute(Uri uri) {
		String searchQuery = uri.getParameter("q");
		boolean hasMoreCards = false;
		List<ReporterCard> reporterCards = null;
		
		setForwardingOption(searchQuery);
		reporterCards = this.getReporterCards(searchQuery, 0, Constants.NUM_OF_CARDS);
		if (reporterCards.size() > Constants.NUM_OF_CARDS) {
			hasMoreCards = true;
		}
		
		this.ar = new ActionResult();
		this.ar.setAttribute("totalNum", Constants.NUM_OF_CARDS);
		this.ar.setAttribute("hasMoreCards", hasMoreCards);
		this.ar.setAttribute("reporterCards", reporterCards);
		this.ar.setAttribute("searchQuery", searchQuery);
		
		return this.ar;
	}
	
	private void setForwardingOption(String searchQuery) {
		if (searchQuery == null) {
			this.ar.setForwardingOption(false, Constants.PAGE_SEARCH_REPORTER);
			return;
		}
		
		String trimmedQuery = searchQuery.trim();
		if ("".equals(trimmedQuery)) {
			this.ar.setForwardingOption(true, Constants.RESOURCE_ERROR);
		} else {
			this.ar.setForwardingOption(false, Constants.PAGE_SEARCH_REPORTER);
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
