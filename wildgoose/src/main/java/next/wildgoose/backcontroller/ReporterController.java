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
		ReporterResult reporterResult = null;
		Uri uri = new Uri(request);
		
		// id가 입력되지 않은 경우 처리
		if (uri.size() <= 1 || uri.check(1, "")) {
			reporterResult = new ReporterResult(request.getParameterMap());
			reporterResult.setStatus(500);
			reporterResult.setMessage("parameter is missing.");
			return reporterResult;
		}
		
		int reporterId = Integer.parseInt(uri.get(1));
		if (uri.check(2, "")) {
			reporterResult = getReporterPage(request, reporterId);
		} else if (uri.check(2, "statistics")) {
			reporterResult = getGraphData(request, reporterId);
		}
		
		
		LOGGER.debug("resultData: " + Utility.toJsonString(reporterResult));
		
		
		return reporterResult;
	}

	private ReporterResult getGraphData(HttpServletRequest request,
			int reporterId) {
		// TODO Auto-generated method stub
		return null;
	}

	private ReporterResult getReporterPage(HttpServletRequest request, int reporterId) {
		ServletContext context = request.getServletContext();
		ReporterResult reporterResult = new ReporterResult(request.getParameterMap());
		
		Reporter reporter = null;
		List<Article> articles = null;
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		ArticleDAO articleDao =  (ArticleDAO) context.getAttribute("ArticleDAO");

		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporter = reporterDao.findReporterById(reporterId);
		articles = articleDao.findArticlesById(reporterId);
		
		reporterResult.setReporter(reporter);
		reporterResult.setArticles(articles);
		reporterResult.setMessage("getting Reporter Info success");
		return reporterResult;
	}

}
