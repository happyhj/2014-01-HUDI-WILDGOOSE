package next.wildgoose.service;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dto.ArticleCard;
import next.wildgoose.utility.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeLineService implements Action {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeLineService.class.getName());
	@Override
	public ActionResult execute(HttpServletRequest request) {
		List<ArticleCard> articleCards = null;
		ServletContext context = request.getServletContext();
		ActionResult ar = new ActionResult();
		HttpSession session = request.getSession();
		
		ArticleCardDAO articleCardDao =  (ArticleCardDAO) context.getAttribute("ArticleCardDAO");
		articleCards = articleCardDao.findArticlesByFavorite((String)session.getAttribute("userId"));
		
		request.setAttribute("articleCards", articleCards);
		
		ar.setForwardingOption(false, Constants.PAGE_TIMELINE);
		return ar;
	}

}
