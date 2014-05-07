package next.wildgoose.service;

import java.util.List;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ArticleCard;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.utility.Constants;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetArticleCard implements Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetArticleCard.class.getName());
	private static GetArticleCard showReporter;
	private ActionResult ar;
	
	private GetArticleCard() {
		
	}
	
	public static GetArticleCard getInstance() {
		if (showReporter == null) {
			showReporter = new GetArticleCard();
		}
		return showReporter;
	}
	
	public ActionResult execute(Uri uri) {
		ReporterCard reporterCard = null;
		List<ArticleCard> articleCards = null;
		
		ReporterCardDAO reporterCardDao = ReporterCardDAO.getInstance();
		ArticleCardDAO articleCardDao =  ArticleCardDAO.getInstance();
		
		// id가 입력되지 않은 경우 처리
		if (uri.size() <= 1 || uri.get(1).equals("")) {
			LOGGER.debug(uri.toString());
			this.ar.setForwardingOption(true, Constants.RESOURCE_ERROR);
			return this.ar;
		}
		
		int reporterId = Integer.parseInt(uri.get(1));
		
		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporterCard = reporterCardDao.findReporterById(reporterId);
		articleCards = articleCardDao.findArticlesById(reporterId);		
		
		this.ar.setAttribute("reporter_id", reporterId);
		this.ar.setAttribute("reporter", reporterCard);		
		this.ar.setAttribute("articles", articleCards);
		this.ar.setForwardingOption(false, Constants.PAGE_SHOW_REPORTER);
		return this.ar;	
	}
}
