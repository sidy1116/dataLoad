 package com.oracle.qa.dataload.web.rest.util;
 
 
 public class Cookie
 {
   private String name;
   
   private String value;
   private String path;
   private String domain;
   private String expires;
   private String version;
   
   public Cookie(String name, String value, String path, String domain, String expires)
   {
     this.name = name;
     this.value = value;
     this.path = path;
     this.domain = domain;
     this.expires = expires;
     version = "1";
   }
   
   public Cookie(String name, String value, String path, String domain, String expires, String version) {
     this.name = name;
     this.value = value;
     this.path = path;
     this.domain = domain;
     this.expires = expires;
     
     if (version == null) {
       this.version = "1";
     } else
       this.version = version;
   }
   
   public String getName() {
     return name;
   }
   
   public String getValue() { return value; }
   
   public String getPath() {
     return path;
   }
   
   public String getDomain() { return domain; }
   
   public boolean isExpired() {
     return false;
   }
   
   public int getVersion() { return Integer.valueOf(version).intValue(); }
   
   public String toString()
   {
     return name + "=" + value;
   }
 }

/* Location:           /Users/siddhant.choudhary/.m2/repository/com/oracle/qa/automation_framework_core/3.9.14/automation_framework_core-3.9.14.jar
 * Qualified Name:     com.oracle.qa.automation.framework.rest.Cookie
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */