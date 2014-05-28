package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dao.DummyData;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.dao.ReporterDAO;
import next.wildgoose.dto.Article;
import next.wildgoose.dto.NumberOfArticles;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.ReporterResult;
import next.wildgoose.dto.Result;
import next.wildgoose.dto.StatPoints;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReporterController implements BackController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReporterController.class.getName());

	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		Uri uri = new Uri(request);

		// id가 입력되지 않은 경우 처리
		if (uri.size() <= 1 || uri.check(1, "")) {
			result = new ReporterResult(request.getParameterMap());
			result.setMessage("parameter is missing.");
			return result;
		}
		
		int reporterId = Integer.parseInt(uri.get(1));
		if (uri.get(2) == null) {
			result = getReporterPage(request, reporterId);
		}
		else if (uri.check(2, "statistics")) {
			result = getGraphData(request, uri, reporterId);
		}

		return result;
	}

	private ReporterResult getGraphData(HttpServletRequest request, Uri uri, int reporterId) {
		
		ReporterResult reporterResult = new ReporterResult(request.getParameterMap());
		ServletContext context = request.getServletContext();
		
		String graph = request.getParameter("data");
		String by = request.getParameter("by");
		List<NumberOfArticles> numberOfArticlesList = null;
		
		if("number_of_articles".equals(graph)){
			NumberOfArticlesDAO numberOfArticlesDao = (NumberOfArticlesDAO) context.getAttribute("NumberOfArticlesDAO");
			if("day".equals(by)){
				reporterResult.setStatus(200);
				numberOfArticlesList = numberOfArticlesDao.findNumberOfArticlesByDay(reporterId);
				reporterResult.setNumberOfArticles(numberOfArticlesList);
			}
			else if ("section".equals(by)){
				reporterResult.setStatus(200);
				numberOfArticlesList = numberOfArticlesDao.findNumberOfArticlesBySection(reporterId);
				reporterResult.setNumberOfArticles(numberOfArticlesList);
			}
		}
		else if ("stat_points".equals(by)){
			DummyData dummy = (DummyData) context.getAttribute("DummyData");
			StatPoints statPoints = dummy.getStatPoints(reporterId);
			reporterResult.setStatus(200);
			reporterResult.setStatPoints(statPoints);
		}
		return reporterResult;
	}

	private ReporterResult getReporterPage(HttpServletRequest request, int reporterId) {
		ServletContext context = request.getServletContext();
		ReporterResult reporterResult = new ReporterResult(request.getParameterMap());

		Reporter reporter = null;
		List<Article> articles = null;
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		ArticleDAO articleDao = (ArticleDAO) context.getAttribute("ArticleDAO");

		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporter = reporterDao.findReporterById(reporterId);
		articles = articleDao.findArticlesById(reporterId);

		reporterResult.setReporter(reporter);
		reporterResult.setArticles(articles);
		reporterResult.setMessage("getting Reporter Info success");
		
		return reporterResult;
	}

}
