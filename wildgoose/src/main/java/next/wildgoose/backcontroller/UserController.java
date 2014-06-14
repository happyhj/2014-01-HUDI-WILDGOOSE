package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.FavoriteDAO;
import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.result.FavoriteResult;
import next.wildgoose.dto.result.SimpleResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.framework.utility.Uri;
import next.wildgoose.utility.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController extends AuthController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());
	
	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		Uri uri = new Uri(request);
		String userId = uri.get(1);
		String pageName = uri.get(2);
		String method = request.getMethod();
		LOGGER.debug(uri.toString());
		
		result = authenticate(request, userId);
		if (result != null) {
			return result;
		}
		 
		if ("favorites".equals(pageName)) {
			if ("GET".equals(method)) {
				if (uri.check(3, null)) {
					result = getFavorites(request, userId);
				} else {
					int reporterId = Integer.parseInt(uri.get(3));
					result = isFavorite(request, userId, reporterId);
				}
			} else if ("POST".equals(method)) {
				result = addFavorites(request, userId);
			} else if ("DELETE".equals(method)) {
				result = removeFavorites(request, userId);
			}
		}
		result.setPageName("me");
		return result;
	}
	
	private Result isFavorite(HttpServletRequest request, String userId,
			int reporterId) {
		ServletContext context = request.getServletContext();
		FavoriteDAO favoriteDao =  (FavoriteDAO) context.getAttribute("FavoriteDAO");
		SimpleResult result = new SimpleResult(true);
		result.setData("bool", favoriteDao.isFavorite(userId, reporterId));
		return result;
	}
	
	private Result getFavorites(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		
		FavoriteDAO favoriteDao =  (FavoriteDAO) context.getAttribute("FavoriteDAO");
		List<Reporter> reporters = favoriteDao.findFavoriteReporters(userId);
		
		FavoriteResult favoriteResult = new FavoriteResult();
		favoriteResult.setStatus(200);
		favoriteResult.setMessage("OK");
		LOGGER.debug(""+reporters.size());
		favoriteResult.setFavorites("reporters", reporters);
		return favoriteResult;
	}
	
	private SimpleResult addFavorites(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		FavoriteDAO favDao = (FavoriteDAO) context.getAttribute("FavoriteDAO");
		SimpleResult simpleResult = new SimpleResult();
		
		int reporterId = Integer.parseInt(request.getParameter("reporter_id"));
		if (favDao.addFavorite(reporterId, userId)) {
			simpleResult.setStatus(200);
			simpleResult.setMessage("OK");
		}
		return simpleResult;
	}

	private SimpleResult removeFavorites(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		FavoriteDAO favDao = (FavoriteDAO) context.getAttribute("FavoriteDAO");
		SimpleResult simpleResult = new SimpleResult();
		
		int reporterId = Integer.parseInt(request.getParameter("reporter_id"));
		if (favDao.removeFavorite(reporterId, userId)) {
			simpleResult.setStatus(200);
			simpleResult.setMessage("OK");
		}
		return simpleResult;
	}
}
