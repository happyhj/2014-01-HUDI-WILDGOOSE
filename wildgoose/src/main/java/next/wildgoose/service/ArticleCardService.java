package next.wildgoose.service;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ArticleCard;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleCardService implements Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleCardService.class.getName());

	public ActionResult execute(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		ActionResult ar = new ActionResult();;
		ReporterCard reporterCard = null;
		List<ArticleCard> articleCards = null;
		
		ReporterCardDAO reporterCardDao = (ReporterCardDAO) context.getAttribute("ReporterCardDAO");
		ArticleCardDAO articleCardDao =  (ArticleCardDAO) context.getAttribute("ArticleCardDAO");
		
		// id가 입력되지 않은 경우 처리
		if (uri.size() <= 1 || uri.get(1).equals("")) {
			LOGGER.debug(uri.toString());
			ar.setForwardingOption(true, Constants.RESOURCE_ERROR);
			return ar;
		}
		
		int reporterId = Integer.parseInt(uri.get(1));
		
		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporterCard = reporterCardDao.findReporterById(reporterId);
		articleCards = articleCardDao.findArticlesById(reporterId);		
		
		request.setAttribute("reporter_id", reporterId);
		request.setAttribute("reporter", reporterCard);		
		request.setAttribute("articles", articleCards);
		
		ar.setForwardingOption(false, Constants.PAGE_SHOW_REPORTER);
		return ar;	
	}
}
