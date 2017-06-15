 package com.oracle.qa.dataload.web.rest.util;
 
 import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 
 
 
 
 public class CookieUtils
 {
  
	 private final Logger log = LoggerFactory.getLogger(CookieUtils.class); 
	 
   private static String COOKIE_FILE_EXTENTION = "_cookies";
   private static String USER_COOKIE_DIRECTORY = "user-cookies";
   
   private static Object syncObject = new Object();
   
   private int numberOfCookieUsers = 0;
   private boolean persistCookies = true;
   
   private Map<String, Map<String, String>> userCookiePropertyMap;
   
   private Map<String, List<Cookie>> userCookieMap;
   
   //private DataFileUtils dfUtils;
   
   private Logger logger;
   
 
/*   public CookieUtils(Logger logger, DataFileUtils dfUtils)
   {
     this.logger = logger;
     this.dfUtils = dfUtils;
     
     userCookiePropertyMap = new HashMap();
     userCookieMap = new HashMap();
     
     numberOfCookieUsers = loadCookies();
   }*/
   
/*   private int loadCookies()
   {
     List<String> listOfFiles = null;
     
 
     try
     {
       listOfFiles = dfUtils.getFilesInDirectory(USER_COOKIE_DIRECTORY);
     }
     catch (Exception e) {
       logger.log("Exception loading cookies: " + e);
       return -1;
     }
     
     if (listOfFiles.isEmpty())
     {
       return 0;
     }
     
 
     for (String filename : listOfFiles)
     {
 
 
       if (filename.contains("_"))
       {
 
         logger.log("loading user cookie files: " + filename);
         try
         {
           lines = dfUtils.loadListOfStrings(USER_COOKIE_DIRECTORY, filename);
         } catch (Exception e) {
           List<String> lines;
           logger.log("**** Error loading cookie file: " + USER_COOKIE_DIRECTORY + "/" + filename + " ****");
           return -1;
         }
         List<String> lines;
         if (lines.isEmpty()) {
           logger.log("** Warning empty cookie file: " + USER_COOKIE_DIRECTORY + "/" + filename + " **");
           return 0;
         }
         
 
         String[] cookie_user = filename.split("_");
         
         String user = cookie_user[0];
         
 
         cookiePropertyMap = new HashMap();
         userCookiePropertyMap.put(user, cookiePropertyMap);
         
 
         cookieList = new ArrayList();
         userCookieMap.put(user, cookieList);
         
         for (String line : lines) {
           Cookie cookie = createCookieFromProperty(line);
           
           cookieList.add(cookie);
           
           String[] cookie_key_val = getCookie_key_val(cookie);
           String cookie_key = cookie_key_val[0];
           String cookie_val = cookie_key_val[1];
           
 
           cookiePropertyMap.put(cookie_key, cookie_val); } } }
     Map<String, String> cookiePropertyMap;
     List<Cookie> cookieList;
     return listOfFiles.size();
   }*/
   
 
 
 
 
   public int getNumberOfCookieUsers()
   {
     return numberOfCookieUsers;
   }
   
 
 
 
 
   public void setPersistCookies(boolean value)
   {
     persistCookies = value;
   }
   
 
 
 
 
 
 
 
   public void persistCookies()
   {/*
     if (!persistCookies) {
       return;
     }
     for (String user : userCookiePropertyMap.keySet()) {
       Map<String, String> userCookieMap = (Map)userCookiePropertyMap.get(user);
       
       StringBuilder cookieProperties = new StringBuilder();
       
       for (String cookie_key : userCookieMap.keySet()) {
         cookieProperties.append(cookie_key);
         cookieProperties.append("=");
         cookieProperties.append((String)userCookieMap.get(cookie_key));
         cookieProperties.append("\n");
       }
       
 
       String filePath = dfUtils.getProjectAbsBaseDir() + File.separator + "src" + File.separator + "main" + File.separator + "resources" parator + USER_COOKIE_DIRECTORY;
       
 
       String fileName = user + COOKIE_FILE_EXTENTION;
       dfUtils.writeDataToFile(filePath, fileName, cookieProperties.toString());
     }
   */}
   
   private Cookie createCookieFromProperty(String propertyLine)
   {
     String[] key_val = propertyLine.split("=");
     String key = key_val[0];
     String val = key_val[1];
     
     String[] key_parts = key.split("::");
     String domain = key_parts[0];
     String path = key_parts[1];
     String name = key_parts[2];
     
     String[] val_parts = val.split(";");
     String version = val_parts[0];
     String value = val_parts[1];
     
     return new Cookie(name, value, path, domain, version);
   }
   
 
 
 
   public List<Cookie> getUserCookies(String user)
   {
     Map<String, String> cookieMap = null;
     
     synchronized (syncObject)
     {
    	 if (user==null){
    		 return new ArrayList();
    	 }else
       cookieMap = (Map)userCookiePropertyMap.get(user);
       
       if (cookieMap == null) {
         return new ArrayList();
       }
     }
     
 
     synchronized (cookieMap)
     {
       Object cookieList = (List)userCookieMap.get(user);
       
       if (cookieList == null) {
         return new ArrayList();
       }
       return (List<Cookie>)cookieList;
     }
   }
   
   private Cookie createCookie(String cookieStr)
   {
     String domain = null;
     String path = null;
     String name = null;
     String value = null;
     String version = null;
     String expires = null;
     
     String[] cookies = cookieStr.split(";");
     
     for (String cookieParts : cookies)
     {
       int index = cookieParts.indexOf("=");
       String key = cookieParts.substring(0, index).trim();
       String val = cookieParts.substring(index + 1).trim();
       
       switch (key.toLowerCase()) {
       case "domain": 
         domain = val; break;
       case "path":  path = val; break;
       case "expires":  expires = val; break;
       case "version":  version = val; break;
       
       default: 
         name = key;
         value = val;
       }
       
     }
     return new Cookie(name, value, path, domain, expires, version);
   }
   
 
 
 
   public void updateUserCookies(String user, List<String> responseCookies)
   {
     Map<String, String> cookieMap = null;
     
     List<Cookie> cookieList;
     synchronized (syncObject)
     {
       cookieMap = (Map)userCookiePropertyMap.get(user);
       
       if (cookieMap == null) {
         logger.debug("updateUserCookies no cookies for user: " + user + ", creating new cookie map");
         
         cookieMap = new HashMap();
         userCookiePropertyMap.put(user, cookieMap);
         
         cookieList = new ArrayList();
         userCookieMap.put(user, cookieList);
       }
     }
     
 
     synchronized (cookieMap)
     {
       for (String newCookie : responseCookies) {
         Cookie testCookie = createCookie(newCookie);
         
         String[] testCookie_key_val = getCookie_key_val(testCookie);
         String testCookie_key = testCookie_key_val[0];
         String testCookie_val = testCookie_key_val[1];
         
         String user_cookie_val = (String)cookieMap.get(testCookie_key);
         if ((user_cookie_val == null) || (!user_cookie_val.equals(testCookie_val)))
         {
           logger.debug("updateUserCookies, user: " + user + ", key: " + testCookie_key + ", val: " + testCookie_val);
           
 
           cookieMap.put(testCookie_key, testCookie_val);
           
           updateUserCookieList(user, testCookie, testCookie_key);
         }
       }
     }
   }
   
   private void updateUserCookieList(String user, Cookie testCookie, String testCookie_key)
   {
     List<Cookie> cookieList = (List)userCookieMap.get(user);
     
     for (int i = 0; i < cookieList.size(); i++)
     {
       String[] cookie_key_val = getCookie_key_val((Cookie)cookieList.get(i));
       String cookie_key = cookie_key_val[0];
       
 
       if (cookie_key.equals(testCookie_key)) {
         cookieList.set(i, testCookie);
         return;
       }
     }
     
     cookieList.add(testCookie);
   }
   
   private String[] getCookie_key_val(Cookie cookie)
   {
     String[] key_val = new String[2];
     
 
     StringBuilder key = new StringBuilder();
     key.append(cookie.getDomain());
     key.append("::");
     key.append(cookie.getPath());
     key.append("::");
     key.append(cookie.getName());
     
     key_val[0] = key.toString();
     
 
     StringBuilder val = new StringBuilder();
     val.append(cookie.getVersion());
     val.append(";");
     val.append(cookie.getValue());
     
     key_val[1] = val.toString();
     
     return key_val;
   }
 }

