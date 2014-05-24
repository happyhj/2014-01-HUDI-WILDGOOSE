package next.wildgoose.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Utility {

	public static String getDate(Date date, int addDate) {
		DateFormat dateFormat = new SimpleDateFormat("MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDate);
		return dateFormat.format(cal.getTime());
	}
	
	public static boolean isURL(String URL) {
		if (URL == null) {
			return false;
		}
		String regex = "(?i)^(http://)?(www.)?[a-z0-9-_]+.[a-z]{2,3}(.[a-z]{2,3})?.*";

		return Pattern.matches(regex, URL);
	}
}
