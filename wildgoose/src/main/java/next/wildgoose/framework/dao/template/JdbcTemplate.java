package next.wildgoose.framework.dao.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JdbcTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class.getName());
//	Connection conn;

	@Autowired
	private BasicDataSource dataSource;	
	
//	public JdbcTemplate() {
//		this.conn = DataSource.getInstance().getConnection();
//	}
	
	public Object execute (String query, PreparedStatementSetter pss) {
		return execute(query, pss, null);
	}
	
	public Object execute (String query, PreparedStatementSetter pss, RowMapper rm) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		Object result = null;
		
		try {
//			psmt = this.conn.prepareStatement(query);
			conn = dataSource.getConnection();
			psmt = conn.prepareStatement(query);
			pss.setValues(psmt);
			
			if (rm == null) {
				result = false;
				psmt.execute();
				result = true;
			} else {
				rs = psmt.executeQuery();
				result = rm.mapRow(rs);
			}
			
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
			
		} finally {
			SqlUtil.closeResultSet(rs);
			SqlUtil.closePrepStatement(psmt);
//			SqlUtil.closeConnection(this.conn);
			SqlUtil.closeConnection(conn);
		}
				
		return result;
	}
	
//	public abstract void setValues(PreparedStatement psmt) throws SQLException;
}
