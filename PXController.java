package natcash.proxy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import natcash.proxy.common.CommonUtils;

@RestController
@RequestMapping(path = "/proxy")
public class PXController {

	@Value("${bidvrunservice.url}")
	private String bidvrunserviceUrl;
	@Value("${bidvrunserviceAdmin.url}")
	private String bidvrunserviceAdminUrl;
	@Value("${app_token}")
	private String app_token;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CommonUtils commonUtils;
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(path = { "/forward" }, method = RequestMethod.POST)
	public ResponseEntity<String> modifyResponse(@RequestBody Map<String, Object> requestBody,
			@RequestHeader Map<String, String> requestHeader) {
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		try {
			String uri = (String) requestBody.get("uri");
			String method = (String) requestBody.get("method");
			String accessToken = (String) requestHeader.get("authorization");
			if (accessToken != null && accessToken.isEmpty() == false && commonUtils.validateProxyToken(accessToken)) {
//			Accept token from client - create request to forward API

				requestHeader.replace("authorization", app_token);
				HttpHeaders headers = new HttpHeaders();
				if (requestHeader != null) {
					for (String headerKey : requestHeader.keySet()) {
						headers.add(headerKey, requestHeader.get(headerKey));
					}
				}
				
				Object bidvrunBody = requestBody.get("natcashBody");
				Map<String, Object> param = new HashMap<String, Object>();
				HttpEntity<Object> entity = new HttpEntity<Object>(bidvrunBody, headers);
				try {
					ResponseEntity<Map<String, Object>> serviceResponse = restTemplate.exchange(bidvrunserviceUrl + uri,
							HttpMethod.resolve(method), entity, new ParameterizedTypeReference<Map<String, Object>>() {
							}, param);
					if (serviceResponse != null) {
						Map<String, Object> serviceResponseBody = serviceResponse.getBody();
						ObjectMapper mapper = new ObjectMapper();
						Map<String, Object> proxyResponse = new HashMap<String, Object>();
						Map<String, Object> proxyHeader = new HashMap<String, Object>();
						String resultCode = "0";
						String resultMessage = "Success";
						if (serviceResponseBody.get("errorCode") != null) {
							resultCode = String.valueOf(serviceResponseBody.get("errorCode"));
						}
						if (serviceResponseBody.get("errorMessage") != null) {
							resultMessage = String.valueOf(serviceResponseBody.get("errorMessage"));
						}
						proxyHeader.put("resultCode", resultCode);
						proxyHeader.put("resultMessage", resultMessage);
						proxyResponse.put("header", proxyHeader);
						proxyResponse.put("body", serviceResponseBody);
						return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse), HttpStatus.OK);}
				} catch (HttpClientErrorException e) {
					ObjectMapper mapper = new ObjectMapper();
					String responseString = e.getResponseBodyAsString();
					int serviceResponseCode = e.getRawStatusCode();
					Map<String, Object> proxyResponse = new HashMap<String, Object>();
					if (responseString != null && responseString.isEmpty() == false) {
						Map<String, Object> serviceResponseBody = mapper.readValue(responseString,
								new TypeReference<HashMap<String, Object>>() {
								});
						Map<String, Object> proxyHeader = new HashMap<String, Object>();
						proxyHeader.put("errorcode", serviceResponseBody.get("errorCode"));
						proxyHeader.put("code", serviceResponseBody.get("code"));
						proxyHeader.put("message", serviceResponseBody.get("message"));
						proxyHeader.put("errordesc", serviceResponseBody.get("errorMessage"));
						proxyResponse.put("header", proxyHeader);
					}
					proxyResponse.put("body", responseString);

					try {
						return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse), e.getStatusCode());
					} catch (JsonProcessingException e1) {
						return new ResponseEntity<String>(
								"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
								HttpStatus.INTERNAL_SERVER_ERROR);

					}
				}

			} else {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> proxyResponse = new HashMap<String, Object>();
				Map<String, Object> proxyHeader = new HashMap<String, Object>();
				proxyHeader.put("errorcode", "406");
				proxyHeader.put("errordesc", "Proxy invalid token");
				proxyHeader.put("requestID", null);
				proxyResponse.put("header", proxyHeader);
				proxyResponse.put("body", null);
				return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse), HttpStatus.NOT_ACCEPTABLE);
			}
		} catch (ExpiredJwtException e) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> proxyResponse = new HashMap<String, Object>();
			Map<String, Object> proxyHeader = new HashMap<String, Object>();
			proxyHeader.put("errorcode", "406");
			proxyHeader.put("errordesc", "Proxy token expire");
			proxyHeader.put("requestID", null);
			proxyResponse.put("header", proxyHeader);
			proxyResponse.put("body", null);
//			try {
//				return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse),
//						HttpStatus.INTERNAL_SERVER_ERROR);
//			} catch (JsonProcessingException e1) {
//				return new ResponseEntity<String>(
//						"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
//						HttpStatus.INTERNAL_SERVER_ERROR);
//			}
		} catch (Exception e) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> proxyResponse = new HashMap<String, Object>();
			Map<String, Object> proxyHeader = new HashMap<String, Object>();
			proxyHeader.put("errorcode", "500");
			proxyHeader.put("errordesc", "Proxy " + e.getMessage());
			proxyHeader.put("requestID", null);
			proxyResponse.put("header", proxyHeader);
			proxyResponse.put("body", null);
//			try {
//				return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse),
//						HttpStatus.INTERNAL_SERVER_ERROR);
//			} catch (JsonProcessingException e1) {
//				return new ResponseEntity<String>(
//						"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
//						HttpStatus.INTERNAL_SERVER_ERROR);
//			}
		}
		return null;
	}

}
