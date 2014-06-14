package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dao.FavoriteDAO;
import next.wildgoose.dao.ReporterDAO;
import next.wildgoose.dao.SignDAO;
import next.wildgoose.dto.Article;
import next.wildgoose.dto.MeResult;
import next.wildgoose.dto.Reporter;
import next.wildgoose.dto.SearchResult;
import next.wildgoose.dto.SimpleResult;
import next.wildgoose.framework.BackController;
import next.wildgoose.framework.Result;
import next.wildgoose.framework.utility.Uri;
import next.wildgoose.utility.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeController implements BackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MeController.class.getName());
	
	@Override
	public Result execute(HttpServletRequest request) {
		Result result = null;
		Uri uri = new Uri(request);
		
		String userId = uri.get(1);
		
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
		
		int startItem = (request.getParameter("start_item") != null)? Integer.parseInt(request.getParameter("start_item")) : 0;
		int howMany = (request.getParameter("how_many") != null)? Integer.parseInt(request.getParameter("how_many")) : Constants.NUM_OF_ARTICLES;
		LOGGER.debug("startItem: " + startItem + ", howMany: " + howMany);
		
		return getMe(request, userId, startItem, howMany);
	}
	
	private Result getMe(HttpServletRequest request, String userId, int start, int howMany) {
		ServletContext context = request.getServletContext();

		MeResult meResult = new MeResult("me");
		
		
		
		ArticleDAO articleDao =  (ArticleDAO) context.getAttribute("ArticleDAO");
		List<Article> articles = articleDao.findArticlesByFavorite(userId, start, howMany);
		int totalNum = articleDao.findNumberOfArticlesByFavorite(userId);
		
		FavoriteDAO favoriteDao =  (FavoriteDAO) context.getAttribute("FavoriteDAO");
		List<Reporter> reporters = favoriteDao.findFavoriteReporters(userId);
		
		ReporterDAO reporterDao = (ReporterDAO) context.getAttribute("ReporterDAO");
		List<Reporter> recommands = reporterDao.getRandomReporters(userId, 10);
		
		meResult.setStatus(200);
		meResult.setMessage("success");
		meResult.setTotalNum(totalNum);
		meResult.setArticles("articles", articles);
		meResult.setFavorites("reporters", reporters);
		meResult.setRecommands("recommands", recommands);

		return meResult;
	}

	private boolean isValidUserId(HttpServletRequest request, String userId) {
		ServletContext context = request.getServletContext();
		SignDAO signDao = (SignDAO) context.getAttribute("SignDAO");
		if (signDao.findEmail(userId)) {
			return true;
		}
		return false;
	}
}
