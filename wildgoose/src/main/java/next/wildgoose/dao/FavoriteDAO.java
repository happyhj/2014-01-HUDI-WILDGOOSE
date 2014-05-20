package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.database.DataSource;

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

}
