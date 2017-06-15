package com.oracle.qa.dataload.web.rest.util;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

public abstract interface RESTUtilsI
{
  public abstract Map<String, List<String>> getHttpResponseHeaders();
  
  public abstract void addToHeaders(Map<String, String> paramMap);
  
  public abstract String getUrlParameters(Object paramObject)
    throws Exception;
  
  public abstract String getLocationInHeader();
  
  public abstract String getHttpResponseCode();
  
  public abstract String getHttpResponseContent();
  
  public abstract byte[] getHttpResponseContentBytes();
  
  public abstract String getHttpResponseContentLength();
  
  public abstract CookieUtils getCookieUtils();
  
  public abstract String getCookieUser();
  
  public abstract String getHttpErrorCode();
  
  public abstract boolean hasHttpErrorCode();
  
  public abstract void setProperty(String paramString, Object paramObject);
  
  public abstract void setHttpExpectedResponseCode(String paramString);
  
  public abstract String getHttpVerbGET();
  
  public abstract String getHttpVerbPOST();
  
  public abstract String getHttpVerbPUT();
  
  public abstract String getHttpVerbDELETE();
  
  public abstract void setHttpVerb(String paramString);
  
  public abstract void setHttpVerbGET();
  
  public abstract void setHttpVerbPOST();
  
  public abstract void setHttpVerbPUT();
  
  public abstract void setHttpVerbDELETE();
  
  public abstract void setProxy(String paramString);
  
  public abstract boolean httpGETRequest(String paramString)
    throws Exception;
  
  public abstract boolean httpDELETERequest(String paramString)
    throws Exception;
  
  public abstract boolean httpRequestWithNewResource(String paramString)
    throws Exception;
  
  public abstract boolean httpRequestWithNewResource(String paramString1, String paramString2)
    throws Exception;
  
  public abstract boolean httpRequestWithRedirect(String paramString)
    throws Exception;
  
  public abstract boolean httpRequestWithRedirect(String paramString1, String paramString2)
    throws Exception;
  
  public abstract boolean httpRequest(String paramString)
    throws Exception;
  
  public abstract void setContentForPOST(String paramString);
  
  public abstract void setContentForPOST(byte[] paramArrayOfByte);
  
  public abstract void setContentForPUT(String paramString);
  
  public abstract void setContentForPUT(byte[] paramArrayOfByte);
  
  public abstract byte[] getContentToPOST();
  
  public abstract String getContentToPOSTasString();
  
  public abstract byte[] getContentToPUT();
  
  public abstract String getContentToPUTasString();
  
  public abstract boolean loadDataFileForPOST(String paramString1, String paramString2)
    throws Exception;
  
  public abstract boolean loadDataFileForPUT(String paramString1, String paramString2)
    throws Exception;
  
  public abstract String percentEncode(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void updateOAuthParameters(String paramString1, String paramString2, Map<String, String> paramMap, String paramString3)
    throws Exception;
}