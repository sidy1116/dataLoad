package com.oracle.qa.dataload.service.async.tasks;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.qa.dataload.service.dto.RadsInventoryDTO;


public class RadsInventoryCallTask {
	
	private String url = "http://rads.bluekai.com/Queen/api/v2/inventory/categories";
	String partnerId;
	Integer categoryid;
	String categoryName;
	
	
	
	public RadsInventoryCallTask(Integer categoryid,String categoryName) {
		
		this.partnerId = "2452";
		this.categoryid = categoryid;
		this.categoryName=categoryName;
	}
	public RadsInventoryCallTask(String partnerId, Integer categoryid) {
		
		this.partnerId = partnerId;
		this.categoryid = categoryid;
	}
	
	
	
	public RadsInventoryDTO makeRadsApiCalls()  {
		RadsInventoryDTO radsInventoryDTO =null;
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		Map<String,Object>  reach=null;
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy() {

			@Override
			public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
					throws ProtocolException {

				if (super.isRedirected(request, response, context)) {
					int statusCode = response.getStatusLine().getStatusCode();
					String redirectURL = response.getFirstHeader("Location").getValue();

					return true;
				}
				return false;
			}
		})
				//.setProxy(new HttpHost("www-proxy.us.oracle.com", 80))

				.build();
		factory.setHttpClient(httpClient);
		
		Map<String, String> uriParams = new HashMap<String, String>();
		builder.queryParam("categories", categoryid.toString());
		builder.queryParam("partnerId", partnerId);
		HttpHeaders headers2 = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers2);
		RestTemplate restemplate2 = new RestTemplate(factory);
		ResponseEntity<String> response = restemplate2.exchange(builder.buildAndExpand(uriParams).encode().toUri(),
				HttpMethod.GET, entity, String.class);
		
		if (HttpStatus.OK == response.getStatusCode()) {
			
			try {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = new HashMap<String, Object>();

				// convert JSON string to Map
				map = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>(){});
				reach=(Map<String, Object>) map.get("reach");
				
				radsInventoryDTO =new RadsInventoryDTO();
				radsInventoryDTO.setCatId(categoryid);
				radsInventoryDTO.setCatName(categoryName);
				radsInventoryDTO.setCount(castLongObject(reach.get(categoryid.toString())) );
				
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				formatter = formatter.withLocale( Locale.US );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
				LocalDate date = LocalDate.parse(map.get("dataset").toString(), formatter);
				radsInventoryDTO.setInventoryDate(date);
				
				

			}  catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
		
		try {
			httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return radsInventoryDTO;
	}
	
	
	
	
	
	
	public Long castLongObject(Object object) {
        Long result = 0l;
        try {
            if (object instanceof Long)
                result = ((Long) object).longValue();
            else if (object instanceof Integer) {
                result = ((Integer) object).longValue();
            } else if (object instanceof String) {
                result = Long.valueOf((String) object);
            }
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("============= cannot cast");
            // do something
        }
        return result;
    }
	
	
	
	
	
	
	
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public Integer getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}
	
	
	
	
	
}
