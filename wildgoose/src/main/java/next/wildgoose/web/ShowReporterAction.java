package next.wildgoose.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.model.ArticleCard;
import next.wildgoose.model.ReporterCard;
import next.wildgoose.utility.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowReporterAction implements Action {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowReporterAction.class.getName());
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response, RestfulURI restful) throws Exception {
		
		ActionForward forward = new ActionForward();
		
		ReporterCard reporterCardData = null;
		List<ArticleCard> articleCards = null;
		ReporterCardDAO reporterCardDao = null;
		ArticleCardDAO articleCardDao = null;
		
		
		// id가 입력되지 않은 경우 처리
		if (restful.size() <= 1 || restful.get(1).equals("")) {
			forward.setPath(Wildgoose.PAGE_ERROR);
			return forward;
		}
		
		int reporterId = Integer.parseInt(restful.get(1));
		
		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporterCardDao = new ReporterCardDAO();
		reporterCardData = reporterCardDao.findReporterById(reporterId);
		
		
		// DB에서 id으로 검색하여 reporter의 최신 기사 리스트 가져오기
		articleCardDao = new ArticleCardDAO();
		articleCards = articleCardDao.findArticlesById(reporterId);
		
		request.setAttribute("reporter", reporterCardData);		
		request.setAttribute("articles", articleCards);
		
		forward.setPath(Wildgoose.PAGE_SHOW_REPORTER);
		LOGGER.debug("in hrer");
		return forward;
		
	}

}
