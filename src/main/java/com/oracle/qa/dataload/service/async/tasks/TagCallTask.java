package com.oracle.qa.dataload.service.async.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.oracle.qa.dataload.domain.enumeration.IdType;
import com.oracle.qa.dataload.service.async.AsyncUtil;

public class TagCallTask {

	private String url = "http://tags.bluekai.com/site/{siteId}";

	String siteId;
	String phint;
	String headers;
	IdType idType;

	public TagCallTask(Integer siteId, String phint, String headers, IdType type) {
		this.siteId = siteId.toString();
		this.phint = phint;
		this.headers = headers;
		this.idType = type;
	}

	@Retryable(value = { IOException.class,HttpServerErrorException.class,NullPointerException.class }, maxAttempts = 5,backoff = @Backoff(delay = 1000))
	public String makeTagCalls() {
		String bkuid = "";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		String returnID = "";
		switch (idType) {
		case bkuuid:

			break;
		case adid:
			String adid = getRandomHexString(8) + "-" + getRandomHexString(4) + "-" + getRandomHexString(4) + "-"
					+ getRandomHexString(4) + "-" + getRandomHexString(12);
			builder.queryParam("adid", adid);
			returnID = adid;
			break;
		case idfa:
			String idfa = getRandomHexString(8) + "-" + getRandomHexString(4) + "-" + getRandomHexString(4) + "-"
					+ getRandomHexString(4) + "-" + getRandomHexString(12);
			builder.queryParam("idfa", idfa);
			returnID = idfa;
			break;
		case e_id_m:
			returnID = getRandomHexString(32);
			builder.queryParam("e_id_m", returnID);
			break;
		case e_id_s:
			returnID = getRandomHexString(64);
			builder.queryParam("e_id_s", returnID);
			break;
		case p_id_m:
			returnID = getRandomHexString(32);
			builder.queryParam("p_id_m", returnID);
			break;
		case p_id_s:
			returnID = getRandomHexString(64);
			builder.queryParam("p_id_s", returnID);
			break;
		}

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
					// int statusCode =
					// response.getStatusLine().getStatusCode();
					// String redirectURL =
					// response.getFirstHeader("Location").getValue();

					return true;
				}
				return false;
			}
		})
				// .setProxy(new HttpHost("www-proxy.us.oracle.com", 80))

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
		try {

			HttpEntity<?> entity = new HttpEntity<>(headers2);
			ResponseEntity<String> response = restemplate2.exchange(builder.buildAndExpand(uriParams).encode().toUri(),
					HttpMethod.GET, entity, String.class);
			if (HttpStatus.OK == response.getStatusCode()) {
				System.out.println(AsyncUtil.getThreadName());

				if (idType.equals(IdType.bkuuid))
					bkuid = response.getHeaders().get("Set-Cookie").get(0).split(";")[0].replaceAll("bku=", "");
				else {
					bkuid = response.getHeaders().get("Set-Cookie").get(0).split(";")[0].replaceAll("bku=", "")
							+ "  ||  " + returnID;
				}
			} else {
				bkuid = "BKU NOT FOUND";
			}

			httpClient.close();
		} catch (IOException io) {
			System.out.println("IO Exception");
		}catch (NullPointerException np) {
			System.out.println("NullPointerException");
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

	String getRandomHexString(int numchars) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < numchars) {
			sb.append(Integer.toHexString(r.nextInt()));
		}

		return sb.toString().substring(0, numchars);
	}


	public String getPhint() {
		return phint;
	}

	public void setPhint(String phint) {
		this.phint = phint;
	}

	public IdType getIdType() {
		return idType;
	}

	public void setIdType(IdType idType) {
		this.idType = idType;
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
