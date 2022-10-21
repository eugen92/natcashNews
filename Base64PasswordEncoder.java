package natcash.news.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;

import org.springframework.security.crypto.password.PasswordEncoder;

//Customize base64(sha256) encode password
public class Base64PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence charSequence) {
		String result = "";
		try {
			String string = String.valueOf(charSequence);
			String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(string);
			String encodedString = Base64.getEncoder().encodeToString(sha256hex.getBytes());
			result = encodedString;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public boolean matches(CharSequence charSequence, String s) {
		return encode(charSequence).equals(s);
	}

}
