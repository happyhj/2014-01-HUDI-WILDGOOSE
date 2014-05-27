package next.wildgoose.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.dao.template.SelectJdbcTemplate;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonDAO.class.getName());

	public JSONObject getSimilarNames(final String name, final int count) {
		
		SelectJdbcTemplate template = new SelectJdbcTemplate() {

			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				JSONObject result = new JSONObject();
				
				while (rs.next()) {
					JSONObject data = new JSONObject();
					data.put("name", rs.getString("name"));
					result.append("data", data);
				}
				
				return result;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, name + "%");
				psmt.setInt(2, count);
			}
		};

		String query = "SELECT name FROM author WHERE name LIKE ? ORDER BY name LIMIT 0, ? ";
		JSONObject result = (JSONObject) template.select(query);
		
		return result;
	}

	// 일단 변경했지만 reporterCardDAO의 findReportersByType()과 동등.
	public JSONObject moreReporterCard(String type, final String searchQuery, final int start, final int num) {
		
		SelectJdbcTemplate template = new SelectJdbcTemplate() {

			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				JSONObject result = new JSONObject();
				
				while (rs.next()) {
					JSONObject data = new JSONObject();
					data.put("id", rs.getInt("id"));
					data.put("email", rs.getString("email"));
					data.put("name", rs.getString("name"));
					data.put("pressName", rs.getString("press_name"));
					data.put("articleTitle", rs.getString("title"));
					result.append("data", data);
				}
				return result;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, "%" + searchQuery + "%");
				psmt.setInt(2, start);
				psmt.setInt(3, num);
			}
		};
		
		String where = null;
		if ("name".equals(type)) {
			where = "author.name";
		}
		else if ("url".equals(type)) {
			where = "aa.article_URL";
		}
		StringBuilder query = new StringBuilder();
		query.append("SELECT result.id as id, result.name as name, result.email as email, article.title as title, press.name as press_name ");
		query.append("FROM (SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id ");
		query.append("WHERE ").append(where).append(" LIKE ? GROUP BY author.id ORDER BY author.name ");
		query.append("LIMIT ?, ?) as result ");
		query.append("JOIN article ON article.URL = result.article_URL ");
		query.append("JOIN press ON result.press_id = press.id");

		JSONObject jsonReporters = (JSONObject) template.select(query.toString());

		return jsonReporters;
	}
}
