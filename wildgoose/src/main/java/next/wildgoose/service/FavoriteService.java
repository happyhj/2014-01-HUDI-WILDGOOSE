package next.wildgoose.service;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.ArticleDAO;
import next.wildgoose.dto.Article;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FavoriteService implements Daction {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteService.class.getName());

	public DactionResult execute(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("userId");
		LOGGER.debug("email: " + email);
		
		ServletContext context = request.getServletContext();
		ArticleDAO articleDao = (ArticleDAO) context.getAttribute("ArticleCardDAO");
		
		List<Article> articles = articleDao.findArticlesByFavorite(email);
		
		Iterator<Article> ir = articles.iterator();
		Article article = null;
		JSONObject json = new JSONObject();
		while (ir.hasNext()) {
			JSONObject sub = new JSONObject();
			
			article = ir.next();
			sub.put("name", article.getName());
			sub.put("title", article.getTitle());
			sub.put("content", article.getContent());
			sub.put("datetime", article.getDatetime());
			sub.put("url", article.getUrl());
			
			json.append("data",sub);
		}
		DactionResult result = new DactionResult("json", json);
		return result;
		
	}

}
