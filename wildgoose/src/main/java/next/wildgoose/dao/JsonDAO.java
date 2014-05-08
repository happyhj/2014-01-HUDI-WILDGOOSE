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

	public JSONObject getSimilarNames(String name) {
		JSONObject result = new JSONObject();
		String query = "SELECT name FROM author WHERE name LIKE ? ORDER BY name LIMIT 0, 5 ";
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		LOGGER.debug(name);
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

	public JSONObject moreReporterCard(String name, int start, int num) {
		JSONObject result = new JSONObject();
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT result.id as id, result.name as name, result.email as email, article.title as title, press.name as press_name ");
		query.append("FROM (SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id ");
		query.append("WHERE author.name LIKE ? GROUP BY author.id ORDER BY author.name ");
		query.append("LIMIT ?, ?) as result ");
		query.append("JOIN article ON article.URL = result.article_URL ");
		query.append("JOIN press ON result.press_id = press.id");
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, "%" + name + "%");
			psmt.setInt(2, start);
			psmt.setInt(3, num);
			rs = psmt.executeQuery();
			while (rs.next()) {
				JSONObject data = new JSONObject();
				data.put("id", rs.getInt("id"));
				data.put("email", rs.getString("email"));
				data.put("name", rs.getString("name"));
				data.put("pressName", rs.getString("press_name"));
				data.put("articleTitle", rs.getString("title"));
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
