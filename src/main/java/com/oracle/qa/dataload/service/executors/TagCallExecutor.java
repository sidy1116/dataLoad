package com.oracle.qa.dataload.service.executors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class TagCallExecutor {
	private String url;
	private TaskExecutor taskExecutor;
	@Autowired
	RestTemplate restTemplate;
	
	private class MakeTagCallTask implements Runnable {

		private String quantity;
		final String regex = "bku=(.+?);";
		final Pattern pattern = Pattern.compile(regex);

		public MakeTagCallTask(String quantity) {
			this.quantity = quantity;
		}
		

		public void run() {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

			CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy() {

				@Override
				public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
						throws ProtocolException {

					System.out.println(response);

					// If redirect intercept intermediate response.
					if (super.isRedirected(request, response, context)) {
						int statusCode = response.getStatusLine().getStatusCode();
						String redirectURL = response.getFirstHeader("Location").getValue();
						System.out.println("RedirectURL:  " + redirectURL);

						return true;
					}
					return false;
				}
			})

					.build();
			factory.setHttpClient(httpClient);
			RestTemplate restemplate2=new RestTemplate(factory);
			ResponseEntity<String> response = restemplate2.exchange(url, HttpMethod.GET, null, String.class);
			System.out.println(response.getStatusCodeValue());
			if (HttpStatus.OK == response.getStatusCode()) {
				System.out.println("Cookie"+response.getHeaders().get("Set-Cookie"));
				
			} else {
				// log error, retry or ?
			}

		}

	}

	public TagCallExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void makeTagCalls(Integer quantity, String url1) {
		int i = 0;
		setUrl(url1);
		while (i < quantity) {

			taskExecutor.execute(new MakeTagCallTask("Message" + i));
			i++;
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public static synchronized void appendContents(String sFileName, String sContent) {
		try {

			File oFile = new File(sFileName);
			if (!oFile.exists()) {
				oFile.createNewFile();
			}
			if (oFile.canWrite()) {
				BufferedWriter oWriter = new BufferedWriter(new FileWriter(sFileName, true));
				oWriter.write(sContent + "\n");
				oWriter.close();
			}

		} catch (IOException oException) {
			throw new IllegalArgumentException("Error appending/File cannot be written: \n" + sFileName);
		}
	}
}