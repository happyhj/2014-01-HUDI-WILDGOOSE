package next.wildgoose.web;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.action.Action;
import next.wildgoose.action.ActionForward;
import next.wildgoose.dao.ReporterCardDAO;
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
	private List<ReporterCard> getCards(String searchQuery, int start, int end) {
		LOGGER.debug("valid search: " + searchQuery);

		List<ReporterCard> reporterCards = null;
		ReporterCardDAO reporterCardDao = (ReporterCardDAO) context.getAttribute("reporterCardDAO");

		// DB에서 검색하여 reporterCard 리스트 가져오기
		// URL로 검색
		if (isURL(searchQuery)) {
			reporterCards = reporterCardDao.findReportersByURL(searchQuery, start, end);
		} else {
			reporterCards = reporterCardDao.findReportersByName(searchQuery, start, end);
		}

		return reporterCards;
	}
	
	public static boolean isURL(String URL) {

		String regex = "(?i)^(http://)?(www.)?[a-z0-9-_]+.[a-z]{2,3}(.[a-z]{2,3})?.*";

		return Pattern.matches(regex, URL);
	}
	
	/*
	 *  Action 인터페이스의 구현체, 상황에 맞는 View를 선택해서 ActionForward 객체로 반환
	 *  (non-Javadoc)
	 * @see next.wildgoose.model.Action#execute()
	 */
	@Override
	public ActionForward execute() {
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
		reporterCards = this.getCards(searchQuery, 0, Wildgoose.NUM_OF_CARDS + 1);
		
		// 더보기 버튼 보여줄지 여부 확
		if (reporterCards.size() >= Wildgoose.NUM_OF_CARDS + 1) {
			hasMoreCards = true;
			reporterCards.remove(Wildgoose.NUM_OF_CARDS);
		}
		
		// request 객체에 attribute 설정하기
		super.request.setAttribute("totalNum", Wildgoose.NUM_OF_CARDS);
		super.request.setAttribute("hasMoreCards", hasMoreCards);
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
		
		int hasMoreCards = 0;
		List<ReporterCard> reporterCards = null;
		
		String searchQuery = super.request.getParameter("q");
		int lastNum = Integer.parseInt(super.request.getParameter("last"));
		int requestNum = Integer.parseInt(super.request.getParameter("req"));
		
		LOGGER.debug("lastNum: " + lastNum);
		LOGGER.debug("requestNum: " + requestNum);
		
		// 자료 가져오기
		reporterCards = this.getCards(searchQuery, lastNum, requestNum + 1);
		
		// 더보기 버튼 보여줄지 여부 확인
		if (reporterCards.size() >= Wildgoose.NUM_OF_CARDS + 1) {
			hasMoreCards = 1;
			reporterCards.remove(Wildgoose.NUM_OF_CARDS);
		}
		
		return hasMoreCards + gson.toJson(reporterCards).toString();
	}
}