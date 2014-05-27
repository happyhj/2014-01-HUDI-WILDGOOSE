package next.wildgoose.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.dao.template.SelectJdbcTemplate;
import next.wildgoose.dto.NumberOfArticles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberOfArticlesDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfArticlesDAO.class.getName());
	
	public List<NumberOfArticles> findNumberOfArticlesByDay(final int reporterId) {
		SelectJdbcTemplate template = new SelectJdbcTemplate() {
			
			@Override
			protected List<NumberOfArticles> mapRow(ResultSet rs) throws SQLException {
				List<NumberOfArticles> result = new ArrayList<NumberOfArticles>();
				NumberOfArticles numOfArticle = null;
				while (rs.next()) {
					numOfArticle = new NumberOfArticles();
					numOfArticle.setCount(rs.getInt("count"));
					numOfArticle.setDate(rs.getString("date"));
					result.add(numOfArticle);
				}
				return result;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setInt(1, reporterId);
			}
			
		};
		
		StringBuilder query = new StringBuilder();
		query.append("select date_format(datetime, '%m/%d') as date, count(URL) as count from article ");
		query.append("WHERE URL in (Select article_URL as url from article_author where author_id = ?) ");
		query.append("and DATEDIFF(now(), article.datetime) < 7 ");
		query.append("group by date_format(datetime, '%m/%d');");
		
		return (List<NumberOfArticles>) template.select(query.toString());
	}
	
	public List<NumberOfArticles> findNumberOfArticlesBySection (final int reporterId) {
		SelectJdbcTemplate template = new SelectJdbcTemplate() {
			
			@Override
			protected List<NumberOfArticles> mapRow(ResultSet rs) throws SQLException {
				List<NumberOfArticles> result = new ArrayList<NumberOfArticles>();
				NumberOfArticles numOfArticle = null;
				while (rs.next()) {
					numOfArticle = new NumberOfArticles();
					numOfArticle.setLabel(rs.getString(1));
					numOfArticle.setValue(rs.getInt(2));
					result.add(numOfArticle);
				}
				return result;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setInt(1, reporterId);
			}
			
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT distinct sec.name, count(section_id) FROM article ");
		query.append("JOIN (SELECT author_id, article_URL FROM article_author as aa WHERE aa.author_id = ?) as result ");
		query.append("ON URL = result.article_URL ");
		query.append("JOIN section as sec ON section_id = sec.id ");
		query.append("GROUP BY section_id ORDER BY section_id LIMIT 5;");

		return (List<NumberOfArticles>) template.select(query.toString());
	}

//	public JSONObject getJson(int reporterId, HttpServletRequest request) {
//		String condition = request.getParameter("by");
//		
//		if ("section".equals(condition)) {
//			return findNumberOfArticlesBySection(reporterId);
//		} else if ("day".equals(condition)) {
//			return findNumberOfArticlesByDay(reporterId);
//		}
//		return null;
//	}
}
