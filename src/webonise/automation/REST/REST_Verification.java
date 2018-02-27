package webonise.automation.REST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.testng.Assert;

import webonise.automation.core.CommonUtility;

public class REST_Verification {
	JSONObject jsonResponse,jsonObject=null;
	JSONArray jsonArray=new JSONArray();
	public REST_Verification(){
	}
	
	/*
	 @Method = retrieve_AuthToken
	 @description = Method to retrieve authentication token from the response body
	 @param	= 1. resonse - Response generated after invoking URL
	 @return = accessToken for further execution	
	*/
	public String retrieveAuthToken(String response) throws JSONException 
	{
		jsonResponse = new JSONObject(response);
		String accessToken=jsonResponse.getString("authToken");
		return accessToken;

	}
	
	public String retrievefinalPScore(String response) throws JSONException 
	{
		try{
		jsonResponse = new JSONObject(response);
		JSONObject finalPScoreObject=jsonResponse.getJSONObject("algoOutput");
		JSONArray finalPScore=finalPScoreObject.getJSONArray("finalPScore");
		return finalPScore.toString();
		}catch(Exception ex){
			System.out.println("error "+ex);
			return "";
		}
	}
	
	public String retrieveOrgId(String response) throws JSONException 
	{
		try{
		jsonResponse = new JSONObject(response);
		int presentOrgId = 0;
		JSONArray userOrganisations=jsonResponse.getJSONArray("userOrganisations");
		for(int n = 0; n < userOrganisations.length(); n++)
		{
		    JSONObject object = userOrganisations.getJSONObject(n);
		    presentOrgId=object.getInt("orgId");
		    n=userOrganisations.length();
		}
		return Integer.toString(presentOrgId);
		}catch(Exception ex){
			System.out.println("error "+ex);
			return "";
		}

	}
	public String retrievePeriodAvgScore(String response) throws JSONException 
	{
		try{
		jsonResponse = new JSONObject(response);
		JSONObject periodAvgScoreObject=jsonResponse.getJSONObject("algoOutput");
		JSONArray periodAvgScore=periodAvgScoreObject.getJSONArray("periodAvgScore");
		return periodAvgScore.toString();
		}catch(Exception ex){
			System.out.println("error "+ex);
			return "";
		}

	}
	
	public int retrieveTotalCount(String response) throws JSONException 
	{
		try{
		jsonResponse = new JSONObject(response);
		JSONObject allData=jsonResponse.getJSONObject("data");
		int periodAvgScore=allData.getInt("total");
		return periodAvgScore;
		}catch(Exception ex){
			System.out.println("error "+ex);
			return 0;
		}

	}
	
	public List retrieveRecentSearch(String response) throws JSONException 
	{
		try{
		jsonResponse = new JSONObject(response);
		JSONArray allData=jsonResponse.getJSONArray("data");
		
		int totalSearches=allData.length();
		List<String> dataList=new ArrayList<String>();
		dataList.add(Integer.toString(totalSearches));
		for(int repeatSearch=0;repeatSearch<totalSearches;repeatSearch++){
			
			
			/*JSONObject json  = new JSONObject(allData);

			JSONArray searchParams1= json.getJSONArray("search_params");
			
			JSONObject searchParams = new JSONObject();
			searchParams.put("arrayName",searchParams1);*/
			
			JSONObject searchObject=(JSONObject) allData.get(repeatSearch);
			
			JSONObject searchParams=searchObject.getJSONObject("search_params");
			
			
			String searchData="";
			Iterator<?> keyIterator=searchParams.keys();
			while(keyIterator.hasNext()){
				String key=(String)keyIterator.next();
				if(key.equalsIgnoreCase("text")){
					
					searchData+=searchParams.getString("text").trim();
				}
				else if(key.equalsIgnoreCase("location")){
					JSONObject location=searchParams.getJSONObject("location");
					String radius=location.getString("radius");
					double lon=location.getJSONObject("coordinates").getDouble("lon");
					double lat=location.getJSONObject("coordinates").getDouble("lat");
					String accountCountResult=location.getString("accountCountResult");
					String userCountResult=location.getString("userCountResult");
					searchData+=key+" "+"Within:"+radius+"Radius:"+radius+"Lon:"+lon+"Lat:"+lat+"AccountCountResult:"+accountCountResult+"UserCountResult:"+userCountResult;
				}
				else{
					try{
						
						JSONArray arrayKey=searchParams.getJSONArray(key);
						searchData+=key+":";
						for(int arrayIterator=0;arrayIterator<arrayKey.length();arrayIterator++){
							searchData+=arrayKey.getString(arrayIterator).trim();
						}
					}
					catch(Exception e){
						
						if(key.equalsIgnoreCase("save_search")){
							
						}
						else
						{
						JSONObject objectKeyList=searchParams.getJSONObject(key);
						//searchData+=key+":";
						Iterator<?> objectKeyIterator=objectKeyList.keys();
						while(objectKeyIterator.hasNext()){
							String objectKey=(String)objectKeyIterator.next();
							JSONArray arrayKey=objectKeyList.getJSONArray(objectKey);
							searchData+=objectKey+":";
							for(int arrayIterator=0;arrayIterator<arrayKey.length();arrayIterator++){
								searchData+=arrayKey.getString(arrayIterator).trim();
							}
							}
						}
					}
				}
			}
			dataList.add(searchData);
		}
		return dataList;
		}catch(Exception ex){
			System.out.println("error "+ex);
			return null;
		}

	}
	
