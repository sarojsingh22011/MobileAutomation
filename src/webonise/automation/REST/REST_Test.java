package webonise.automation.REST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Assert;

public class REST_Test {
	public REST_Test(){}

	boolean text=false;
	/**
	 * executeREST_Service         
	 * @param map					map containing all data required for execution
	 * @return String				Response body is returned as a String
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String executeREST_Service(HashMap map) {
		//String certificatesTrustStorePath = "C:\\Program Files\\Java\\jre1.8.0_101/lib/security/cacerts";
		//String certificatesTrustStorePath=System.getenv("JRE_HOME")+"\\lib\\security\\cacerts";
		//C:\Program Files\Java\jre1.8.0_151
		System.out.println("****************************************************************");
		System.out.println(map);
		String certificatesTrustStorePath="C:\\Program Files\\Java\\jre1.8.0_151\\lib\\security\\cacerts";
		System.out.println(certificatesTrustStorePath);
		System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
		String actualResponse=null;
		String url = null,method=null,ExpectedStatusCode=null,REST_RequestBody=null;
		//Map<String, String> headers = new HashMap<String, String>();
		Map headers = new HashMap();
		Set set = map.entrySet();
		Iterator i = set.iterator();
		System.out.println(i);
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			String keyval=me.getKey().toString();
			System.out.println(keyval);
			switch(keyval)
			{
			case "UrlForAuthentication": url= me.getValue().toString(); 							break;
			case "Authorization":   	 headers.put("Authorization", me.getValue().toString()); 	break;
			case "x-access-token":   	 headers.put("x-access-token", me.getValue().toString()); 	break;
			case "Content_Type":   		 headers.put("Content_Type", me.getValue().toString());		break;
			case "Method": 				 method=me.getValue().toString();							break;
			case "RequestBody": 		 REST_RequestBody=me.getValue().toString(); 				break;
			case "ExpectedStatus": 		 ExpectedStatusCode=me.getValue().toString();				break;
			case "URL": 				 url= me.getValue().toString();								break;
			case "x-fh-auth-app":		 headers.put("x-fh-auth-app", me.getValue().toString()); 	break;
			case "user-id":		         headers.put("user-id", me.getValue().toString()); 			break;
			case "details":		         headers.put("details", me.getValue()); 			break;
			case "text":		         text=true;headers.put("text", me.getValue().toString()); 			break;
			
			}
		}
		try{
			System.out.println(headers);
			System.out.println(url);
			System.out.println(method);
			System.out.println(ExpectedStatusCode);
			System.out.println(REST_RequestBody);
			
			actualResponse=callHttpService(url, method, headers,ExpectedStatusCode,REST_RequestBody);
			System.out.println("===="+actualResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
		return actualResponse;
	}
	/**
	 * callHttpService           	This method executes the http method
	 * @param url					URL which is to be executed
	 * @param method				Execution method - GET/POST/PUT/DELETE
	 * @param headers				It includes multiple headers for method execution like username/password/authentication/parameters
	 * @param REST_expectedResponse Expected response after execution
	 * @param REST_RequestBody		Request body is required for POST and PUT method
	 * @return String				Response body is returned as a String
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	protected String callHttpService(String url, String method, Map<String, String> headers,String REST_expectedResponse,String REST_RequestBody)throws Exception {
		HttpClient client = null;
		HttpMethodBase httpBaseMethod  = null;
		int actualResponse = 0;
		try{
			client = new HttpClient();
		} catch(Exception e)
		{
			System.out.println("**** Exception in creating the client*****");
			e.printStackTrace();
		}
		switch(method) 
		{
		case "GET" : 	
			httpBaseMethod = new GetMethod(url);
			GetMethod getMethod=(GetMethod) httpBaseMethod;
			getMethod.setRequestHeader("Content-Type", (String)headers.get("Content_Type"));
			getMethod.setRequestHeader("user-id", (String)headers.get("user-id"));
			getMethod.setRequestHeader("x-access-token", (String)headers.get("x-access-token"));
			getMethod.setRequestHeader("x-fh-auth-app", (String)headers.get("x-fh-auth-app"));
			break;
		case "POST" :
			httpBaseMethod =new PostMethod(url);
			PostMethod postMethod = (PostMethod) httpBaseMethod;
			postMethod.setRequestHeader("Content-Type", (String)headers.get("Content_Type"));
			postMethod.setRequestHeader("user-id", (String)headers.get("user-id"));
			postMethod.setRequestHeader("x-access-token", (String)headers.get("x-access-token"));
			postMethod.setRequestHeader("x-fh-auth-app", (String)headers.get("x-fh-auth-app"));
			if(text)
				postMethod.setParameter("text", (String)headers.get("text"));
			if(REST_RequestBody!= null){
				postMethod.setRequestBody(REST_RequestBody);
			}
			break;
		case "PUT" :
			httpBaseMethod = new PutMethod(url);
			PutMethod putMethod = (PutMethod) httpBaseMethod;
			putMethod.setRequestHeader("Content-Type", (String)headers.get("Content_Type"));
			if(REST_RequestBody!= null){
				putMethod.setRequestBody(REST_RequestBody);
			}
			break;
		case "DELETE" :
			httpBaseMethod = new DeleteMethod(url);
			DeleteMethod deleteMethod = (DeleteMethod) httpBaseMethod;
			deleteMethod.setRequestHeader("Content-Type", (String)headers.get("Content_Type"));
			break;
		}
		try{
			actualResponse = client.executeMethod(httpBaseMethod);
		}catch(Exception e){
			e.printStackTrace();
		}
		/*if (actualResponse != HttpStatus.SC_OK) {
			throw new Exception("Method execution failed for method type=:: " + method + " and Response code generated :: " + actualResponse);
		} */
		String responsebody=httpBaseMethod.getResponseBodyAsString();
		System.out.println("ResponseBody="+responsebody);
		Assert.assertEquals(actualResponse, Integer.parseInt(REST_expectedResponse));
		return responsebody;
	}

}
