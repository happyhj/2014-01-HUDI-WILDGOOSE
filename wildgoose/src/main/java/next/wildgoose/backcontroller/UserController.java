package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dao.FavoriteDAO;
import next.wildgoose.dto.Article;
import next.wildgoose.dto.FavoriteResult;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.Result;
import next.wildgoose.dto.SimpleResult;
import next.wildgoose.dto.TimelineResult;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController implements BackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());
	
	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		Uri uri = new Uri(request);
		String userId = uri.get(1);
		String pageName = uri.get(2);
		String method = request.getMethod();
		
		if ("timeline".equals(pageName)) {
			result = getTimeline(request, userId);
		} else if ("favorites".equals(pageName)) {
			if ("GET".equals(method)) {
				result = getFavorites(request, userId);
			} else if ("POST".equals(method)) {
				result = addFavorites(request, userId);
			} else if ("DELETE".equals(method)) {
				result = removeFavorites(request, userId);
			}
		}
		return result;
	}
	
	// TODO: change to result
	private TimelineResult getTimeline(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();

		ArticleDAO articleDao =  (ArticleDAO) context.getAttribute("ArticleDAO");
		List<Article> articles = articleDao.findArticlesByFavorite(userId);
		
		TimelineResult timelineResult = new TimelineResult(request.getParameterMap());
		timelineResult.setArticles("articles", articles);
		return timelineResult;
	}
	
	// TODO: change to result
	private Result getFavorites(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		
		FavoriteDAO favoriteDao =  (FavoriteDAO) context.getAttribute("FavoriteDAO");
		List<Reporter> reporters = favoriteDao.findReporter(userId);
		
		FavoriteResult favoriteResult = new FavoriteResult(request.getParameterMap());
		favoriteResult.setFavorites("reporters", reporters);
		return favoriteResult;
	}
	
	private SimpleResult addFavorites(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		FavoriteDAO favDao = (FavoriteDAO) context.getAttribute("FavoriteDAO");
		SimpleResult simpleResult = new SimpleResult(request.getParameterMap());
		
		int reporterId = Integer.parseInt(request.getParameter("reporterId"));
		if (favDao.addFavorite(reporterId, userId)) {
			simpleResult.setStatus(200);
			simpleResult.setMessage("success");
		}
		return simpleResult;
	}

	private SimpleResult removeFavorites(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		FavoriteDAO favDao = (FavoriteDAO) context.getAttribute("FavoriteDAO");
		SimpleResult simpleResult = new SimpleResult(request.getParameterMap());
		
		int reporterId = Integer.parseInt(request.getParameter("reporterId"));
		if (favDao.removeFavorite(reporterId, userId)) {
			simpleResult.setStatus(200);
			simpleResult.setMessage("success");
		}
		return simpleResult;
	}
}
