 package com.oracle.qa.dataload.web.rest.security;
 
 import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
 
 public class SignSHA1
 {
   private Mac sha1 = null;
   private Logger logger = LoggerFactory.getLogger(SignSHA1.class); ;
   
 
 
 
 
 
 
   public SignSHA1(Logger logger, byte[] secret)
   {
     this.logger = logger;
     
     if (secret != null) {
       try {
         sha1 = Mac.getInstance("HmacSHA1");
         sha1.init(new SecretKeySpec(secret, "HmacSHA1"));
       }
       catch (Exception e) {
         logger.debug("SignSHA1 init error: " + e);
       }
       
     } else {
       logger.debug("SignSHA1 init error: input is null");
     }
   }
   
 
 
 
 
 
   public String encode(String message)
   {
     if (message == null) {
       logger.debug("Error: message to sign is null");
       return null;
     }
     
     if (sha1 == null) {
       logger.debug("Error: SignSHA256 did not initialize");
       return null;
     }
     
     return Base64.encode(sha1.doFinal(message.getBytes()));
   }
 }

