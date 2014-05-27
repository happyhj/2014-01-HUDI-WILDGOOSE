package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dao.ReporterDAO;
import next.wildgoose.dto.Article;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.ReporterResult;
import next.wildgoose.utility.Uri;
import next.wildgoose.utility.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReporterController implements BackController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterController.class.getName());
	
	@Override
	public Object execute(HttpServletRequest request) {
		
		Reporter reporter = null;
		List<Article> articles = null;
		ReporterResult reporterResult = new ReporterResult(request.getParameterMap());
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		ArticleDAO articleDao =  (ArticleDAO) context.getAttribute("ArticleDAO");
		
		// id가 입력되지 않은 경우 처리
		if (uri.size() <= 1 || uri.get(1).equals("")) {
			reporterResult.setStatus(500);
			reporterResult.setMessage("parameter is missing.");
			return reporterResult;
		}
		
		int reporterId = Integer.parseInt(uri.get(1));
		
		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporter = reporterDao.findReporterById(reporterId);
		articles = articleDao.findArticlesById(reporterId);
		
		reporterResult.setReporter(reporter);
		reporterResult.setArticles(articles);
		reporterResult.setMessage("getting Reporter Info success");
		
		
		LOGGER.debug("resultData: " + Utility.toJsonString(reporterResult));
		
		return reporterResult;
	}

}
