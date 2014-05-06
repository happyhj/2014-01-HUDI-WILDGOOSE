package next.wildgoose.refactoring;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.utility.Validation;
import next.wildgoose.web.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchReporter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchReporter.class.getName());
	private HttpServletRequest request;
	private ActionForward af;

	// 궁극적으로 없어져야 할 생성자
	public SearchReporter(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setAttributeData() {
		boolean hasMoreCards = false;
		List<ReporterCard> reporterCards = null;
		String searchQuery = request.getParameter("q");
		
		setForwardPath(searchQuery);
		reporterCards = this.getReporterCards(searchQuery, 0, Wildgoose.NUM_OF_CARDS);
		if (reporterCards.size() > Wildgoose.NUM_OF_CARDS) {
			hasMoreCards = true;
		}
		
		this.request.setAttribute("totalNum", Constants.NUM_OF_CARDS);
		this.request.setAttribute("hasMoreCards", hasMoreCards);
		this.request.setAttribute("reporterCards", reporterCards);
		this.request.setAttribute("searchQuery", searchQuery);
	}
	
	public ActionForward getForwardPath() {
		return this.af;
	}
	
	private void setForwardPath(String searchQuery) {
		if (searchQuery == null) {
			this.af = new ActionForward(false, Constants.PAGE_SEARCH_REPORTER);
			return;
		}
		
		String trimmedQuery = searchQuery.trim();
		if ("".equals(trimmedQuery)) {
			this.af = new ActionForward(true, Constants.RESOURCE_ERROR);
		} else {
			this.af = new ActionForward(false, Constants.PAGE_SEARCH_REPORTER);
		}
	}
	
	private List<ReporterCard> getReporterCards(String searchQuery, int start, int end) {
		ServletContext context = this.request.getServletContext();
		List<ReporterCard> reporterCards = null;
		ReporterCardDAO reporterCardDao = (ReporterCardDAO) context.getAttribute("reporterCardDAO");

		// DB에서 검색하여 reporterCard 리스트 가져오기
		// URL로 검색
		if (Validation.isURL(searchQuery)) {
			reporterCards = reporterCardDao.findReportersByURL(searchQuery, start, end);
		} else {
			reporterCards = reporterCardDao.findReportersByName(searchQuery, start, end);
		}

		return reporterCards;
	}
}