	public HashMap retrieveUserData(String response) throws JSONException 
	{
		try{
		jsonResponse = new JSONObject(response);
		JSONObject allData=jsonResponse.getJSONObject("data");
		JSONArray hitsArray=allData.getJSONArray("hits");
		JSONObject firstUserData=(JSONObject) hitsArray.get(0);
		JSONObject sourceData=firstUserData.getJSONObject("_source");
		HashMap<String,String> userData=new HashMap<String,String>();
		
		String name=sourceData.getString("FirstName");
		name+=" "+sourceData.getString("LastName");
		userData.put("Name",name);
		String email;
		try{
		 email=sourceData.getString("Email");
		}
		catch(Exception e){
			email="null";
		}
		userData.put("Email",email);
		String phone;
		try{
		 phone=sourceData.getString("Phone");
		}
		catch(Exception e){phone="null";}
		userData.put("Phone",phone);
		String aboutMe;
		try{
			aboutMe=sourceData.getString("about_me");
		}
		catch(Exception e){aboutMe="null";}
		userData.put("AboutMe",aboutMe);
		JSONArray socialLinks = null;
		String socialLinksString="";
		try{
			socialLinks=sourceData.getJSONArray("social_links");
			for(int i=0;i<socialLinks.length();i++){
				socialLinksString+=socialLinks.getString(i);
			}
		}
		catch(Exception e){socialLinksString="null";}
		userData.put("SocialLinks",socialLinksString);
		String industry;
		try{
			industry=sourceData.getString("industry");
		}
		catch(Exception e){industry="null";}
		userData.put("Industry",industry);
		String areaFocus;
		try{
			areaFocus=sourceData.getString("expertise");
			if(areaFocus.equals(""))
				areaFocus="null";
		}
		catch(Exception e){areaFocus="null";}
		userData.put("AreaFocus",areaFocus);
		JSONObject productExpertise=sourceData.getJSONObject("red_hat_product_family");
		Iterator<?> productExpertiseKeys=productExpertise.keys();
		String productExpertiseString="";
		while(productExpertiseKeys.hasNext()){
			String key=(String)productExpertiseKeys.next();
			String data=productExpertise.getString(key);
			productExpertiseString+=";"+data;
		}
		while(productExpertiseString.startsWith(";")){
			productExpertiseString=productExpertiseString.substring(1);
		}
		userData.put("ProductExpertise",productExpertiseString);
		JSONArray locationArray=null;
		String countryData="";
		try{
			locationArray=sourceData.getJSONArray("locations");
			JSONObject countryObject=locationArray.getJSONObject(0);
			String street=countryObject.getString("street");
			String city=countryObject.getString("city");
			String state=countryObject.getString("state");
			String country=countryObject.getString("country");
			countryData=street+";"+city+";"+state+";"+";"+country;
			countryData.replaceAll(";;", ";");
			countryData.replaceAll(";;;", ";");
			countryData.replaceAll(";;;;", ";");
			while(countryData.startsWith(";")){
				countryData=countryData.substring(1);
			}
		}
		catch(Exception e){countryData="null";}
		userData.put("Country",countryData);
		String accerdationName;
		try{
			 accerdationName=sourceData.getString("Accreditation_Name__c");
		}
		catch(Exception e){accerdationName="null";		}
		userData.put("Accerdation",accerdationName);
		return userData;
		}catch(Exception ex){
			System.out.println("error "+ex);
			HashMap<String,String> userData=null;
			return userData;
		}

	}
	/////////////////////////////////////////////////////
	public HashMap retrieveUserData_foronboarding(String response) throws JSONException 
	{
		try{
		jsonResponse = new JSONObject(response);
		
		
		
		JSONArray allData=jsonResponse.getJSONArray("data");
		
		HashMap<String,String> userData=new HashMap<String,String>();
		
		
		
		for(int dealsIterator=0;dealsIterator<allData.length();dealsIterator++){
			JSONArray entries = allData.getJSONObject(dealsIterator).names();
			if(entries.toString().contains("_id")){
				String id=allData.getJSONObject(dealsIterator).getString("_id");
				String status=allData.getJSONObject(dealsIterator).getString("status");
				JSONObject sourceData=allData.getJSONObject(dealsIterator).getJSONObject("deal");
				JSONArray objectKeys = sourceData.names();
				String email="null";
				
				String name=sourceData.getString("FirstName");
				name+=" "+sourceData.getString("LastName");
				userData.put("Name",name);
				
				try{
				 email=sourceData.getString("Email");
				}
				catch(Exception e){
					email="null";
				}
				userData.put("Email",email);
				String phone;
				try{
				 phone=sourceData.getString("Phone");
				}
				catch(Exception e){phone="null";}
				userData.put("Phone",phone);
				String Company;
				try{
					Company=sourceData.getString("Company");
					}
					catch(Exception e){Company="null";}
				userData.put("Company",Company);
				
				}
				
			}
		
		
				
		
		return userData;
		}catch(Exception ex){
			System.out.println("error "+ex);
			HashMap<String,String> userData=null;
			return userData;
		}

	}
	
