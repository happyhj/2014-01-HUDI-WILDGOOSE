package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.database.DataSource;
import next.wildgoose.dto.ReporterCard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FavoriteDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteDAO.class.getName());

	public boolean addFavorite(String reporterId, String email) {
		boolean result = false;
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;

		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO favorite (user_email, author_id) VALUES(?,?);");

		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, email);
			psmt.setString(2, reporterId);
			psmt.execute();
			result = true;
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeConnection(conn);
		}
		return result;
	}

	public boolean removeFavorite(String reporterId, String email) {
		boolean result = false;
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;

		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM favorite WHERE user_email =? AND author_id =?;");

		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, email);
			psmt.setString(2, reporterId);
			psmt.execute();
			result = true;
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeConnection(conn);
		}
		return result;
	}
	
	public List<Integer> getFavorites(String email) {
		List<Integer> favorites = new ArrayList<Integer>();
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;

		StringBuilder query = new StringBuilder();
		query.append("SELECT author_id FROM favorite WHERE user_email = ?");

		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, email);
			rs = psmt.executeQuery();

			while (rs.next()) {
				favorites.add(rs.getInt("author_id"));
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closeResultSet(rs);
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeConnection(conn);
		}
		return favorites;
	}
	
	public List<ReporterCard> findReporterCard(String email) {		
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<ReporterCard> favorites = new ArrayList<ReporterCard>();
		ReporterCard reporterCard = null;
		

		StringBuilder query = new StringBuilder();
		query.append("SELECT author.* FROM (SELECT * FROM favorite WHERE user_email = ? ) AS myfav JOIN ");
		query.append("(SELECT result.id as id, result.name as name, result.email as email, article.title as article_title, press.name as press_name, result.article_URL ");
		query.append("FROM (SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id GROUP BY author.id ORDER BY author.name) as result ");
		query.append("JOIN article ON article.URL = result.article_URL JOIN press ON result.press_id = press.id) AS author ON author.id = myfav.author_id LIMIT 24");

		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, email);
			//psmt.setInt(2, start);
			//psmt.setInt(3, num);
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				reporterCard = new ReporterCard();
				reporterCard.setId(rs.getInt("id"));
				reporterCard.setEmail(rs.getString("email"));
				reporterCard.setName(rs.getString("name"));
				reporterCard.setPressName(rs.getString("press_name"));
				reporterCard.setArticleTitle(rs.getString("article_title"));
				reporterCard.setArticleURL(rs.getString("article_url"));

				favorites.add(reporterCard);
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(),sqle);
			favorites = null;
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		return favorites;
	}
	
	/*
	public JSONObject getFavoritesAsJson(String email) {
		JSONObject result = new JSONObject();
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT author_id FROM favorite WHERE user_email = ?");

		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, email);
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				result.append("data", rs.getInt("author_id"));
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closeResultSet(rs);
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeConnection(conn);
		}
		return result;
	} */

}
