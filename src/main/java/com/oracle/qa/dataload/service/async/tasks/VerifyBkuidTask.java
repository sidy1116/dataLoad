package com.oracle.qa.dataload.service.async.tasks;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
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
	
	public VerifyBkuidTask(String bkuid, String id, String password) {
		super();
		this.bkuid = bkuid;
		this.id = id;
		this.password = password;
	}


	public String getBkuid() {
		return bkuid;
	}


	public void setBkuid(String bkuid) {
		this.bkuid = bkuid;
	}


	public String verifyBkuid(){
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		
		Map<String, String> uriParams = new HashMap<String, String>();
		

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
				//.setProxy(new HttpHost("www-proxy.us.oracle.com", 80))

				.build();
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
		if (HttpStatus.OK == response.getStatusCode()) {
			
			System.out.println("RESPONSE==>"+ response.getBody());
			return bkuid +"  || VERIFIED";
		}else return "NOT VERIFIED";
		
		
		
		
	}
	
}
