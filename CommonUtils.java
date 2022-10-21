package natcash.proxy.common;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class CommonUtils {

//	private static final String PUBLICKEY_STRING = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnOoWr/InRTtaHw46ul6dNd508wddyJJbdFMUzOrKkmEj8z9ceezP0QrJQsQa1+iPttig+nRdeheGLS3Aau5WetXMEx9EMBKo6EGRKF8eCnwlC4G/0fWWwwGw/4MR3ObAkRpLEsjTDmXKj7sci34xK0xjEkGeiWLPD/pPmBf+1f6aVu/UfzFXsnrPpIpZub2LlFgqIKjgTNMcxqMXoBj8XngMw2O3KJmEIgmXdrXHZkvuKkZaqDSadZIA0ExN9zAOlgLrrVAwr63lXtwLG3wzaESuPUC3neCmguoKlbPq3W20O8xsIs+D1txpIxKfmr19ycd0ukXDccrXm4YKPOvS/QIDAQAB";
//	private static final String PRIVATEKEY_STRING = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCc6hav8idFO1ofDjq6Xp013nTzB13Iklt0UxTM6sqSYSPzP1x57M/RCslCxBrX6I+22KD6dF16F4YtLcBq7lZ61cwTH0QwEqjoQZEoXx4KfCULgb/R9ZbDAbD/gxHc5sCRGksSyNMOZcqPuxyLfjErTGMSQZ6JYs8P+k+YF/7V/ppW79R/MVeyes+kilm5vYuUWCogqOBM0xzGoxegGPxeeAzDY7comYQiCZd2tcdmS+4qRlqoNJp1kgDQTE33MA6WAuutUDCvreVe3AsbfDNoRK49QLed4KaC6gqVs+rdbbQ7zGwiz4PW3GkjEp+avX3Jx3S6RcNxytebhgo869L9AgMBAAECggEAb/CraK9mhoQ14YvEJ6pRmtltMubhZ7HJXm3am+kvxEdaXzCnZy+ifa2lMGU4BSkaRUW7aZDUEsJvbRGDgIUw2oZlBIs8mNLm/nGv/U4F31L7Fn9keYMrH0i4nT9C4gUYASvxvJ/ZuR6y5+CDF5xZtVrUHnUDhV0g/HQEjisoGD5ee5QjWZDEOOPVe3moicgTBqPtFP0MWWUUsRo9VgG0DoH9QbHQj9/ziw+P4+jSuom7eydX8LUvMohblydOU6gJRsEIstz2Nu6JbzJYoUTeiJ5mOtXHEvY/k8g6a31xngso1F1QGbW4AYu1/6/Hz1bddyyxscOTz+qHT/Wfg+j8YQKBgQDimbeT+Xynh3WXcUJUh4E0c9MzSgKfiQ48OfsXO43WXWLEEbuADkr342cAfoU8IlKZKd5lLgObSI1beo9BMqw2x/Zpk5vmydTTQ+yai6aFXMjoSZYfXe40/PQqzSvZOgiMT0u22Ih44yoH7z2AQFeAv6MsrnrOiGeI4oP3JwyvhQKBgQCxRdOiEkpyUVaVrYR7S/TmxQRkvrADnGh/gpeukHO5OwfvjwSlrayEfFbCNSU49F4MweGgOODAqwqJcekyZsr81PlJUxtOTmfUAhEAINjqwWYx3mvLtfR/00mnq8vgwUsFjPxjW923S2Uyxz3u8sOHZuWyO0fzPpBybSIe3EmjGQKBgBPKkTofZD/NmPutLLntS9+k7iFgqXH9qnygI8oXrX+3kFTHp7S7itizx99HLaPe64/tFDuKFU6Ou6ntvm1XW6s9M8xs1AUIQ8pf+F5XLCghvnprUu1Saue94CWM3fOPGggHNPm3V9QXIyimQJzfAcWA92ODqb44M7LE9fm0M2txAoGAUSaG8A1xeQ2VFmXdXNfzn2SXU0sbpjuGdsCgkS/dWfEU++w7k2CCtg95Y+QSSxVKrIpgsAj5khl6dFrqBsGZsM4J5DAgMuOfkiT0BVwEEMtB7hhEvndoGs/tSs1RQm0Z28ujihR21b60797XkjgdnDFVexa6JDcCxu8AjKIJL5kCgYBde39g3WdbSY5pkpMTrz60h1y6fljjB1iDEeLMCn0b+ZxUZ+954V5evo7upmIcTpG7Cr7jRNcI0g+drrcrV4Q4G7WXrWt+aSIM5VwSRCMNvnaUNxXTte1hKHDs99RS5TyEq9rcd2JD1ImbsMad8BPoKOlFVr2w2+9Du9FMpNfokA==";

	@Value("${privatekey_string}")
	private String PRIVATEKEY_STRING;
	@Value("${publickey_string}")
	private String PUBLICKEY_STRING;
	@Value("${app_id}")
	private String app_id;
	private static final String PROXY_USER="proxyToken";

	public String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Forwarded-For");
		if (remoteAddr == null || remoteAddr.isEmpty()) {
			remoteAddr = request.getRemoteAddr();
		} else if (remoteAddr.contains(",")) {
			remoteAddr = remoteAddr.split(",")[0].trim();
		}
		return remoteAddr;
	}

	public String createToken(String userId, String username, String authRequestID) throws Exception {
		byte[] privateKeyBytes = Base64.getDecoder().decode(PRIVATEKEY_STRING);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privKey = kf.generatePrivate(keySpec);
		if (authRequestID == null) {
			JwtBuilder jwtBuilder = Jwts.builder().setSubject(username).setId(userId).claim("tokenType", "user");
			return jwtBuilder.signWith(privKey).compact();
		} else {
			JwtBuilder jwtBuilder = Jwts.builder().setSubject(username).setId(authRequestID)
					.claim("tokenType", "register")
					.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(5).toInstant()));
			return jwtBuilder.signWith(privKey).compact();
		}
		// JwtBuilder jwtBuilder = Jwts.builder().setSubject(username).setId(userId);
	}

	public boolean validateProxyToken(String jwtToken) throws Exception {
		byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLICKEY_STRING);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey publicKey = kf.generatePublic(keySpec);
		Jws<Claims> jws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwtToken);
		Claims claims = jws.getBody();
		if (app_id != null && app_id.equals(claims.getId()) && PROXY_USER.equals(claims.getSubject())) {
			return true;
		}
		return false;
	}

	public KeyPair createNewKeyPair() {
		return Keys.keyPairFor(SignatureAlgorithm.RS256);
	}

	public String getPasswordHash(String username, String password) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest((username + password).getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(hash);
	}
}

