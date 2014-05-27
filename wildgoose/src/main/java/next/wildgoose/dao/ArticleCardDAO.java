package next.wildgoose.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.dao.template.SelectJdbcTemplate;
import next.wildgoose.dto.ArticleCard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleCardDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleCardDAO.class.getName());
	
	public List<ArticleCard> findArticlesById(final int reporterId) {
		
		SelectJdbcTemplate template = new SelectJdbcTemplate(){

			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				List<ArticleCard> articles = new ArrayList<ArticleCard>();
				ArticleCard article = null;
				while (rs.next()) {
					article = new ArticleCard();
					article.setUrl(rs.getString("url"));
					article.setTitle(rs.getString("title"));
					article.setSectionId(rs.getInt("section"));
					article.setContent(rs.getString("content"));
					article.setDatetime(rs.getTimestamp("datetime").toString());
					articles.add(article);
				}
				return articles;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setInt(1, reporterId);
			}
			
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT article.URL as url, article.title as title, ");
		query.append("article.section_id as section, article.content as content, article.datetime as datetime ");
		query.append("FROM article_author JOIN article ON article.URL = article_author.article_URL ");
		query.append("WHERE article_author.author_id = ? ORDER BY datetime DESC limit 5;");
		
		List<ArticleCard> articles = (List<ArticleCard>) template.select(query.toString());
		
		return articles;
	}
	
	public List<ArticleCard> findArticlesByFavorite(final String email) {
		SelectJdbcTemplate template = new SelectJdbcTemplate() {

			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				List<ArticleCard> articles = new ArrayList<ArticleCard>();
				ArticleCard article = null;
				
				while (rs.next()) {
					article = new ArticleCard();
					article.setUrl(rs.getString("URL"));
					article.setTitle(rs.getString("title"));
					article.setAuthorId(rs.getInt("id"));
					article.setName(rs.getString("name"));
					article.setContent(rs.getString("content"));
					article.setDatetime(rs.getTimestamp("datetime").toString());
					articles.add(article);
				}
				
				return articles;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
			}
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT author.name, author.id, favorite.* from author JOIN ");
		query.append("(SELECT * FROM article JOIN article_author ON article_author.article_URL = article.URL ");
		query.append("WHERE article_author.author_id IN ");
		query.append("(SELECT author_id FROM favorite WHERE user_email = ?) ");
		query.append("ORDER BY article.datetime desc limit 24) AS favorite ON author.id = favorite.author_id;");
		
		
		List<ArticleCard> articles = (List<ArticleCard>) template.select(query.toString());
		
		return articles;
	}
}
