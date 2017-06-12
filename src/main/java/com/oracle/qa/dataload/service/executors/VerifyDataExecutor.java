package com.oracle.qa.dataload.service.executors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class VerifyDataExecutor {

	
	private TaskExecutor taskExecutor;
	private String url;
	@Autowired
	RestTemplate restTemplate;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public VerifyDataExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	private class VerifyTagCallTask implements Runnable {

		private String bkuid;

		
		
		public VerifyTagCallTask(String bkuid) {
			this.bkuid = bkuid;
			
		}

		public void run() {

			ResponseEntity<String> response = restTemplate.getForEntity(url+bkuid, String.class);

			if (HttpStatus.OK == response.getStatusCode()) {

				 System.out.println(response.getHeaders());
				 System.out.println("RESPONSE BODY"+ response.getBody());
			} else {
				 System.out.println("RESPONSE BODY"+ response.getBody());
			}

		}

	}
	
	

	public void verifyTagCalls(String sFileName, String url1) {
		
		setUrl(url1);
		
		 try {
	            BufferedReader oReader = new BufferedReader(new FileReader(sFileName));
	            String sLine, sContent = "";
	            while ((sLine=oReader.readLine()) != null) {
	            	System.out.println(sLine);
	    			taskExecutor.execute(new VerifyTagCallTask(sLine));

	            }
	            oReader.close();
	            
	        }
	        catch (IOException oException) {
	            throw new IllegalArgumentException("Invalid file path/File cannot be read: \n" + sFileName);
	        }
		
	}
	

	
	
}
