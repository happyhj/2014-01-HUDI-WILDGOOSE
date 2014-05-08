package next.wildgoose.utility;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {

	private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class.getName());

	public static String encode(String original, String encodingType) {

		String encoded = null;
		try {
			encoded = new String(original.getBytes("8859_1"), encodingType);
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug("Utility error" + e.getMessage());
		}

		return encoded;
	}

	public static String getDate(Date date, int addDate) {
		DateFormat dateFormat = new SimpleDateFormat("MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDate);
		return dateFormat.format(cal.getTime());
	}
}
