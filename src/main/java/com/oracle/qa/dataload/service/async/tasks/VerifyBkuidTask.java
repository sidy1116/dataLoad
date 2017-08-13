package com.oracle.qa.dataload.service.async.tasks;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class VerifyBkuidTask {
	private String url = "https://bkauth.bluekai.com/bk_auth/login";
	private String DECODER_URL = "http://tags.bluekai.com/decode_cookies?bkuuid=";
	private String bkuid ;
	private String id ;
	private String password ;
	private String categoryID ;
	private String prefix=">";
	private String postfix="&nbsp;<";		
	
	private String cookie;
	public String getCategoryID() {
		return categoryID;
	}


	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}




	public VerifyBkuidTask(String bkuid, String id, String password, String categoryID, String cookie) {
		super();
		this.bkuid = bkuid;
		this.id = id;
		this.password = password;
		this.categoryID = categoryID;
		this.cookie=cookie;

	}


	public String getBkuid() {
		return bkuid;
	}


	public void setBkuid(String bkuid) {
		this.bkuid = bkuid;
	}


	public String getLoginCookie(){
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		
		Map<String, String> uriParams = new HashMap<String, String>();
		

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		factory.setHttpClient(httpClient);
		RestTemplate restemplate2 = new RestTemplate(factory);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("id", id);
		map.add("passwd", password);
		map.add("done", DECODER_URL+bkuid);

		HttpEntity<?> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> response = restemplate2.exchange(builder.buildAndExpand(uriParams).encode().toUri(),
				HttpMethod.POST, entity, String.class);
		String cookie= response.getHeaders().getFirst("Set-Cookie");
		return cookie;
		
		
	}
	
	public String getCookie() {
		return cookie;
	}


	public void setCookie(String cookie) {
		this.cookie = cookie;
	}


	
}
