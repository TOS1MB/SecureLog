package secureLog;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;

public class Crypt {

	public static String encryptString(String data) {

		String sha1 = DigestUtils.sha1Hex(data);

		// #Old-Version
		// MessageDigest crypt;
		//
		// try {
		// crypt = MessageDigest.getInstance("SHA-1");
		// crypt.reset();
		// crypt.update(data.getBytes("UTF-8"));
		//
		// } catch (NoSuchAlgorithmException e1) {
		// e1.printStackTrace();
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

		return sha1;
	}

	public static String generateHmac(String secretKey, String data, int lineCounter) {

		while (lineCounter > 0) {
			secretKey = encryptString(secretKey);
			lineCounter--;
		}

		String hMac = HmacUtils.hmacSha1Hex(secretKey, data);

		return hMac;

	}

}
