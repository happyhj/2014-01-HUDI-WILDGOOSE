package next.wildgoose.service;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dto.Article;
import next.wildgoose.utility.Constants;

public class TimeLineService implements Action {

	public ActionResult execute(HttpServletRequest request) {
		List<Article> articleCards = null;
		ServletContext context = request.getServletContext();
		ActionResult ar = new ActionResult();
		HttpSession session = request.getSession();
		
		ArticleDAO articleCardDao =  (ArticleDAO) context.getAttribute("ArticleCardDAO");
		articleCards = articleCardDao.findArticlesByFavorite((String)session.getAttribute("userId"));
		
		request.setAttribute("articleCards", articleCards);
		
		ar.setForwardingOption(false, Constants.PAGE_TIMELINE);
		return ar;
	}

}