	///////////////////////////////////////////////////
	
	public HashMap retrieveDealsData(String response) throws JSONException 
	{
		try{
			jsonResponse = new JSONObject(response);
			JSONArray allData=jsonResponse.getJSONArray("data");
			HashMap<String,String> dealsData=new HashMap<String,String>();
			for(int dealsIterator=0;dealsIterator<allData.length();dealsIterator++){
				JSONArray entries = allData.getJSONObject(dealsIterator).names();
				if(entries.toString().contains("_id")){
					String id=allData.getJSONObject(dealsIterator).getString("_id");
					String status=allData.getJSONObject(dealsIterator).getString("status");
					JSONObject dataObject=allData.getJSONObject(dealsIterator).getJSONObject("deal");
					JSONArray objectKeys = dataObject.names();
					String email="null";
					if(objectKeys.toString().contains("Email")){
						email=dataObject.getString("Email");
					}
					dealsData.put(id, email+"#"+status);
				}
			}
			return dealsData;
		}catch(Exception e){
			System.out.println("error "+e);
			HashMap<String,String> dealsData=null;
			return dealsData;
		}
		
	}
	/*
	 @Method = updateURL
	 @description = Method to update URL which is to be invoked
	 @param	= 1. urlToInvoke - Basic url which is to be invoked
	 		  2. commonHashMap - Values for newUrl which has to be replaced
	 @return = newUrlToInvoke - Updated url for invocation	
	*/
	public String updateURL(String urlToInvoke,HashMap<String, String> commonHashMap) 
	{
		String newUrlToInvoke=urlToInvoke;
		if(urlToInvoke.contains("#")) {
			newUrlToInvoke=newUrlToInvoke.replace("#","&");
		}
		if(urlToInvoke.contains("{userId}")) {
			newUrlToInvoke=newUrlToInvoke.replace("{userId}",commonHashMap.get("userId"));
		} 
		if (urlToInvoke.contains("{username}")) {
			newUrlToInvoke=newUrlToInvoke.replace("{username}",commonHashMap.get("username"));
		}
		if (urlToInvoke.contains("{organizationId}")) {
			newUrlToInvoke=newUrlToInvoke.replace("{organizationId}",commonHashMap.get("organizationId"));
		}
		if (urlToInvoke.contains("{roleId}")) {
			newUrlToInvoke=newUrlToInvoke.replace("{roleId}",commonHashMap.get("roleId"));
		}
		if (urlToInvoke.contains("{geofenceId}")) {
			newUrlToInvoke=newUrlToInvoke.replace("{geofenceId}",commonHashMap.get("geofenceId"));
		}
		if (urlToInvoke.contains("{userToken}")) {
			newUrlToInvoke=newUrlToInvoke.replace("{userToken}",commonHashMap.get("userToken"));
		}
		System.out.println(""+newUrlToInvoke);
		return newUrlToInvoke;
		
	}
	
