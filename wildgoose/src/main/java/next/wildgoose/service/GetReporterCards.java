package next.wildgoose.service;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetReporterCards implements Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetReporterCards.class.getName());
	
	public ActionResult execute(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		ActionResult ar = new ActionResult();
		String searchQuery = request.getParameter("q");
		boolean hasMoreCards = false;
		List<ReporterCard> reporterCards = null;
		
		LOGGER.debug("searchquery : " + searchQuery);
		
		setForwardingOption(ar, searchQuery);
		// 25개를 가져온 후, 마지막 카드를 지움.
		ReporterCardDAO rcardDao = (ReporterCardDAO) context.getAttribute("ReporterCardDAO");
		reporterCards = getReporterCards(rcardDao, searchQuery, 0, Constants.NUM_OF_CARDS + 1);
		if (reporterCards.size() > Constants.NUM_OF_CARDS) {
			hasMoreCards = true;
			reporterCards.remove(Constants.NUM_OF_CARDS);
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
	
	private List<ReporterCard> getReporterCards(ReporterCardDAO rcardDao, String searchQuery, int start, int end) {
		List<ReporterCard> reporterCards = null;

		if (Validation.isURL(searchQuery)) {
			// URL로 검색
			reporterCards = rcardDao.findReportersByURL(searchQuery, start, end);
		} else {
			// 이름으로 검색
			reporterCards = rcardDao.findReportersByName(searchQuery, start, end);
		}

		return reporterCards;
	}

}
