package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import next.wildgoose.model.DataSource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleCountDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleCountDAO.class.getName());

	public JSONObject countArticle(int reporterId) throws SQLException {
		JSONObject result = new JSONObject();
		PreparedStatement psmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		
		// getting database connection to MySQL server
		
		// 날짜 구하기
		for (int i = -6; i < 1; i++) {
			String date = getDate(new Date(), i);
			StringBuilder query = new StringBuilder();
			query.append("SELECT count(*) as count from article where URL ");
			query.append("IN (select article_URL from article_author where author_id=? ");
			query.append(") and datetime like ?;");
			
			System.out.println(query.toString());

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
				if (rs != null) rs.close();
				if (psmt != null) psmt.close();
				if (conn != null) conn.close();
			}
		}

		// return DateForCounts;
		return result;
	}

	public static String getDate(Date date, int addDate) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDate);
		return dateFormat.format(cal.getTime());
	}

}
