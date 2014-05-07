package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.pool.DataSource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDAO {
	private static JsonDAO jsonDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonDAO.class.getName());
	public static JsonDAO getInstance() {
		if (jsonDao == null) {
			jsonDao = new JsonDAO();
		}
		return jsonDao;
	}
	public JSONObject getSimilarNames(String name) {
		JSONObject result = new JSONObject();
		String query = "SELECT name FROM author WHERE name LIKE ? ORDER BY name LIMIT 0, 5 ";
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, name + "%");
			rs = psmt.executeQuery();
			while (rs.next()) {
				JSONObject data = new JSONObject();
				data.put("name", rs.getString("name"));
				result.append("data", data);
			}
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage(), e);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		return result;
	}
}
