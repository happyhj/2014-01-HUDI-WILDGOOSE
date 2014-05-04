package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.utility.Utility;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberOfArticlesDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfArticlesDAO.class.getName());

	public JSONObject byDay(int reporterId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		PreparedStatement psmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		StringBuilder query = new StringBuilder();
		// getting database connection to MySQL server
		query.append("select date_format(datetime, '%m/%d') as date, count(URL) as count from article ");
		query.append("WHERE URL in (Select article_URL as url from article_author where author_id = ?) ");
		query.append("and DATEDIFF(now(), article.datetime) < 7 ");
		query.append("group by date_format(datetime, '%m/%d');");
		
		try {
			conn = DataSource.getInstance().getConnection();
			psmt = conn.prepareStatement(query.toString());
			psmt.setInt(1, reporterId);
			
			rs = psmt.executeQuery();

			while (rs.next()) {
				data.accumulate(rs.getString("date"), rs.getInt("count"));	
			}
			result.put("data", data);
			
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
			
		} finally {
			Utility.closePrepStatement(psmt);
			Utility.closeResultSet(rs);
			Utility.closeConnection(conn);
		}

		return result;
	}
	
	public JSONObject bySection (int reporterId) {
		JSONObject result = new JSONObject();
		PreparedStatement psmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT distinct sec.name, count(section_id) FROM article ");
		query.append("JOIN (SELECT author_id, article_URL FROM article_author as aa WHERE aa.author_id = ?) as result ");
		query.append("ON URL = result.article_URL ");
		query.append("JOIN section as sec ");
		query.append("ON section_id = sec.id ");
		query.append("GROUP BY section_id ");
		query.append("ORDER BY section_id ");
		query.append("LIMIT 5;");
		
		LOGGER.debug(query.toString());
		LOGGER.debug(Integer.toString(reporterId));
		
		try {
			conn = DataSource.getInstance().getConnection();
			psmt = conn.prepareStatement(query.toString());
			psmt.setInt(1, reporterId);

			rs = psmt.executeQuery();

			JSONObject subJsonObj = null;
			while (rs.next()) {
				subJsonObj = new JSONObject();
				subJsonObj.put("label", rs.getString(1));
				subJsonObj.put("value", rs.getInt(2));
				
				result.append("data", subJsonObj);
			}

		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
			
		} finally {
			Utility.closePrepStatement(psmt);
			Utility.closeResultSet(rs);
			Utility.closeConnection(conn);
		}

		return result;
	}

}