	/*
	 @Method = updatePOSTRequestBody
	 @description = Method to update request body
	 @param	= 1. requestBody - Request body which is to be updated
	 @return = requestBody - Updated request body for execution
	*/
	public String updatePOSTRequestBody(String requestBody) throws JSONException
	{
		String newRequestBody=requestBody;
		if(requestBody.contains("#phoneNumber")) {
			newRequestBody=requestBody.replace("#phoneNumber", CommonUtility.dateFormat("phoneNumber"));
		} 
		return newRequestBody;

	}

	public int getUserId(String responseBody,String expectedUserName) throws JSONException 
	{
		String userNameFromResponseBody=null;
		int userID=0;
		jsonResponse=new JSONObject(responseBody);
		jsonArray=jsonResponse.getJSONArray("data");
		for(int i=0;i<jsonArray.length();i++) {
			jsonObject=jsonArray.getJSONObject(i);
			userNameFromResponseBody=jsonObject.getString("username");
			if(userNameFromResponseBody.equalsIgnoreCase(expectedUserName)) {
				userID=jsonObject.getInt("id");
				break;
			}
		}
		return userID;

	}

	public int getAttributeValues(String responseBody,String expectedUserName,String[] attributeValueToRetrieve) throws JSONException 
	{
		//String userNameFromResponseBody=null;
		int userID=0;
		String res[];
		jsonResponse=new JSONObject(responseBody);
		res=JSONObject.getNames(jsonResponse);
		for(int loopCounter=0;loopCounter<res.length;loopCounter++) {
			if(jsonResponse.get(res[loopCounter]).toString().equals(expectedUserName)) {
			for(int i=0;i<attributeValueToRetrieve.length;i++) {
				
			}
			}
		}
		return userID;

	}
	/**
	 * verifyJSONResponse           This method verifies the JSON response
	 * @param ActualResponse		Generated response after executing the method
	 * @param ExpectedStatus		Expected status code for verification
	 * @param ExpectedMessage		Expected message for verification
	 * @return boolean				true - If status and message are equal 
	 * 								false - If status and message are not equal
	 * @throws Exception
	 */
	public boolean verifyJSONResponse(String ActualResponse,String ExpectedStatus,String ExpectedMessage)
	{
		boolean isVerified=false;
		try {
			JSONObject actualResponseObject = new JSONObject(ActualResponse);
			JSONObject  obj = actualResponseObject.getJSONObject("response");
			String messageOutput=obj.getString("message");
			String expectedStatusOutput=obj.getInt("status")+"";
			if(messageOutput.equalsIgnoreCase(ExpectedMessage.trim()) && ExpectedStatus.equalsIgnoreCase(expectedStatusOutput)) {
				isVerified=true;
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return isVerified;
	}

	/**
	 * verifyJSONResponse_LATAS     This method verifies the JSON response for latas
	 * @param ActualResponse		Generated response after executing the method
	 * @param ExpectedStatus		Expected status code for verification
	 * @param ExpectedMessage		Expected message for verification
	 * @return boolean				true - If status and message are equal 
	 * 								false - If status and message are not equal
	 * @throws Exception
	 */
	public boolean verifyJSONResponse_LATAS(String ActualResponse,String ExpectedValueForAttribute)
	{
		boolean isVerified=false;
		try {
			JSONObject actualResponseObject = new JSONObject(ActualResponse);
			String expectedStatusOutput=actualResponseObject.getBoolean("status")+"";
			if(ExpectedValueForAttribute.equalsIgnoreCase(expectedStatusOutput)) {
				isVerified=true;
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return isVerified;
	}

	/*************************************************************************************************************************************
	  Verification of jsonArray->JsonObjects
	  [
	{
		color: "red",
		value: "#f00"
	},
	{
		color: "green",
		value: "#0f0"
	}
]
	 **************************************************************************************************************************************/
	public boolean verifyJSONArrayWithJSONObjects(String ActualResponse,String ExpectedStatus,String ExpectedMessage) throws JSONException
	{
		boolean isVerified=false;
		JSONArray actualResponseObject = new JSONArray();
		JSONObject jsonObject=new JSONObject();
		String valueToFetch;
		String[] valuesForVerification=ExpectedStatus.split(",");
		for(int i=0;i<actualResponseObject.length();i++) 
		{
			jsonObject=actualResponseObject.getJSONObject(i);
			valueToFetch=jsonObject.getString("color");
			Assert.assertEquals(valuesForVerification[i], valueToFetch);
		}
		return isVerified;
	}
}
