package com.oracle.qa.dataload.service.async.tasks;

import java.util.HashMap;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.oracle.qa.dataload.service.async.AsyncUtil;
import com.oracle.qa.dataload.web.rest.util.RESTUtils;

public class TagCallTask {
	private String url="http://tags.bluekai.com/site/{siteId}";
	
	String siteId;
	String phint;
	String headers;
	



	public TagCallTask(Integer siteId, String phint,String headers){
		this.siteId=siteId.toString();
		this.phint=phint;
		this.headers=headers;
	}




	public String makeTagCalls() {
		String bkuid = "";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		if(phint!=null){
			String [] phints=phint.split("\\|\\|");
			for(String temp:phints){
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
				//.setProxy(new HttpHost("www-proxy.us.oracle.com", 80))

				.build();
		factory.setHttpClient(httpClient);
		RestTemplate restemplate2 = new RestTemplate(factory);
		HttpHeaders headers2 = new HttpHeaders();
		if(headers!=null){
			String[] headersList=headers.split("\\|\\|");
			for(String headerTemp:headersList){
				String[] keyValue=headerTemp.split(":");
				headers2.add(keyValue[0], keyValue[1]);
				
			}
			
			
			
		}
		
		HttpEntity<?> entity = new HttpEntity<>(headers2);
		ResponseEntity<String> response = restemplate2.exchange(builder.buildAndExpand(uriParams).toUri(), HttpMethod.GET, entity, String.class);
		
		if (HttpStatus.OK == response.getStatusCode()) {
			 System.out.println(AsyncUtil.getThreadName());
			
			bkuid = response.getHeaders().get("Set-Cookie").get(0).split(";")[0].replaceAll("bku=", "");
		} else   {
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


}
