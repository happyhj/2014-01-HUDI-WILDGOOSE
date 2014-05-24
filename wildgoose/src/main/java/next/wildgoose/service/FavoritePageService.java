package next.wildgoose.service;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.FavoriteDAO;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.utility.Constants;

public class FavoritePageService implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request) {
		List<ReporterCard> reporters = null;
		ServletContext context = request.getServletContext();
		ActionResult ar = new ActionResult();
		HttpSession session = request.getSession();
		
		FavoriteDAO favoriteDao =  (FavoriteDAO) context.getAttribute("FavoriteDAO");
		reporters = favoriteDao.findReporterCard((String)session.getAttribute("userId"));
		
		request.setAttribute("reporters", reporters);
		
		ar.setForwardingOption(false, Constants.PAGE_FAVORITE_PAGE);
		return ar;
	}
}
