package com.oracle.qa.dataload.service.executors;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.oracle.qa.dataload.service.TagRequestService;
import com.oracle.qa.dataload.service.async.tasks.TagCallTask;
import com.oracle.qa.dataload.service.dto.TagRequestDTO;

@Service
public class Runner  {
	@Autowired
	 private  TagRequestService tagRequestService;
	
	@Async
	public  void useCompletableFutureWithExecutor(List<TagCallTask> tasks,TagRequestDTO tagRequestDTO) throws IOException  {
		  long start = System.nanoTime();
		  
		  List<CompletableFuture<String>> futures =
		      tasks.stream()
		           .map(t -> CompletableFuture.supplyAsync(() -> t.makeTagCalls()))
		           .collect(Collectors.toList());
		 
		  List<String> result =
		      futures.stream()
		             .map(CompletableFuture::join)
		             .collect(Collectors.toList());
		  long duration = (System.nanoTime() - start) / 1_000_000;
		  System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
		  System.out.println(result);
		  
		  ByteArrayOutputStream btOs = new ByteArrayOutputStream();
		  String nl = System.getProperty("line.separator");
	        try {

	            for (String bkuid:result) {
	            	btOs.write(bkuid.getBytes() );
	            	btOs.write(nl.getBytes() );
	            }
	        } finally {
	        	 tagRequestDTO.setFile(btOs.toByteArray());
	 	        tagRequestService.save(tagRequestDTO);
	        }
	       
	        
		}
		
	 
	
}
