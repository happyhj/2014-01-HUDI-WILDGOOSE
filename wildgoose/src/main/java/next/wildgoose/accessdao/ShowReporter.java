package next.wildgoose.accessdao;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ArticleCard;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.web.Wildgoose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowReporter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowReporter.class.getName());
	private HttpServletRequest request;
	private UriHandler uriHandler;
	
	public ShowReporter(HttpServletRequest request, UriHandler uriHandler) {
		this.request = request;
		this.uriHandler = uriHandler;
	}
	
	/*
	 * Action 인터페이스의 구현체, 상황에 맞는 View를 선택해서 ActionForward 객체로 반환
	 * (non-Javadoc)
	 * @see next.wildgoose.model.Action#execute()
	 */
	
	public ActionForward execute() {
		ReporterCard reporterCard = null;
		List<ArticleCard> articleCards = null;
		ServletContext context = request.getServletContext();
		ActionForward forward = new ActionForward();
		
		ReporterCardDAO reporterCardDao = (ReporterCardDAO) context.getAttribute("reporterCardDAO");
		ArticleCardDAO articleCardDao =  (ArticleCardDAO) context.getAttribute("articleCardDAO");
		
		// id가 입력되지 않은 경우 처리
		if (this.uriHandler.size() <= 1 || this.uriHandler.get(1).equals("")) {
			LOGGER.debug(this.uriHandler.toString());
			
			forward.setRedirect(true);
			forward.setPath(Wildgoose.RESOURCE_ERROR);
			return forward;
		}
		int reporterId = Integer.parseInt(this.uriHandler.get(1));
		
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
