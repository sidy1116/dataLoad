package com.oracle.qa.dataload.service.async.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.oracle.qa.dataload.domain.enumeration.IdType;
import com.oracle.qa.dataload.service.async.AsyncUtil;

@Service
public class TagCallBomber {
	
	private String url = "http://tags.bluekai.com/site/{siteId}";

	
	@Retryable(value = { IOException.class,HttpServerErrorException.class,NullPointerException.class }, maxAttempts = 3,backoff = @Backoff(delay = 1000))
	public String makeTagCalls(TagCallTask tagCallTask) {
		String bkuid = "";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		String returnID="";
		switch (tagCallTask.getIdType()) {
		case bkuuid:
			
			break;
		case adid:
			String adid =getRandomHexString(8) + "-" + getRandomHexString(4) + "-" + getRandomHexString(4) + "-" +getRandomHexString(4) + "-" +getRandomHexString(12);
			builder.queryParam("adid", adid);
			returnID=adid;
			break;
		case idfa:
			String idfa = getRandomHexString(8) + "-" + getRandomHexString(4) + "-" + getRandomHexString(4) + "-" +getRandomHexString(4) + "-" +getRandomHexString(12);
			builder.queryParam("idfa", idfa);
			returnID=idfa;
			break;
		case e_id_m:
			returnID  =getRandomHexString(32);
			builder.queryParam("e_id_m",returnID );
			break;
		case e_id_s:
			returnID  =getRandomHexString(64);
			builder.queryParam("e_id_s", returnID);
			break;
		case p_id_m:
			returnID  =getRandomHexString(32);
			builder.queryParam("p_id_m", returnID);
			break;
		case p_id_s:
			returnID  =getRandomHexString(64);
			builder.queryParam("p_id_s", returnID);
			break;
		}

		if (tagCallTask.getPhint() != null) {
			String[] phints = tagCallTask.getPhint().split("\\|\\|");
			for (String temp : phints) {
				builder.queryParam("phint", temp);
			}
		}
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("siteId", tagCallTask.getSiteId());

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy() {

			@Override
			public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
					throws ProtocolException {

				if (super.isRedirected(request, response, context)) {

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
		if (tagCallTask.getHeaders() != null && !tagCallTask.getHeaders().equalsIgnoreCase("")) {
			String[] headersList = tagCallTask.getHeaders().split("\\|\\|");

			for (String headerTemp : headersList) {
				String[] keyValue = headerTemp.split(":");
				headers2.add(keyValue[0], keyValue[1]);

			}

		}
		try{
			
		String bku=null;

		HttpEntity<?> entity = new HttpEntity<>(headers2);
		ResponseEntity<String> response = restemplate2.exchange(builder.buildAndExpand(uriParams).encode().toUri(),
				HttpMethod.GET, entity, String.class);
		if (HttpStatus.OK == response.getStatusCode()) {
			System.out.println(AsyncUtil.getThreadName());

			if(tagCallTask.getIdType().equals(IdType.bkuuid)){
				for(String values:response.getHeaders().getValuesAsList("Set-Cookie")){
					if(values.contains("bku=") && (values.indexOf(";", values.indexOf("bku=")-values.indexOf("bku="))>15)){
						bku=	values.substring(values.indexOf("bku="), values.indexOf(";", values.indexOf("bku=")));
						bku=bku.substring(bku.indexOf("=")+1);
					}
				}
				

				bkuid = bku;

			}
			else{
				for(String values:response.getHeaders().getValuesAsList("Set-Cookie")){
					if(values.contains("bku=") && (values.indexOf(";", values.indexOf("bku=")-values.indexOf("bku="))>15)){
						bku=values.substring(values.indexOf("bku="), values.indexOf(";", values.indexOf("bku=")));
						bku=bku.substring(bku.indexOf("=")+1);
					}
				}


				//bkuid =	response.getHeaders().get("Set-Cookie").get(0).split(";")[0].replaceAll("bku=", "") +"  ||  "+returnID;
				bkuid=bku+"  ||  "+returnID;
			}
		} else {
			bkuid = "BKU NOT FOUND";
		}
		
		httpClient.close();
		}catch(IOException io){
			System.out.println("IO Exception");
		}
		
		return bkuid;

	}
	
	
	String getRandomHexString(int numchars) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < numchars) {
			sb.append(Integer.toHexString(r.nextInt()));
		}

		return sb.toString().substring(0, numchars);
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
