package natcash.proxy.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import natcash.proxy.common.CommonUtils;
@RestController
@RequestMapping(path = "/proxy")
public class ProxyController {

	@Value("${bidvrunservice.url}")
	private String bidvrunserviceUrl;
	@Value("${bidvrunserviceAdmin.url}")
	private String bidvrunserviceAdminUrl;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CommonUtils commonUtils;

	@RequestMapping(path = { "/cms", "/cust_token", "/web_admin" }, method = RequestMethod.POST)
	public ResponseEntity<String> modifyResponse(@RequestBody Map<String, Object> requestBody) {
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		try {
			String uri = (String) requestBody.get("uri");
			String method = (String) requestBody.get("method");
			String tokenProxy = (String) requestBody.get("proxyToken");
			if (tokenProxy != null && tokenProxy.isEmpty() == false && commonUtils.validateProxyToken(tokenProxy)) {
				HttpHeaders headers = new HttpHeaders();
				Map<String, String> bidvrunHeaders = (Map<String, String>) requestBody.get("bidvrunHeaders");
				if (bidvrunHeaders != null) {
					for (String headerKey : bidvrunHeaders.keySet()) {
						headers.add(headerKey, bidvrunHeaders.get(headerKey));
					}
				}
				String url = bidvrunserviceUrl;
				if (uri.indexOf("back_end") != -1 || uri.indexOf("/api/authenticate") != -1
						|| uri.indexOf("/grant_role_webadmin/auth") != -1) {
					url = bidvrunserviceAdminUrl;
				}
				Object bidvrunBody = requestBody.get("bidvrunBody");
				Map<String, Object> param = new HashMap<String, Object>();
				HttpEntity<Object> entity = new HttpEntity<Object>(bidvrunBody, headers);
				try {
					ResponseEntity<Map<String, Object>> serviceResponse = restTemplate.exchange(url + uri,
							HttpMethod.resolve(method), entity, new ParameterizedTypeReference<Map<String, Object>>() {
							}, param);
					if (serviceResponse != null) {
						Map<String, Object> serviceResponseBody = serviceResponse.getBody();
						ObjectMapper mapper = new ObjectMapper();
						Map<String, Object> proxyResponse = new HashMap<String, Object>();
						Map<String, Object> proxyHeader = new HashMap<String, Object>();
						String errorCode = "0";
						String errorMessage = "Success";
						if (serviceResponseBody.get("errorCode") != null) {
							errorCode = String.valueOf(serviceResponseBody.get("errorCode"));
						}
						if (serviceResponseBody.get("errorMessage") != null) {
							errorMessage = String.valueOf(serviceResponseBody.get("errorMessage"));
						}
						proxyHeader.put("errorcode", errorCode);
						proxyHeader.put("errordesc", errorMessage);
						proxyHeader.put("requestID", null);
						proxyResponse.put("header", proxyHeader);
						proxyResponse.put("body", serviceResponseBody);
						return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse), HttpStatus.OK);
					} else {
						ObjectMapper mapper = new ObjectMapper();
						Map<String, Object> proxyResponse = new HashMap<String, Object>();
						Map<String, Object> proxyHeader = new HashMap<String, Object>();
						proxyHeader.put("errorcode", "500");
						proxyHeader.put("errordesc", "Proxy Fail to get response from service");
						proxyHeader.put("requestID", null);
						proxyResponse.put("header", proxyHeader);
						proxyResponse.put("body", null);
						return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse),
								HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} catch (HttpServerErrorException e) {
					ObjectMapper mapper = new ObjectMapper();
					String responseString = e.getResponseBodyAsString();
					Map<String, Object> proxyResponse = new HashMap<String, Object>();
					if (responseString != null && responseString.isEmpty() == false) {
						Map<String, Object> serviceResponseBody = mapper.readValue(responseString,
								new TypeReference<HashMap<String, Object>>() {
								});
						Map<String, Object> proxyHeader = new HashMap<String, Object>();
						proxyHeader.put("errorcode", serviceResponseBody.get("errorCode"));
						proxyHeader.put("errordesc", serviceResponseBody.get("errorMessage"));
						proxyHeader.put("requestID", null);
						proxyResponse.put("header", proxyHeader);
					}
					proxyResponse.put("body", null);
					try {
						return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse), e.getStatusCode());
					} catch (JsonProcessingException e1) {
						return new ResponseEntity<String>(
								"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
								HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} catch (HttpClientErrorException e) {
					ObjectMapper mapper = new ObjectMapper();
					String responseString = e.getResponseBodyAsString();
					int serviceResponseCode=e.getRawStatusCode();
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
						proxyHeader.put("requestID", null);
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
			try {
				return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (JsonProcessingException e1) {
				return new ResponseEntity<String>(
						"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> proxyResponse = new HashMap<String, Object>();
			Map<String, Object> proxyHeader = new HashMap<String, Object>();
			proxyHeader.put("errorcode", "500");
			proxyHeader.put("errordesc", "Proxy " + e.getMessage());
			proxyHeader.put("requestID", null);
			proxyResponse.put("header", proxyHeader);
			proxyResponse.put("body", null);
			try {
				return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (JsonProcessingException e1) {
				return new ResponseEntity<String>(
						"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@RequestMapping(path = { "/auth_callback" }, method = RequestMethod.POST)
	public ResponseEntity<String> notModifyResponse(@RequestBody Map<String, Object> requestBody) {
		try {
			String uri = (String) requestBody.get("uri");
			String method = (String) requestBody.get("method");
			String tokenProxy = (String) requestBody.get("proxyToken");
			if (tokenProxy != null && tokenProxy.isEmpty() == false && commonUtils.validateProxyToken(tokenProxy)) {
				HttpHeaders headers = new HttpHeaders();
				Map<String, String> bidvrunHeaders = (Map<String, String>) requestBody.get("bidvrunHeaders");
				if (bidvrunHeaders != null) {
					for (String headerKey : bidvrunHeaders.keySet()) {
						headers.add(headerKey, bidvrunHeaders.get(headerKey));
					}
				}
				Object bidvrunBody = requestBody.get("bidvrunBody");
				Map<String, Object> param = new HashMap<String, Object>();
				HttpEntity<Object> entity = new HttpEntity<Object>(bidvrunBody, headers);
				try {
					ResponseEntity<String> serviceResponse = restTemplate.exchange(bidvrunserviceUrl + uri,
							HttpMethod.resolve(method), entity, String.class, param);
					return new ResponseEntity<String>(serviceResponse.getBody(), HttpStatus.OK);
				} catch (HttpServerErrorException e) {
					ObjectMapper mapper = new ObjectMapper();
					String responseString = e.getResponseBodyAsString();
					Map<String, Object> proxyResponse = new HashMap<String, Object>();
					if (responseString != null && responseString.isEmpty() == false) {
						Map<String, Object> serviceResponseBody = mapper.readValue(responseString,
								new TypeReference<HashMap<String, Object>>() {
								});
						Map<String, Object> proxyHeader = new HashMap<String, Object>();
						proxyHeader.put("errorcode", serviceResponseBody.get("errorCode"));
						proxyHeader.put("errordesc", serviceResponseBody.get("errorMessage"));
						proxyHeader.put("requestID", null);
						proxyResponse.put("header", proxyHeader);
					}
					proxyResponse.put("body", null);
					try {
						return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse), e.getStatusCode());
					} catch (JsonProcessingException e1) {
						return new ResponseEntity<String>(
								"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
								HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} catch (HttpClientErrorException e) {
					ObjectMapper mapper = new ObjectMapper();
					String responseString = e.getResponseBodyAsString();
					Map<String, Object> proxyResponse = new HashMap<String, Object>();
					if (responseString != null && responseString.isEmpty() == false) {
						Map<String, Object> serviceResponseBody = mapper.readValue(responseString,
								new TypeReference<HashMap<String, Object>>() {
								});
						Map<String, Object> proxyHeader = new HashMap<String, Object>();
						proxyHeader.put("errorcode", serviceResponseBody.get("errorCode"));
						proxyHeader.put("errordesc", serviceResponseBody.get("errorMessage"));
						proxyHeader.put("requestID", null);
						proxyResponse.put("header", proxyHeader);
					}
					proxyResponse.put("body", null);
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
			try {
				return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (JsonProcessingException e1) {
				return new ResponseEntity<String>(
						"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> proxyResponse = new HashMap<String, Object>();
			Map<String, Object> proxyHeader = new HashMap<String, Object>();
			proxyHeader.put("errorcode", "500");
			proxyHeader.put("errordesc", "Proxy " + e.getMessage());
			proxyHeader.put("requestID", null);
			proxyResponse.put("header", proxyHeader);
			proxyResponse.put("body", null);
			try {
				return new ResponseEntity<String>(mapper.writeValueAsString(proxyResponse),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (JsonProcessingException e1) {
				return new ResponseEntity<String>(
						"{\"header\":{\"errorcode\":\"500\",\"errordesc\":\"Proxy unable to create json\"},\"body\":{}}",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
}
