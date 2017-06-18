package com.oracle.qa.dataload.service.async.tasks;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.oracle.qa.dataload.service.async.AsyncUtil;
import com.oracle.qa.dataload.web.rest.interceptors.LoggingRequestInterceptor;

public class TagCallTask {
	private String url = "http://tags.bluekai.com/site/{siteId}";

	String siteId;
	String phint;
	String headers;

	public TagCallTask(Integer siteId, String phint, String headers) {
		this.siteId = siteId.toString();
		this.phint = phint;
		this.headers = headers;
	}

	public String makeTagCalls() {
		String bkuid = "";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		
		
		
		
		if (phint != null) {
			String[] phints = phint.split("\\|\\|");
			for (String temp : phints) {
				builder.queryParam("phint", temp);
			}
		}
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("siteId", siteId);

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy() {

			@Override
			public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
					throws ProtocolException {

				if (super.isRedirected(request, response, context)) {
					int statusCode = response.getStatusLine().getStatusCode();
					String redirectURL = response.getFirstHeader("Location").getValue();

					return true;
				}
				return false;
			}
		})
				 .setProxy(new HttpHost("www-proxy.us.oracle.com", 80))

				.build();
		factory.setHttpClient(httpClient);
		RestTemplate restemplate2 = new RestTemplate(factory);
		
		HttpHeaders headers2 = new HttpHeaders();
		if (headers != null && !headers.equalsIgnoreCase("")) {
			String[] headersList = headers.split("\\|\\|");

			for (String headerTemp : headersList) {
				String[] keyValue = headerTemp.split(":");
				headers2.add(keyValue[0], keyValue[1]);

			}

		}

		HttpEntity<?> entity = new HttpEntity<>(headers2);
		ResponseEntity<String> response = restemplate2.exchange(builder.buildAndExpand(uriParams).encode().toUri(),
				HttpMethod.GET, entity, String.class);
		//System.out.println("Request===>"+builder.buildAndExpand(uriParams).toUri());
		if (HttpStatus.OK == response.getStatusCode()) {
			System.out.println(AsyncUtil.getThreadName());

			bkuid = response.getHeaders().get("Set-Cookie").get(0).split(";")[0].replaceAll("bku=", "");
		} else {
			bkuid = "BKU NOT FOUND";
		}

		return bkuid;

	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}
	
	public String getUrlParameters(Object urlParameters) throws Exception {
		// TODO Auto-generated method stub

		if (urlParameters == null) {
			return "";
		}
		List<Map<String, Object>> parameterList = null;

		if ((urlParameters instanceof Map)) {
			parameterList = new ArrayList();
			parameterList.add((Map) urlParameters);
		} else if ((urlParameters instanceof List)) {
			parameterList = (List) urlParameters;
		}

		StringBuilder sb = new StringBuilder("?");
		int parameterCount = 0;

		for (Iterator localIterator1 = parameterList.iterator(); localIterator1.hasNext();) {
			Map<String, Object> urlParametersMap = (Map<String, Object>) localIterator1.next();
			for (String key : urlParametersMap.keySet()) {
				String encodedVal = "";
				Object val = urlParametersMap.get(key);
				if (val != null) {
					if (!(val instanceof String)) {
						val = val.toString();
					}
					encodedVal = URLEncoder.encode((String) val, "UTF-8");
				}

				if (parameterCount++ > 0) {
					sb.append("&");
				}
				sb.append(URLEncoder.encode(key, "UTF-8"));
				sb.append("=");
				sb.append(encodedVal);
			}
		}

		return sb.toString();

	}

}
