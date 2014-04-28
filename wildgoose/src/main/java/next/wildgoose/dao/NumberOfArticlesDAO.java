package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import next.wildgoose.model.DataSource;
import next.wildgoose.utility.Utility;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberOfArticlesDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfArticlesDAO.class.getName());

	public JSONObject byDay(int reporterId) {
		JSONObject result = new JSONObject();
		PreparedStatement psmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		
		// getting database connection to MySQL server
		
		// 날짜 구하기
		for (int i = -6; i < 1; i++) {
			String date = Utility.getDate(new Date(), i);
			StringBuilder query = new StringBuilder();
			query.append("SELECT count(*) as count from article where URL ");
			query.append("IN (select article_URL from article_author where author_id=? ");
			query.append(") and datetime like ?;");
			
			LOGGER.debug(query.toString());

			try {
				conn = DataSource.getInstance().getConnection();
				psmt = conn.prepareStatement(query.toString());
				psmt.setInt(1, reporterId);
				psmt.setString(2, "%" + date + "%");
				
				rs = psmt.executeQuery();

				while (rs.next()) {
					JSONObject data = new JSONObject().put(date, rs.getInt("count"));
					result.append("data", data);
				}

			} catch (SQLException sqle) {
				LOGGER.debug(sqle.getMessage(), sqle);
				
			} finally {
				Utility.closePrepStatement(psmt);
				Utility.closeResultSet(rs);
				Utility.closeConnection(conn);
			}
		}

		// return DateForCounts;
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
