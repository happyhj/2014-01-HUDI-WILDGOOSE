package next.wildgoose.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.model.ArticleCard;
import next.wildgoose.model.DatabaseConnector;

public class ArticleCardDAO {
	
	static Logger logger = LoggerFactory.getLogger(ArticleCardDAO.class.getName());

	ResultSet rs = null;
	String mysqlQuery = null;
	ArticleCard articleCard = null;
	List<ArticleCard> articleCards = null;
	
	public List<ArticleCard> findArticlesById(int reporterId) {
		String mysqlQuery = "SELECT article.URL as url, article.title as title, "
				+ "article.section_id as section, article.content as content, article.datetime as datetime "
				+ "FROM article_author JOIN article ON article.URL = article_author.article_URL "
				+ "WHERE article_author.author_id = " + reporterId + ";";
		
		try {
			rs = DatabaseConnector.select(mysqlQuery);
			articleCards = new ArrayList<ArticleCard>();
			
			while (rs.next()) {
				articleCard = new ArticleCard();
				articleCard.setUrl(rs.getString("url"));
				articleCard.setTitle(rs.getString("title"));
				articleCard.setSectionId(rs.getInt("section"));
				articleCard.setContent(rs.getString("content"));
				articleCard.setDatetime(rs.getTimestamp("datetime").toString());
				articleCards.add(articleCard);
			}
		}
		catch (SQLException sqle) {
			logger.debug(sqle.getMessage(),sqle);	
		}
		
		return articleCards;
	}
}
