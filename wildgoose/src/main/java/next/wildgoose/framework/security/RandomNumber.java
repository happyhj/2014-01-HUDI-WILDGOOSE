package next.wildgoose.framework.security;

import java.util.Random;

import javax.servlet.http.HttpSession;

public class RandomNumber {
	private RandomNumber() {
		
	}
	public static String set(HttpSession session) {
		Random random = new Random();
		String rand = Double.toString(random.nextDouble());
		rand = rand.replace("0.", "");
		session.setAttribute("randNum", rand);
		return rand;
	}
	
	public static String get(HttpSession session) {
		return (String) session.getAttribute("randNum");
	}
}
