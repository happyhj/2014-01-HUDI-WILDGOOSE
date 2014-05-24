package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.database.DataSource;

public class SearchKeywordDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchKeywordDAO.class.getName());
	
	public void addKeywordRecord (String keyword) {
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO search_keyword (keyword) VALUES (?) ");
		query.append("ON DUPLICATE KEY UPDATE count = count + 1");
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, keyword);
			psmt.execute();
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
	}
}	
