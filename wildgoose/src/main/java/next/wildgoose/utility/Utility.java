package next.wildgoose.utility;

import java.io.UnsupportedEncodingException;

public class Utility {
	
	public String encode(String rawData, String encodeType) throws NullPointerException, UnsupportedEncodingException {
		
		return new String(rawData.getBytes("8859_1"), encodeType);
	} 
	
}
