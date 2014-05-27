package next.wildgoose.dao.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.database.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SelectJdbcTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectJdbcTemplate.class.getName());
	Connection conn;
	
	public SelectJdbcTemplate () {
		this.conn = DataSource.getInstance().getConnection();
	}
	
	public Object select (String query) {
		Object obj = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = this.conn.prepareStatement(query);
			setValues(psmt);
			
			rs = psmt.executeQuery();
			obj = mapRow(rs);

		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(this.conn);
		}
		
		return obj;
	}

	protected abstract Object mapRow(ResultSet rs) throws SQLException ;

	protected abstract void setValues(PreparedStatement psmt) throws SQLException ;

	

}
