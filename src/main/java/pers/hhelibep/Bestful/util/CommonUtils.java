package pers.hhelibep.Bestful.util;

import java.util.Base64;

public class CommonUtils {

	public static String getBase64Encoded(String user, String password) {
		return new String(Base64.getEncoder().encode((user + ":" + password).getBytes()));
	}
}
