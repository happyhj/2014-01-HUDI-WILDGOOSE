package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SqlUtil.class.getName());
	// database
	public static void closeConnection(final Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException sqle) {
				LOGGER.debug(sqle.getMessage(),sqle);
			}
		}
	}

	public static void closePrepStatement(final PreparedStatement psmt) {
		if (psmt != null) {
			try {
				psmt.close();
			} catch (SQLException sqle) {
				LOGGER.debug(sqle.getMessage(),sqle);
			}
		}
	}

	public static void closeResultSet(final ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException sqle) {
				LOGGER.debug(sqle.getMessage(),sqle);
			}
		}
	}
}
