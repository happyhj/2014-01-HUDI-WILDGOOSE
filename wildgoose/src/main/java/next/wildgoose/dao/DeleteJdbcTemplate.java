package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.database.DataSource;

public abstract class DeleteJdbcTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteJdbcTemplate.class.getName());
	Connection conn;
	
	public DeleteJdbcTemplate() {
		this.conn = DataSource.getInstance().getConnection();
	}
	
	public boolean delete (String query) {
		PreparedStatement psmt = null;
		boolean result = false;
		try {
			psmt = this.conn.prepareStatement(query);
			setValues(psmt);
			psmt.execute();
			result = true;
			
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
			
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeConnection(this.conn);
		}
				
		return result;
	}
	
	public abstract void setValues(PreparedStatement psmt) throws SQLException;
}
