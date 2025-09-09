/**
 * 
 */
package com.hitech.dms.app.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hitech.dms.constants.AppConstants;

/**
 * @author dinesh.jakhar
 *
 */
public class CustomPasswordEncoder implements PasswordEncoder {
	@Autowired
	private MsecretKey msecretKey;
//	public static final String SECRET_KEY = "HiTeCh";

	@Override
	public String encode(CharSequence rawPassword) {
		String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(12));
		return hashed;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String decryptedPassword = null;
		String password = rawPassword.toString();
		try {
			decryptedPassword = new String(java.util.Base64.getDecoder().decode(password));
			AesUtil aesUtil = new AesUtil(128, 1000);
			if (decryptedPassword != null && decryptedPassword.split("::").length == 3) {
				try {
					password = aesUtil.decrypt(decryptedPassword.split("::")[1], decryptedPassword.split("::")[0], AppConstants.SECRET_KEY,
							decryptedPassword.split("::")[2]);
				}catch(IllegalArgumentException ex) {
					ex.printStackTrace();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return BCrypt.checkpw(password, encodedPassword);
	}
}
