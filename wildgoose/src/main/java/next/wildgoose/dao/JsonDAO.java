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
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonDAO.class.getName());
	public String getSimilarNames(String name) {
		JSONObject result = new JSONObject();
		String query = "SELECT name FROM author WHERE name LIKE ? ORDER BY name LIMIT 0, 5 ";
		PreparedStatement psmt = null;
		Connection conn = null;
		ResultSet rs = null;
		conn = DataSource.getInstance().getConnection();
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, name + "%");
			LOGGER.debug(psmt.toString());
			rs = psmt.executeQuery();
			while (rs.next()) {
				JSONObject data = new JSONObject().put("name", rs.getString("name"));
				result.append("data", data);
			}
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
		}
		return result.toString();
	}
}
