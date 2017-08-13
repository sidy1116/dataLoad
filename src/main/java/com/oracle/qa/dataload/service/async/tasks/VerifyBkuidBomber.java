package com.oracle.qa.dataload.service.async.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class VerifyBkuidBomber {
	private String DECODER_URL = "http://tags.bluekai.com/decode_cookies?bkuuid=";
	

	private String prefix=">";
	private String postfix="&nbsp;<";		
	

	@Retryable(value = { IOException.class,HttpServerErrorException.class,NullPointerException.class }, maxAttempts = 5,backoff = @Backoff(delay = 1000))
	public String verifyBkuid(VerifyBkuidTask verifyBkuidTask){
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(DECODER_URL+verifyBkuidTask.getBkuid());
		
		Map<String, String> uriParams = new HashMap<String, String>();
		

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
				//.setProxy(new HttpHost("www-proxy.us.oracle.com", 80))

				.build();
		factory.setHttpClient(httpClient);
		RestTemplate restemplate2 = new RestTemplate(factory);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", verifyBkuidTask.getCookie());
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		
		HttpEntity<?> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> response = restemplate2.exchange(builder.buildAndExpand(uriParams).encode().toUri(),
				HttpMethod.POST, entity, String.class);
		if (HttpStatus.OK == response.getStatusCode()) {
			
			if(response.getBody().contains(prefix+verifyBkuidTask.getCategoryID()+postfix)){
				return verifyBkuidTask.getBkuid() +"  || VERIFIED";
			}
			
			
			return verifyBkuidTask.getBkuid() +"  || NOT VERIFIED" ;
		}else return "NOT VERIFIED";
		
		
		
		
	}

	@Recover
    String recover(IOException e){
		System.out.println("FAILED AFTER RETRY");
		return "RECOVER/FAILED AFTER RETRY";
 }

	@Recover
    String recover(HttpServerErrorException e){
		System.out.println("FAILED AFTER RETRY");
		return "RECOVER/FAILED AFTER RETRY";
 }

	@Recover
    String recover(NullPointerException e){
		System.out.println("Cookie Not Found");
		return "RECOVER/Cookie Not Found";
 }
}
