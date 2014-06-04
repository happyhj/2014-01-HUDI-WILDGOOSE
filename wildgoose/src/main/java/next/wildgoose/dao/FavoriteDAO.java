package next.wildgoose.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.dao.template.JdbcTemplate;
import next.wildgoose.dao.template.PreparedStatementSetter;
import next.wildgoose.dao.template.RowMapper;
import next.wildgoose.dto.Reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FavoriteDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteDAO.class.getName());
	
	public boolean addFavorite(final int reporterId, final String email) {
		JdbcTemplate t = new JdbcTemplate();
		PreparedStatementSetter pss = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				LOGGER.debug("email: " + email + ",  reporterId: " + reporterId);
				psmt.setString(1, email);
				psmt.setInt(2, reporterId);
			}
		};
		
		String query = "INSERT INTO favorite (user_email, author_id) VALUES(?,?);";

		return (Boolean) t.execute(query, pss);
	}
	
	
	public boolean removeFavorite(final int reporterId, final String email) {
		JdbcTemplate t = new JdbcTemplate();
		PreparedStatementSetter pss = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
				psmt.setInt(2, reporterId);
			}
		};
		
		String query = "DELETE FROM favorite WHERE user_email =? AND author_id =?;";
		
		return (Boolean) t.execute(query, pss);
	}
	
	
	public List<Integer> getFavoriteIds(final String email) {
		JdbcTemplate t = new JdbcTemplate();
		PreparedStatementSetter pss = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
			}
			
		};
		
		RowMapper rm = new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				List<Integer> favorites = new ArrayList<Integer>();
				while (rs.next()) {
					favorites.add(rs.getInt("author_id"));
				}
				return favorites;
			}
			
		};
		
		String query = "SELECT author_id FROM favorite WHERE user_email = ?";

		return (List<Integer>) t.execute(query, pss, rm);
	}
	
	public List<Reporter> findFavoriteReporters(final String email) {
		JdbcTemplate t = new JdbcTemplate();
		PreparedStatementSetter pss = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
				//psmt.setInt(2, start);
				//psmt.setInt(3, num);
			}
			
		};
		
		RowMapper rm = new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				List<Reporter> favorites = new ArrayList<Reporter>();
				Reporter reporter = null;
				while (rs.next()) {
					reporter = new Reporter();
					reporter.setId(rs.getInt("id"));
					reporter.setEmail(rs.getString("email"));
					reporter.setName(rs.getString("name"));
					reporter.setPressName(rs.getString("press_name"));
					reporter.setArticleTitle(rs.getString("article_title"));
					reporter.setArticleURL(rs.getString("article_url"));
					
					favorites.add(reporter);
				}
				return favorites;
			}
			
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT author.* FROM (SELECT * FROM favorite WHERE user_email = ? ) AS myfav JOIN ");
		query.append("(SELECT result.id as id, result.name as name, result.email as email, article.title as article_title, press.name as press_name, result.article_URL ");
		query.append("FROM (SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id GROUP BY author.id ORDER BY author.name) as result ");
		query.append("JOIN article ON article.URL = result.article_URL JOIN press ON result.press_id = press.id) AS author ON author.id = myfav.author_id");
		
		return (List<Reporter>) t.execute(query.toString(), pss, rm);
	}
}
