package next.wildgoose.web;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.model.ReporterCard;
import next.wildgoose.utility.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchReporterAction implements Action {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchReporterAction.class.getName());
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response, RestfulURI restful) throws Exception {
		ActionForward forward = new ActionForward();
		
		String searchQuery = null;
		List<ReporterCard> reporterCards = null;
		ReporterCardDAO reporterCardDao = null;
		
		// 첫 요청시, 'q'가 null인 경우
		searchQuery = request.getParameter("q");
		if (searchQuery == null) {
			forward.setPath(Wildgoose.PAGE_SEARCH_REPORTER);
			return forward;	
		}
		
		// query의 앞뒤 공백제거
		searchQuery = searchQuery.trim();
		
		// 유효하지 않은 'q'
		if (searchQuery.equals("")) {
			forward.setPath(Wildgoose.PAGE_ERROR);
			return forward;	
		}
		
		// DB에서 이름으로 검색하여 reporterCard 리스트 가져오기
		reporterCardDao = new ReporterCardDAO();
		try {
			reporterCards = reporterCardDao.findReportersByName(searchQuery.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("reporterCards", reporterCards);
		request.setAttribute("searchQuery", searchQuery);
		
		forward.setPath(Wildgoose.PAGE_SEARCH_REPORTER);
		return forward;	
		
	}

}
