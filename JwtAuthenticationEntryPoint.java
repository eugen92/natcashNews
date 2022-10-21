package natcash.news.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import natcash.news.NewsApplication;
import natcash.news.common.Constant;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	@Autowired
	private ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(NewsApplication.class);
	// Define the case of token state
	private static final long serialVersionUID = -7858869558953243875L;
	private static final String INVALID_TOKEN = "Token invalid";
	private static final String EXPIRED = "Token expired";
	private static final String EMPTY = "Unauthorized";
	private static final String UNSUPPORTED = "Unsupported token";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		final String expired = (String) request.getAttribute("expired");
		final String invalid = (String) request.getAttribute("invalid");
		final String empty = (String) request.getAttribute("empty");
		String errorMessage = "Unauthorized";
		if (invalid != null) {
			errorMessage = INVALID_TOKEN;
		}
		if (expired != null) {
			errorMessage = EXPIRED;
		}
		if (empty != null) {
			errorMessage = EMPTY;
		}
	    response.addHeader("Content-Type", "application/json;charset=UTF-8");
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    Status unauthorized = new Status(HttpServletResponse.SC_UNAUTHORIZED,
	                                    errorMessage);
	    objectMapper.writeValue(response.getOutputStream(), unauthorized);
	    response.flushBuffer();

	}
	public class Status {
	    private int code;
	    private String message;

	    public Status(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public String getMessage() {
	        return message;
	    }
	}
}

