package next.wildgoose.backcontroller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.FavoriteDAO;
import next.wildgoose.dto.ArticleCard;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController implements BackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());
	@Override
	public Object execute(HttpServletRequest request) {
		Uri uri = new Uri(request);
		String userId = uri.get(1);
		String pageName = uri.get(2);
		ServletContext context = request.getServletContext();

		if ("timeline".equals(pageName)) {
			return getTimeline(context, userId);
		} else if ("favorites".equals(pageName)) {
			return getFavorites(context, userId);
		}
		return null;
	}
	
	private Object getTimeline(ServletContext context, String userId) {
		List<ArticleCard> articleCards = null;
		
		ArticleCardDAO articleCardDao =  (ArticleCardDAO) context.getAttribute("ArticleCardDAO");
		articleCards = articleCardDao.findArticlesByFavorite(userId);
		
		return articleCards;
	}
	private Object getFavorites(ServletContext context, String userId) {
		List<ReporterCard> reporterCards = null;
		
		FavoriteDAO favoriteDao =  (FavoriteDAO) context.getAttribute("FavoriteDAO");
		reporterCards = favoriteDao.findReporterCard(userId);
		
		return reporterCards;
	}
}
