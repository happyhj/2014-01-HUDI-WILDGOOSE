package next.wildgoose.utility;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {

	private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class
			.getName());

	public static String encode(String original, String encodingType) {

		String encoded = null;
		try {
			encoded = new String(original.getBytes("8859_1"), encodingType);
		} catch (UnsupportedEncodingException e) {
			// nothing
			encoded = null;
		}

		return encoded;
	}

	public static boolean isURL(String URL) {

		String regex = "(?i)^(http://)?(www.)?[a-z0-9-_]+.[a-z]{2,3}(.[a-z]{2,3})?.*";

		return Pattern.matches(regex, URL);
	}

	// database
	public static void closeConnection(final Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException sqle) {
				LOGGER.debug(sqle.getMessage(),sqle);
			}
		}
	}

	public static void closePrepStatement(final PreparedStatement psmt) {
		if (psmt != null) {
			try {
				psmt.close();
			} catch (SQLException sqle) {
				LOGGER.debug(sqle.getMessage(),sqle);
			}
		}
	}

	public static void closeResultSet(final ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException sqle) {
				LOGGER.debug(sqle.getMessage(),sqle);
			}
		}
	}

	public static String getDate(Date date, int addDate) {
		DateFormat dateFormat = new SimpleDateFormat("MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDate);
		return dateFormat.format(cal.getTime());
	}

}
