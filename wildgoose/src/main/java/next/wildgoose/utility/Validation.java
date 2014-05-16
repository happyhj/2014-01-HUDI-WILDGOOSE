package next.wildgoose.utility;

import java.util.regex.Pattern;

public class Validation {

	public static boolean isURL(String URL) {
		if (URL == null) {
			return false;
		}
		String regex = "(?i)^(http://)?(www.)?[a-z0-9-_]+.[a-z]{2,3}(.[a-z]{2,3})?.*";

		return Pattern.matches(regex, URL);
	}

	

}
