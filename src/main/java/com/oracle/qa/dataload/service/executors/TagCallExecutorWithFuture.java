package com.oracle.qa.dataload.service.executors;

import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oracle.qa.dataload.service.async.AsyncUtil;

@Service
public class TagCallExecutorWithFuture {

	public String makeTagCalls(String url1) {
		int i = 0;
		String bkuid = "";

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy() {

			@Override
			public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
					throws ProtocolException {

				// System.out.println(response);

				// If redirect intercept intermediate response.
				if (super.isRedirected(request, response, context)) {
					int statusCode = response.getStatusLine().getStatusCode();
					String redirectURL = response.getFirstHeader("Location").getValue();
					// System.out.println("RedirectURL: " + redirectURL);

					return true;
				}
				return false;
			}
		})

				.build();
		factory.setHttpClient(httpClient);
		RestTemplate restemplate2 = new RestTemplate(factory);
		ResponseEntity<String> response = restemplate2.exchange(url1, HttpMethod.GET, null, String.class);
		// System.out.println(response.getStatusCodeValue());
		if (HttpStatus.OK == response.getStatusCode()) {
			 System.out.println(AsyncUtil.getThreadName());
			
			bkuid = response.getHeaders().get("Set-Cookie").get(0).split(";")[0].replaceAll("bku=", "");
		} else   {
			bkuid = "BKU NOT FOUND";
		}

		return bkuid;

	}

	@Async
	public CompletableFuture<String> asyncMakeTagCall(String url) {

		String result = makeTagCalls(url);

		return CompletableFuture.completedFuture(result);
	}

	
}
