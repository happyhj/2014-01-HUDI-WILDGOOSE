package next.wildgoose.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import next.wildgoose.model.DatabaseConnector;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleCount {
	static Logger logger = LoggerFactory.getLogger(ReporterCardDAO.class.getName());

	String mysqlQuery = null;
	ResultSet rs = null;

	public JSONObject countArticle(int reporterId) {
		JSONObject result = new JSONObject();

		// getting database connection to MySQL server
		// 날짜 구하기
		for (int i = -6; i < 1; i++) {
			String date = getDate(new Date(), i);

			mysqlQuery = "SELECT count(*) as count from article where URL IN (select article_URL from article_author where author_id="
					+ reporterId + ") and datetime like '%" + date + "%';";
			
			System.out.println(mysqlQuery);

			try {
				rs = DatabaseConnector.select(mysqlQuery);

				while (rs.next()) {

					JSONObject data = new JSONObject().put(date, rs.getInt("count"));
					result.append("data", data);

				}

			} catch (SQLException sqle) {

				logger.debug(sqle.getMessage(), sqle);
			}
		}

		// return DateForCounts;
		return result;
	}

	public static String getDate(Date date, int addDate) {
		DateFormat dateFormat = new SimpleDateFormat("MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDate);
		return dateFormat.format(cal.getTime());
	}

}
