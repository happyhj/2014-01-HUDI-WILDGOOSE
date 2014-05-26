package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.database.DataSource;
import next.wildgoose.dto.ReporterCard;

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

	abstract Object mapRow(ResultSet rs) throws SQLException ;

	abstract void setValues(PreparedStatement psmt) throws SQLException ;

	

}
