package next.wildgoose.dao.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.database.DataSource;

public abstract class InsertJdbcTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(InsertJdbcTemplate.class.getName());
	Connection conn = null;
	
	public InsertJdbcTemplate () {
		this.conn = DataSource.getInstance().getConnection();
	}
	
	public boolean insert (String query) {
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
	
	public abstract void setValues (PreparedStatement psmt)  throws SQLException;
	
	
}
