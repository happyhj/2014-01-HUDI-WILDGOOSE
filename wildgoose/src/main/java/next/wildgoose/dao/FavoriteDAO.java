package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.database.DataSource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FavoriteDAO {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FavoriteDAO.class.getName());

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

	public JSONObject getFavorites(String email) {
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
	}

}
