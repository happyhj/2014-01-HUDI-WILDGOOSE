package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ArticleCard;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.dto.ReporterResult;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReporterController implements BackController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterController.class.getName());
	
	@Override
	public Object execute(HttpServletRequest request) {
		
		ReporterCard reporterCard = null;
		List<ArticleCard> articleCards = null;
		ReporterResult reporterResult = new ReporterResult(request.getParameterMap());
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		
		ReporterCardDAO reporterCardDao = (ReporterCardDAO) context.getAttribute("ReporterCardDAO");
		ArticleCardDAO articleCardDao =  (ArticleCardDAO) context.getAttribute("ArticleCardDAO");
		
		// id가 입력되지 않은 경우 처리
		if (uri.size() <= 1 || uri.get(1).equals("")) {
			reporterResult.setStatus(500);
			reporterResult.setMessage("parameter is missing.");		
			return reporterResult;
		}
		
		int reporterId = Integer.parseInt(uri.get(1));
		
		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporterCard = reporterCardDao.findReporterById(reporterId);
		articleCards = articleCardDao.findArticlesById(reporterId);
		
		reporterResult.setReporterCard(reporterCard);
		reporterResult.setArticleCards(articleCards);
		reporterResult.setMessage("getting Reporter Info success");
	
		
		return reporterResult;
	}

}
