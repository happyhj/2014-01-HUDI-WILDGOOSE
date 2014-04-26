package next.wildgoose.utility;

import java.io.UnsupportedEncodingException;

public class Utility {
	
	public static String encode (String original, String encodingType) {
		
		String encoded = null; 
		try {
			encoded = new String(original.getBytes("8859_1"), encodingType);
		} catch (UnsupportedEncodingException e) {
			//nothing
			encoded = null;
		}
		
		return encoded;
	}

}
