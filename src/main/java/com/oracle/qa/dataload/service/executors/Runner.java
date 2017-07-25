
package com.oracle.qa.dataload.service.executors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.oracle.qa.dataload.domain.RadsInventory;
import com.oracle.qa.dataload.service.RadsInventoryService;
import com.oracle.qa.dataload.service.ReTagProfileService;
import com.oracle.qa.dataload.service.TagRequestService;
import com.oracle.qa.dataload.service.VerifyUserTagService;
import com.oracle.qa.dataload.service.async.tasks.RadsInventoryCallTask;
import com.oracle.qa.dataload.service.async.tasks.TagCallTask;
import com.oracle.qa.dataload.service.async.tasks.VerifyBkuidTask;
import com.oracle.qa.dataload.service.dto.RadsInventoryDTO;
import com.oracle.qa.dataload.service.dto.ReTagProfileDTO;
import com.oracle.qa.dataload.service.dto.TagRequestDTO;
import com.oracle.qa.dataload.service.dto.VerifyUserTagDTO;

@Service
public class Runner  {
	@Autowired
	 private  TagRequestService tagRequestService;
	
	@Autowired
	private  ReTagProfileService reTagProfileService;
	
	@Autowired
	private Executor taskExecutor;
	
	@Autowired
	private  VerifyUserTagService verifyUserTagService;
	
	@Autowired
	private  RadsInventoryService radsInventoryService;
	
	@Async
	public  void useCompletableFutureWithExecutor(List<TagCallTask> tasks,TagRequestDTO tagRequestDTO) throws IOException  {
		  long start = System.nanoTime();
		  
		  List<CompletableFuture<String>> futures =
		      tasks.stream()
		           .map(t -> CompletableFuture.supplyAsync(() -> t.makeTagCalls(),taskExecutor))
		           .collect(Collectors.toList());
		 
		  List<String> result =
		      futures.stream()
		             .map(CompletableFuture::join)
		             .collect(Collectors.toList());
		  long duration = (System.nanoTime() - start) / 1_000_000;
		  System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
		  
		  ByteArrayOutputStream btOs = new ByteArrayOutputStream();
		  String nl = System.getProperty("line.separator");
	        try {

	            for (String bkuid:result) {
	            	btOs.write(bkuid.getBytes() );
	            	btOs.write(nl.getBytes() );
	            }
	        } finally {
	        	 tagRequestDTO.setFile(btOs.toByteArray());
	        	 tagRequestDTO.setFileContentType("text/plain");
	        	 
	        	 // validation if present is not present then dont reinset it 
	        	 
	        	 // update to bkuuid actually created 
	 	        tagRequestService.save(tagRequestDTO);
	        }
	       
	        
		}
		
	
	@Async
	public  void useCompletableFutureWithExecutor(List<TagCallTask> tasks,ReTagProfileDTO tagRequestDTO) throws IOException  {
		  long start = System.nanoTime();
		  
		  List<CompletableFuture<String>> futures =
		      tasks.stream()
		           .map(t -> CompletableFuture.supplyAsync(() -> t.makeTagCalls(),taskExecutor))
		           .collect(Collectors.toList());
		 
		  List<String> result =
		      futures.stream()
		             .map(CompletableFuture::join)
		             .collect(Collectors.toList());
		  long duration = (System.nanoTime() - start) / 1_000_000;
		  System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
		  System.out.println(result);
		 
		}
	
	
	@Async
	public  void useVerifyBkuidTaskCompletableFutureWithExecutor(List<VerifyBkuidTask> tasks,VerifyUserTagDTO verifyUserTagDTO) throws IOException  {
		  long start = System.nanoTime();
		  
		  List<CompletableFuture<String>> futures =
		      tasks.stream()
		           .map(t -> CompletableFuture.supplyAsync(() -> t.verifyBkuid(),taskExecutor))
		           .collect(Collectors.toList());
		 
		  List<String> result =
		      futures.stream()
		             .map(CompletableFuture::join)
		             .collect(Collectors.toList());
		  long duration = (System.nanoTime() - start) / 1_000_000;
		  System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
		  
		  ByteArrayOutputStream btOs = new ByteArrayOutputStream();
		  String nl = System.getProperty("line.separator");
	        try {

	            for (String bkuid:result) {
	            	btOs.write(bkuid.getBytes() );
	            	btOs.write(nl.getBytes() );
	            }
	        } finally {
	        	 verifyUserTagDTO.setOutputFile(btOs.toByteArray());
	        	 verifyUserTagDTO.setOutputFileContentType("text/plain");
	        	 verifyUserTagService.save(verifyUserTagDTO);
	        }
	       
	        
		}

	@Async
	public void fillDataForRadsInventory(ArrayList<RadsInventoryCallTask> radsInventoryCallTaskList) {
		// TODO Auto-generated method stub
		  long start = System.nanoTime();
		  boolean isFirstTimeLoad=false;
		 List<CompletableFuture<RadsInventoryDTO>> futures =
				 radsInventoryCallTaskList.stream()
			           .map(t -> CompletableFuture.supplyAsync(() -> t.makeRadsApiCalls(),taskExecutor))
			           .collect(Collectors.toList());
		 
		 List<RadsInventoryDTO> result =
			      futures.stream()
			             .map(CompletableFuture::join)
			             .collect(Collectors.toList());
		 	long duration = (System.nanoTime() - start) / 1_000_000;
		 
		 	LocalDate maxDate=radsInventoryService.getMaxDate();
		 	List<RadsInventory> rows = radsInventoryService.getDataForMaxDate(maxDate);
		 	Map<Integer, Long> mapWithOldMaxData= new HashMap<Integer, Long>();

		 	if(rows!=null && !rows.isEmpty()){
		 		for (RadsInventory row : rows) {
			 		mapWithOldMaxData.put(row.getCatId(), row.getCount());
			 	}
		 	}else isFirstTimeLoad=true;
		 	
		 	
		 	
		 	System.out.println("MAX DATE DATA");
		 	for(RadsInventoryDTO dto: result){
		 		if(maxDate!=null){
		 			if(maxDate.isBefore(dto.getInventoryDate())){
		 				//add old count
		 				if(isFirstTimeLoad) dto.setPrevInvCount(dto.getCount());
		 				else dto.setPrevInvCount(mapWithOldMaxData.get(dto.getCatId()));
		 				
		 				if(dto.getPrevInvCount()==null){
		 					dto.setPrevInvCount(0L);
		 					dto.setDiffPercentage("NA");
		 				}else{
		 					Double diff=(double) (dto.getCount()-dto.getPrevInvCount());
			 				Double diffPercent=(Double) (diff/dto.getCount()*100);
			 				dto.setDiffPercentage(String.format("%.2f", diffPercent));
		 				}
		 				radsInventoryService.save(dto);

			 		}
		 		}else {
		 			if(isFirstTimeLoad) dto.setPrevInvCount(dto.getCount());
		 			
		 			if(dto.getPrevInvCount()==null){
		 				dto.setPrevInvCount(0L);
		 				dto.setDiffPercentage("NA");
		 			}else{
		 				Long diff=dto.getCount()-dto.getPrevInvCount();
		 				Float diffPercent=(float) (diff/dto.getCount()*100.00);
		 				dto.setDiffPercentage(String.format("%.2f", diffPercent));
		 			}
		 			
		 			
		 			
		 			
		 			radsInventoryService.save(dto);
		 		}
		 		
		 	}
		 	
		 	System.out.printf("Processed %d tasks in %d millis\n", radsInventoryCallTaskList.size(), duration);
	}
	
	
}
