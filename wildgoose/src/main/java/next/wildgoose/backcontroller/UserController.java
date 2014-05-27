package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dao.FavoriteDAO;
import next.wildgoose.dto.Article;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.SimpleResult;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController implements BackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());
	@Override
	// TODO: change to result
	public Object execute(HttpServletRequest request) {
		Object result = null;
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
	private Object getTimeline(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();

		List<Article> articles = null;
		
		ArticleDAO articleDao =  (ArticleDAO) context.getAttribute("ArticleDAO");
		articles = articleDao.findArticlesByFavorite(userId);
		
		return articles;
	}
	
	// TODO: change to result
	private Object getFavorites(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		List<Reporter> reporters = null;
		
		FavoriteDAO favoriteDao =  (FavoriteDAO) context.getAttribute("FavoriteDAO");
		reporters = favoriteDao.findReporterCards(userId);
		
		return reporters;
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
