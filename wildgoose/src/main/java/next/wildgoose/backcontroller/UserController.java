package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dao.FavoriteDAO;
import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Article;
import next.wildgoose.dto.FavoriteResult;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.SimpleResult;
import next.wildgoose.dto.TimelineResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
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
		LOGGER.debug(uri.toString());
		
		if (isValidUserId(request, userId) == false) {
			result = new SimpleResult();
			result.setStatus(404);
			result.setMessage("존재하지 않는 유저입니다");
			return result;
		}
		
		HttpSession session = request.getSession();
		String visitor = (String) session.getAttribute("userId");
		if (visitor == null) {
			// 로그인 하도록 유도하기
			SimpleResult sResult = new SimpleResult();
			sResult.setStatus(401);
			sResult.setMessage("로그인이 필요합니다");
			sResult.setData("requestedUri", uri.toString());
			return sResult;
		}
		 
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
		} else if ("mypage".equals(pageName)){
			result = new SimpleResult(true, "me");
		}
		return result;
	}
	
	private boolean isValidUserId(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		if (signDao.findEmail(userId)) {
			return true;
		}
		return false;
	}

	private TimelineResult getTimeline(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();

		ArticleDAO articleDao =  (ArticleDAO) context.getAttribute("ArticleDAO");
		List<Article> articles = articleDao.findArticlesByFavorite(userId);
		
		TimelineResult timelineResult = new TimelineResult("timeline");
		timelineResult.setStatus(200);
		timelineResult.setMessage("OK");
		timelineResult.setArticles("articles", articles);
		return timelineResult;
	}
	
	private Result getFavorites(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		
		FavoriteDAO favoriteDao =  (FavoriteDAO) context.getAttribute("FavoriteDAO");
		List<Reporter> reporters = favoriteDao.findFavoriteReporters(userId);
		
		FavoriteResult favoriteResult = new FavoriteResult("favorite");
		favoriteResult.setStatus(200);
		favoriteResult.setMessage("success");
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
			simpleResult.setMessage("success");
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
			simpleResult.setMessage("success");
		}
		return simpleResult;
	}
	
	private String dollarSignToAtMark(String dollarSignedId) {
		String atMarkedId = null;
		if (dollarSignedId == null) {
			return null;
		}
		atMarkedId = dollarSignedId.replace("$", "@");
		return atMarkedId;
	}
}
