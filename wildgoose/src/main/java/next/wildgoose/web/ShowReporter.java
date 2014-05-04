package next.wildgoose.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.model.Action;
import next.wildgoose.model.ActionForward;
import next.wildgoose.model.ArticleCard;
import next.wildgoose.model.DaoManager;
import next.wildgoose.model.ReporterCard;
import next.wildgoose.utility.UriHandler;
import next.wildgoose.utility.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowReporter extends DaoManager implements Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowReporter.class.getName());
	
	protected ShowReporter(HttpServletRequest request, HttpServletResponse response, UriHandler uriHandler) {
		super(request, response, uriHandler);
	}
	
	/*
	 * Action 인터페이스의 구현체, 상황에 맞는 View를 선택해서 ActionForward 객체로 반환
	 * (non-Javadoc)
	 * @see next.wildgoose.model.Action#execute()
	 */
	
	@Override
	public ActionForward execute() {
		ReporterCard reporterCard = null;
		List<ArticleCard> articleCards = null;
		
		ActionForward forward = new ActionForward();
		
		ReporterCardDAO reporterCardDao = (ReporterCardDAO) context.getAttribute("reporterCardDAO");
		ArticleCardDAO articleCardDao =  (ArticleCardDAO) context.getAttribute("articleCardDAO");
		
		// id가 입력되지 않은 경우 처리
		if (super.uriHandler.size() <= 1 || super.uriHandler.get(1).equals("")) {
			LOGGER.debug(super.uriHandler.toString());
			
			forward.setRedirect(true);
			forward.setPath(Wildgoose.RESOURCE_ERROR);
			return forward;
		}
		int reporterId = Integer.parseInt(super.uriHandler.get(1));
		
		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporterCard = reporterCardDao.findReporterById(reporterId);
		articleCards = articleCardDao.findArticlesById(reporterId);		
		
		request.setAttribute("reporter_id", reporterId);
		request.setAttribute("reporter", reporterCard);		
		request.setAttribute("articles", articleCards);
		
		forward.setPath(Wildgoose.PAGE_SHOW_REPORTER);
		return forward;
		
	}

}
