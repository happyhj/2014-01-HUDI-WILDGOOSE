package next.wildgoose.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.model.Action;
import next.wildgoose.model.ActionForward;
import next.wildgoose.model.DaoManager;
import next.wildgoose.model.JsonConverter;
import next.wildgoose.model.ReporterCard;
import next.wildgoose.utility.Utility;
import next.wildgoose.utility.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class SearchReporter extends DaoManager implements Action, JsonConverter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchReporter.class.getName());

	public SearchReporter(HttpServletRequest request) {
		super(request);
	}


	/*
	 *  특정 DAO에 접근하여 SearchReporter에 적합한 데이터 반환
	 */
	private List<ReporterCard> getCards(String searchQuery) {
		LOGGER.debug("valid search: " + searchQuery);

		List<ReporterCard> reporterCards = null;
		ReporterCardDAO reporterCardDao = (ReporterCardDAO) context.getAttribute("reporterCardDAO");

		// DB에서 검색하여 reporterCard 리스트 가져오기
		// URL로 검색
		if (Utility.isURL(searchQuery)) {
			reporterCards = reporterCardDao.findReportersByURL(searchQuery);
		}
		// 이름 검색
		else {
			reporterCards = reporterCardDao.findReportersByName(searchQuery);
		}

		return reporterCards;
	}

	/*
	 *  Action 인터페이스의 구현체, 상황에 맞는 View를 선택해서 ActionForward 객체로 반환
	 *  (non-Javadoc)
	 * @see next.wildgoose.model.Action#execute()
	 */
	@Override
	public ActionForward execute() throws Exception {
		List<ReporterCard> reporterCards = null;
		String searchQuery = null;
		boolean hasMoreCards = false;

		ActionForward forward = new ActionForward();


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
			LOGGER.debug("invalid search: " + searchQuery);

			forward.setRedirect(true);
			forward.setPath(Wildgoose.RESOURCE_ERROR);
			return forward;	
		}

		// 자료 가져오기
		reporterCards = this.getCards(searchQuery);
		
		// request 객체에 attribute 설정하기
		super.request.setAttribute("hasMoreCards", !hasMoreCards);
		super.request.setAttribute("reporterCards", reporterCards);
		super.request.setAttribute("searchQuery", searchQuery);

		forward.setPath(Wildgoose.PAGE_SEARCH_REPORTER);
		return forward;	
	}

	/* 
	 * JsonString으로 반환
	 * (non-Javadoc)
	 * @see next.wildgoose.model.JsonConverter#toJsonString()
	 */
	@Override
	public String toJsonString() throws Exception {
		
		Gson gson = new Gson();
		List<ReporterCard> reporterCards = null;
		String searchQuery = super.request.getParameter("q");
		
		// 자료 가져오기
		reporterCards = this.getCards(searchQuery);
		
		return gson.toJson(reporterCards).toString();
	}
}