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

	public static boolean isEmail(String email) {
		String regex = "^[\\w\\.-_\\+]+@[\\w-]+(\\.\\w{2,4})+$";

		return Validation.isFilled(email) && Pattern.matches(regex, email);
	}

	public static boolean isPassword(String password) {
		// client의 암호화 방식에 따라 전달되는 data의 형식을 추가 예정
		
		return Validation.isFilled(password);
	}
	
	public static boolean isFilled(String data) {
		
		if (data.length() > 0) {
			return true;
		}
		
		return false;
		
	}

}
