package next.wildgoose.backcontroller;

import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dao.DummyData;
import next.wildgoose.dao.NumberOfArticlesDAO;
import next.wildgoose.dao.ReporterDAO;
import next.wildgoose.dto.Article;
import next.wildgoose.dto.NumberOfArticles;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.ReporterResult;
import next.wildgoose.dto.StatPoints;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.utility.Uri;

public class ReporterController implements BackController {

	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		Uri uri = new Uri(request);

		// id가 입력되지 않은 경우 처리
		if (uri.size() <= 1 || uri.check(1, "")) {
			result = new ReporterResult();
			result.setMessage("parameter is missing.");
			return result;
		}
		if (request.getParameter("method") != null) {
			int max = Integer.parseInt(request.getParameter("max"));
			result = getRandomReporters(request, max);
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

	private Result getRandomReporters(HttpServletRequest request, int reportersNum) {
		ServletContext context = request.getServletContext();
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		int howmany = Math.min(reportersNum, 20);
		
		ReporterResult reporterResult = new ReporterResult();
		if (userId == null) {
			reporterResult.setStatus("401");
			reporterResult.setMessage("로그인되지 않았습니다");
		} else {
			reporterResult.setStatus("200");
			reporterResult.setMessage("OK");
			List<Reporter> totalReporters = reporterDao.getRandomReporters(userId, howmany);
			reporterResult.setReporters(totalReporters);
		}
		
		return null;
	}
	
	private int[] getRandomIds(int num, int max) {
		Random r = new Random();
		int[] reporterIds = new int[num];
		
		for (int i = 0; i < num; i++) {
			int reporter = r.nextInt(max);
			reporterIds[i] = reporter;
		}
		
		return null;
	}

	private ReporterResult getGraphData(HttpServletRequest request, Uri uri, int reporterId) {
		
		ReporterResult reporterResult = new ReporterResult();
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
		ReporterResult reporterResult = new ReporterResult();

		Reporter reporter = null;
		List<Article> articles = null;
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		ArticleDAO articleDao = (ArticleDAO) context.getAttribute("ArticleDAO");

		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporter = reporterDao.findReporterById(reporterId);
		articles = articleDao.findArticlesById(reporterId);

		reporterResult.setReporter(reporter);
		reporterResult.setArticles(articles);
		reporterResult.setStatus(200);
		reporterResult.setMessage("getting Reporter Info success");
		
		return reporterResult;
	}

}
