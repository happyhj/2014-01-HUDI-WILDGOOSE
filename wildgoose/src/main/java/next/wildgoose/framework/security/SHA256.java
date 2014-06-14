package next.wildgoose.framework.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SHA256 {
	private static final Logger LOGGER = LoggerFactory.getLogger(SHA256.class.getName());
	public static String testSHA256(String str){
		String SHA = ""; 
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();
			
		} catch(NoSuchAlgorithmException e){
			LOGGER.error(e.getMessage(), e);
			SHA = null; 
		}
		return SHA;
	}
}
