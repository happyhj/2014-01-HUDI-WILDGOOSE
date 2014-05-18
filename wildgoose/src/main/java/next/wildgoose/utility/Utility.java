package next.wildgoose.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {

	public static String getDate(Date date, int addDate) {
		DateFormat dateFormat = new SimpleDateFormat("MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDate);
		return dateFormat.format(cal.getTime());
	}
}
