package com.oracle.qa.dataload.web.rest.util;


import java.util.List;
import java.util.Map;








public class JsonStructure
{
  private Map<String, Object> map = null;
  private List<?> list = null;
  
  private String prettyPrintString = "";
  private String flatJSONString = "";
  private String sourceJSON = "";
  


  public JsonStructure(List<?> list, String prettyPrintString)
  {
    this(list, prettyPrintString, "", "");
  }
  


  public JsonStructure(List<?> list, String prettyPrintString, String flatJSONString, String sourceJSON)
  {
    this.list = list;
    this.prettyPrintString = prettyPrintString;
    this.flatJSONString = flatJSONString;
    this.sourceJSON = sourceJSON;
  }
  


  public JsonStructure(Map<String, Object> map, String prettyPrintString)
  {
    this(map, prettyPrintString, "", "");
  }
  


  public JsonStructure(Map<String, Object> map, String prettyPrintString, String flatJSONString, String sourceJSON)
  {
    this.map = map;
    this.prettyPrintString = prettyPrintString;
    this.flatJSONString = flatJSONString;
    this.sourceJSON = sourceJSON;
  }
  
  public String toString()
  {
    if ((flatJSONString != null) && (!flatJSONString.isEmpty())) {
      return flatJSONString;
    }
    return getSourceJSON();
  }
  
  public String toPrettyString() {
    return prettyPrintString;
  }
  
  public String getSourceJSON() {
    return sourceJSON;
  }
  



  public boolean isList()
  {
    return list != null;
  }
  



  public boolean isMap()
  {
    return map != null;
  }
  



  public List<?> getList()
  {
    return list;
  }
  



  public Map<String, Object> getMap()
  {
    return map;
  }
  
  public void setPrettyPrintString(String prettyPrintString) {
    this.prettyPrintString = prettyPrintString;
  }
  
  public void setJSONString(String flatJSONString) {
    this.flatJSONString = flatJSONString;
  }
}