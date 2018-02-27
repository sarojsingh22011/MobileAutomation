package webonise.redHat.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.tools.ant.util.SymbolicLinkUtils;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import webonise.automation.REST.REST_Test;
import webonise.automation.REST.REST_Verification;
import webonise.automation.core.Configuration;
import webonise.automation.core.MobileActions;
import webonise.automation.core.WeboAutomation;
import webonise.automation.core.verification.MobileVerification;
import webonise.redHat.constants.*;

public class RH_Search extends WeboAutomation{
	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	MobileVerification mobileVerification = new MobileVerification();

	public RH_Search() throws Exception 
	{
		super();
		initalizeReport(this.getClass().getName());
		PageFactory.initElements(androidDriver,RH_Common_Constants.class);
		PageFactory.initElements(androidDriver,RH_Search_Constants.class);
		PageFactory.initElements(androidDriver,RH_Connections_Constants.class);
	} 

	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void normalSearch(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			
			
			String accessToken=ApiCall.apiCall((String)gettcdata.get("username"), (String)gettcdata.get("password"));
			System.out.println(accessToken);
			System.out.println(ApiCall.userFirstName);
			System.out.println(ApiCall.userLastName);
			System.out.println(ApiCall.userEmail);
			System.out.println(ApiCall.userPhone);
		//	System.out.println(ApiCall.socialLinks);
			System.out.println(ApiCall.userID);
			String url;
			String instance=(String)gettcdata.get("instance");
			REST_Test rest=new REST_Test();
			REST_Verification restVerify=new REST_Verification();
			if(instance.equalsIgnoreCase("stage")){
				url=Configuration.apiStageURL+(String)gettcdata.get("url");
			}
			else{
				url=Configuration.apiTestURL+(String)gettcdata.get("url");
			}
			String[] noOfSearch=((String)gettcdata.get("textToSearch")).split(";");
			List<Integer> totalSearchCount=new ArrayList<Integer>();
			HashMap<String,HashMap<String,String>> apiUserData=new HashMap<String,HashMap<String,String>>();
			for(int totalSearch=0;totalSearch<noOfSearch.length;totalSearch++){
				String searchKeyword=(noOfSearch[totalSearch].split("#")[1]);
				System.out.println(searchKeyword);
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("user-id",ApiCall.userID);
				hashMap.put("x-access-token",accessToken);
				hashMap.put("x-fh-auth-app",(String)gettcdata.get("authApp"));
				
				hashMap.put("Content_Type", (String)gettcdata.get("contentType"));
				hashMap.put("Method", (String)gettcdata.get("method"));
				hashMap.put("ExpectedStatus", (String)gettcdata.get("status"));
				hashMap.put("URL", url);
				hashMap.put("text", searchKeyword);
				String response=rest.executeREST_Service(hashMap);
				int count=restVerify.retrieveTotalCount(response);
				HashMap data=restVerify.retrieveUserData(response);
				System.out.println(""+data);
				totalSearchCount.add(count);
				apiUserData.put(Integer.toString(totalSearch),data);
			}
			System.out.println(totalSearchCount);
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			RH_Login.login(androidDriver,mobileActions,verify,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);
			String textToSearch = (String) gettcdata.get("textToSearch");
			String searchesToPerform[] = textToSearch.split(";");
			int numberOfSearchesToPerform = searchesToPerform.length;
			System.out.println(numberOfSearchesToPerform); 
			for(int searchCounter=0;searchCounter<numberOfSearchesToPerform;searchCounter++) {
				String searchString[] = searchesToPerform[searchCounter].split("#");
				String textToSearchInNaturalLanguage = searchString[1];
				String typeOfSearch = searchString[2];
				String tokenLabelsRequired = searchString[3];
				List<String> listTokenLabelsRequired = new ArrayList<String>();
				System.out.println(textToSearchInNaturalLanguage);
				System.out.println(typeOfSearch);
				System.out.println(tokenLabelsRequired);
				if(tokenLabelsRequired.contains(",")) {
					String tokenLabelsRequiredList[] = tokenLabelsRequired.split(",");
					for(int tokenLabelCounter=0;tokenLabelCounter<tokenLabelsRequiredList.length;tokenLabelCounter++) {
						System.out.println(tokenLabelsRequiredList[tokenLabelCounter]);
						listTokenLabelsRequired.add(tokenLabelsRequiredList[tokenLabelCounter]);
					}
				}
				WebElement webElement = mobileActions.getWebElement(androidDriver,RH_Search_Constants.naturalLanguageSearchTextBox);
				webElement.clear();
				mobileActions.click(androidDriver, RH_Search_Constants.naturalLanguageSearchTextBox);
				mobileActions.sendKeys(androidDriver,RH_Search_Constants.naturalLanguageSearchTextBox, textToSearchInNaturalLanguage);
				mobileActions.hideKeyboard(androidDriver);
				mobileActions.click(androidDriver,RH_Search_Constants.findConnectionsButton);

				//Total connections found
				String totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
				System.out.println("totalConnectionsFound"+totalConnectionsFound);				
				verify.verifyHeading(Integer.toString(totalSearchCount.get(searchCounter)), totalConnectionsFound);				
				List<WebElement> connectionsHeader=mobileActions.findMultipleElementsInList(androidDriver, RH_Connections_Constants.connectionsHeader);
				String connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
				verify.assertion(connectionsHeaderValue,"CONNECTIONS");
				
				mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
				mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
				mobileActions.setImplicityWait(androidDriver, 1);
				if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
					mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
					Thread.sleep(1000);
				}
				mobileActions.setImplicityWait(androidDriver, 10);
				verifyApiUserData(apiUserData.get(Integer.toString(searchCounter)));
				mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
				mobileActions.click(androidDriver, RH_Search_Constants.listView);
				mobileActions.setImplicityWait(androidDriver, 1);
				if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
					mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
					Thread.sleep(1000);
				}
				mobileActions.setImplicityWait(androidDriver, 10);
					
				if(typeOfSearch.equals("person")) {
					List<String> matchingCriteria=new ArrayList<String>();
					matchingCriteria.add(tokenLabelsRequired.trim());
					List<String> totalConnnection=mobileActions.totalListWithMatching(androidDriver,RH_Connections_Constants.nameListString,RH_Connections_Constants.nameList,RH_Connections_Constants.loaderLogo,matchingCriteria);
					if(totalConnnection.isEmpty()){
						verify.verifyHeading("Keywords matching", "Keywords didn't match");
					}
					if(!(Integer.parseInt(totalConnectionsFound)==totalConnnection.size())){
						verify.verifyHeading(totalConnectionsFound, Integer.toString(totalConnnection.size()));
					}
				} else if(typeOfSearch.equals("keywords")) {
					String matchingElements="xpath#//android.view.ViewGroup[@resource-id='com.redhat.partnerlink:id/plTagLayout']/android.widget.TextView";
					List<String> totalConnnection=mobileActions.totalListWithMatching(androidDriver,RH_Connections_Constants.nameListString,matchingElements,RH_Connections_Constants.loaderLogo,listTokenLabelsRequired);
					if(totalConnnection.isEmpty()){
						verify.verifyHeading("Keywords matching", "Keywords didn't match");
					}
					if(!(Integer.parseInt(totalConnectionsFound)==totalConnnection.size())){
						verify.verifyHeading(totalConnectionsFound, Integer.toString(totalConnnection.size()));
					}
				}
				mobileActions.click(androidDriver, RH_Connections_Constants.backToAdvancedSearchPage);
			}
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}

	void verifyApiUserData(HashMap<String,String> userData) throws InterruptedException{
		try{String userName=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.partnerName, "text");
		verify.verifyHeading(userData.get("Name"), userName);
		if(!userData.get("Email").equals("null")){
			String userEmail=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.partnerEmail, "text");
			verify.verifyHeading(userData.get("Email"), userEmail);
		}
		if(!userData.get("Phone").equals("null")){
			String userPhone=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.partnerContact, "text");
			verify.verifyHeading(userData.get("Phone"), userPhone);
		}
		if(!userData.get("AboutMe").equals("null")){
			String aboutMe=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.aboutMe, "text");
			verify.verifyHeading(userData.get("AboutMe"), aboutMe);
		}
		if(!userData.get("SocialLinks").equals("null")){
			//mobileActions.scrollTo(androidDriver, "SOCIAL LINKS");
			mobileActions.scrollBottom(androidDriver, "SOCIAL LINKS");
			String socialLinks=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.socialLinkText, "text");
			verify.verifyHeading(userData.get("SocialLinks"), socialLinks);
		}
		//mobileActions.scrollTo(androidDriver, "MATCHING CRITERIA");
		mobileActions.scrollBottom(androidDriver, "MATCHING CRITERIA");
		String matchingCriteria="";
		List<WebElement> matchingCriteriaElements=mobileActions.findMultipleElementsInList(androidDriver, RH_Connections_Constants.matchingCriteriaText);
		for(int i=0;i<matchingCriteriaElements.size();i++){
			matchingCriteria+=matchingCriteriaElements.get(i).getAttribute("text");
		}
		if(!userData.get("Industry").equals("null")){
			//mobileActions.scrollTo(androidDriver, "Industry focus");
			mobileActions.scrollBottom(androidDriver, "Industry focus");
			String industryFocus=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.industryText, "text");
			String apiData=(userData.get("Industry")).replaceAll(";", "\n");
			verify.verifyHeading(apiData, industryFocus);
		}
		if(!userData.get("AreaFocus").equals("null")){
			//mobileActions.scrollTo(androidDriver, "Area Of Focus");
			mobileActions.scrollBottom(androidDriver, "Area Of Focus");
			String areaFocus=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.areaOfFocusText, "text");
			String apiData=(userData.get("AreaFocus")).replaceAll(";", "\n");
			verify.verifyHeading(apiData, areaFocus);
		}
		if(!userData.get("ProductExpertise").equals("null")){
			mobileActions.scrollBottom(androidDriver, "red hat product expertise");
			String productExpertise=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.productExpertiseText, "text");
			String[] apiData=(userData.get("ProductExpertise")).replace(";;", ";").split(";");
			int match=0;
			for(int i=0;i<apiData.length;i++){
				if(productExpertise.toLowerCase().contains(apiData[i].trim().toLowerCase())){
					match++;
				}
			}
			if(match!=apiData.length){
				String apiDataFound=(userData.get("ProductExpertise")).replace(";","\n"); 
				verify.verifyHeading(apiDataFound, productExpertise);
			}
		}
		if(!userData.get("Country").equals("null")){
			mobileActions.scrollBottom(androidDriver, "Location");
			String location=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.locationText, "text");
			String apiData=(userData.get("Country")).replaceAll(";;", ";").replaceAll(";", "\n");
			verify.verifyHeading(apiData, location);
		}
		if(!userData.get("Accerdation").equals("null")){
			mobileActions.scrollBottom(androidDriver, "Accreditations");
			String accerdation=mobileActions.getTextDataFromUI(androidDriver, RH_Connections_Constants.accredationTetx, "text");
			String apiData=(userData.get("Accerdation")).replaceAll(";", "\n");
			verify.verifyHeading(apiData, accerdation);
		}
		}catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.fail();
		}
	}
	
	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void advanceSearch(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			PageFactory.initElements(androidDriver,RH_Common_Constants.class);
			PageFactory.initElements(androidDriver,RH_Search_Constants.class);
			PageFactory.initElements(androidDriver,RH_Connections_Constants.class);
			gettcdata=(HashMap) getTestData();
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			//RH_Login.login(androidDriver,mobileActions,verify,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);	
			mobileActions.click(androidDriver,RH_Search_Constants.find_connection_home);
			
			//mobileActions.click(androidDriver,RH_Search_Constants.advanceSearchButton);
			mobileActions.click(androidDriver,RH_Search_Constants.guided_search);
			String header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
			verify.assertion(header,"guided search");

			String searchBy = (String) gettcdata.get("searchBy");
			System.out.println(searchBy);
			String valuesToSearch = (String) gettcdata.get("valuesToSearch");
			String numberOfValuesToSearchArray[] = valuesToSearch.split(";");
			int numberOfValuesToSearch =numberOfValuesToSearchArray.length;
			System.out.println(numberOfValuesToSearch);
			String currentOptionToSearch = null;
			String totalConnectionsFound="";
			for(int searchCounter=0;searchCounter<numberOfValuesToSearch;searchCounter++) {
				switch(searchBy) {
				case "Location":
					mobileActions.click(androidDriver, RH_Search_Constants.locationCheckbox);
					mobileActions.setImplicityWait(androidDriver,3);
					if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.locationAccessPopupButton).size()>0){
						verify.verifyHeading((String)gettcdata.get("locationPopupMsg"), mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.locationPopupMsg, "text"));
						mobileActions.click(androidDriver, RH_Search_Constants.locationAccessPopupButton);
						if(mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.locationSwitch, "checked").equalsIgnoreCase("false")){
							mobileActions.click(androidDriver, RH_Search_Constants.locationSwitch);
							androidDriver.pressKeyCode(AndroidKeyCode.BACK);
						}
					}
					mobileActions.setImplicityWait(androidDriver,1);
					while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
					}
					if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.locateButton).size()==0){
						mobileActions.hideKeyboard(androidDriver);
					}
					mobileActions.setImplicityWait(androidDriver,10);
					List<WebElement> headerList=mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.locationHeader);
 					verify.verifyHeading((String)gettcdata.get("locationHeading"), headerList.get(0).getAttribute("text"));
 					if(searchCounter==0){
 						mobileActions.click(androidDriver, RH_Search_Constants.locationResetButton);
	 					if(mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.currerntLocationCheckbox, "checked").equalsIgnoreCase("true")){
	 						verify.verifyHeading("Reset true", "Reset false");
	 					}
	 				//	mobileActions.click(androidDriver, RH_Search_Constants.currerntLocationCheckbox);
	 					locationCheck(true);
 					}
 					else{
 						
 						String locateBy=(String)gettcdata.get("locateBy");
 						String[] locatebyArray=locateBy.split(";");
 						for(int locateByCounter=0;locateByCounter<locatebyArray.length;locateByCounter++){
 							if(locateByCounter>0){
 								mobileActions.click(androidDriver, RH_Search_Constants.locationCheckbox);
 								mobileActions.setImplicityWait(androidDriver,1);
 								while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
 								}
 								if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.locateButton).size()==0){
 									mobileActions.hideKeyboard(androidDriver);
 								}
 								mobileActions.setImplicityWait(androidDriver,10);		
 							}
 							if(mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.currerntLocationCheckbox, "checked").equalsIgnoreCase("true")){
 		 						mobileActions.click(androidDriver, RH_Search_Constants.currerntLocationCheckbox);
 		 					}
 							mobileActions.sendKeys(androidDriver, RH_Search_Constants.locationEdit, locatebyArray[locateByCounter]);
 							mobileActions.hideKeyboard(androidDriver);
 							locationCheck(false);
 						
 						}
 					}
					break;
				case "Accreditations":
					mobileActions.click(androidDriver, RH_Search_Constants.accreditationsOption);
					String currentOptionToSearchArray[] = numberOfValuesToSearchArray[searchCounter].split("#");
					currentOptionToSearch = currentOptionToSearchArray[1];
					if(!currentOptionToSearch.contains(",")) {
						mobileActions.click(androidDriver, RH_Search_Constants.selectOptionToSearch,currentOptionToSearch);	
					} else {
						String numberOfSearchsToApply[] = currentOptionToSearch.split(",");
						for(int currentOptionSearchCounter=0;currentOptionSearchCounter<numberOfSearchsToApply.length;currentOptionSearchCounter++) {
							System.out.println(numberOfSearchsToApply[currentOptionSearchCounter]);
							String selectOption=RH_Search_Constants.selectOptionToSearch.replace("*", numberOfSearchsToApply[currentOptionSearchCounter].trim());
							if(mobileActions.getTextDataFromUI(androidDriver, selectOption, "checked").equalsIgnoreCase("false")){
								mobileActions.click(androidDriver, RH_Search_Constants.selectOptionToSearch,numberOfSearchsToApply[currentOptionSearchCounter].trim());
							}
						}
					}
					mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"guided search");
					mobileActions.click(androidDriver, RH_Search_Constants.advancedSearchFindConnectionsButton);
					//totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
					
					if(header.contains("guided")) {
						header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
						verify.assertion(header,"guided search");
						mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
					} else {
						//List<WebElement> connectionsHeader=mobileActions.findMultipleElementsInList(androidDriver, RH_Connections_Constants.connectionsHeader);
						//String connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
						header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
						verify.assertion(header,"CONNECTIONS");
					
						mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
						mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
						
					//	mobileActions.setImplicityWait(androidDriver,20);			
						/*if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
							mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
						}*/
						mobileActions.setImplicityWait(androidDriver,10);			
					//	for(int screenSwipeCounter=1;screenSwipeCounter<=Integer.parseInt(totalConnectionsFound);screenSwipeCounter++) {
						for(int screenSwipeCounter=1;screenSwipeCounter<=1;screenSwipeCounter++) {
							//String searchCount=mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.searchCount, "text");
							//String requiredCount="Connections "+Integer.toString(screenSwipeCounter)+" of "+totalConnectionsFound;
							//verify.verifyHeading(requiredCount, searchCount);
							mobileActions.swipeLeftToRight(androidDriver);
														
							mobileActions.setImplicityWait(androidDriver,10);
						}

						mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
						header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
						verify.assertion(header,"guided search");
						
						mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
					}
					break;
				case "Red Hat Product Expertise":
					mobileActions.click(androidDriver, RH_Search_Constants.redHatProductExpertise);
					String currentOptionToSearchArrayRedHatProductFamily[] = numberOfValuesToSearchArray[searchCounter].split("#");
					currentOptionToSearch = currentOptionToSearchArrayRedHatProductFamily[1];
					
					String currentFilterToSelect[] = currentOptionToSearch.split("\\*");
					mobileActions.scrollTo(androidDriver,currentFilterToSelect[0]);
					mobileActions.click(androidDriver, RH_Search_Constants.subCategorySelection.trim(),currentFilterToSelect[0]+" ");
					System.out.println(currentFilterToSelect[1]);
					if(!currentFilterToSelect[1].contains(",")) {
						mobileActions.click(androidDriver, RH_Search_Constants.selectSubCategoryOptions,currentFilterToSelect[1]+" ");
					} else {
						String numberOfSearchsToApply[] = currentFilterToSelect[1].split(",");
						for(int currentOptionSearchCounter=0;currentOptionSearchCounter<numberOfSearchsToApply.length;currentOptionSearchCounter++) {
							System.out.println(numberOfSearchsToApply[currentOptionSearchCounter]);
							String selectOption=RH_Search_Constants.selectSubCategoryOptions.replace("*",numberOfSearchsToApply[currentOptionSearchCounter].trim()+" ");
							if(mobileActions.getTextDataFromUI(androidDriver, selectOption, "checked").equalsIgnoreCase("false")){
								mobileActions.click(androidDriver, RH_Search_Constants.selectSubCategoryOptions,numberOfSearchsToApply[currentOptionSearchCounter].trim()+" ");
							}
						}
					}
					mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
					
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"guided search");
					mobileActions.click(androidDriver, RH_Search_Constants.advancedSearchFindConnectionsButton);
					Thread.sleep(2000);
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					
					
				//	totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
				//	System.out.println("totalConnectionsFound"+totalConnectionsFound);
				//	header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					if(header.contains("Guided")) {
						
						header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
						verify.assertion(header,"guided search");
						mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
					} else {
						
						//List<WebElement> connectionsHeader=mobileActions.findMultipleElementsInList(androidDriver, RH_Connections_Constants.connectionsHeader);
						//String connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
						header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
						verify.assertion(header,"CONNECTIONS");

						mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
						mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
						/*mobileActions.setImplicityWait(androidDriver,2);			
						if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
							mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
						}*/
						mobileActions.setImplicityWait(androidDriver,10);			
						for(int screenSwipeCounter=1;screenSwipeCounter<=1;screenSwipeCounter++) {
							/*String searchCount=mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.searchCount, "text");
							String requiredCount="Connections "+Integer.toString(screenSwipeCounter)+" of "+totalConnectionsFound;
							verify.verifyHeading(requiredCount, searchCount);*/
							
							mobileActions.setImplicityWait(androidDriver,10);
						}
						
						mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
						header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
						verify.assertion(header,"guided search");
						mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
					}
				break;
			case "Industry":
				mobileActions.click(androidDriver, RH_Search_Constants.industryOption);
				currentOptionToSearchArray = numberOfValuesToSearchArray[searchCounter].split("#");
				currentOptionToSearch = currentOptionToSearchArray[1];
				if(!currentOptionToSearch.contains(",")) {
					mobileActions.scrollTo(androidDriver, currentOptionToSearch.trim());
					mobileActions.click(androidDriver, RH_Search_Constants.selectOptionToSearch,currentOptionToSearch);	
				} else {
					String numberOfSearchsToApply[] = currentOptionToSearch.split(",");
					System.out.println(numberOfSearchsToApply.length);
					for(int currentOptionSearchCounter=0;currentOptionSearchCounter<numberOfSearchsToApply.length;currentOptionSearchCounter++) {
						System.out.println(numberOfSearchsToApply[currentOptionSearchCounter]);
						
						mobileActions.scrollTo(androidDriver, numberOfSearchsToApply[currentOptionSearchCounter].trim());
						String selectOption=RH_Search_Constants.selectOptionToSearch.replace("*", numberOfSearchsToApply[currentOptionSearchCounter].trim());
						if(mobileActions.getTextDataFromUI(androidDriver, selectOption, "checked").equalsIgnoreCase("false")){
							mobileActions.click(androidDriver, RH_Search_Constants.selectOptionToSearch,numberOfSearchsToApply[currentOptionSearchCounter].trim());
						}	
					}
				}
				mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
				header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
				verify.assertion(header,"guided search");

				mobileActions.click(androidDriver, RH_Search_Constants.advancedSearchFindConnectionsButton);
				//totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
				//System.out.println("totalConnectionsFound"+totalConnectionsFound);

				if(header.contains("guided")) {
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"guided search");
					mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
				} else {
					//List<WebElement> connectionsHeader=mobileActions.findMultipleElementsInList(androidDriver, RH_Connections_Constants.connectionsHeader);
					//String connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"CONNECTIONS");
					mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
					mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
					/*mobileActions.setImplicityWait(androidDriver,2);			
					if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
						mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
					}*/
					mobileActions.setImplicityWait(androidDriver,10);			
					for(int screenSwipeCounter=1;screenSwipeCounter<=1;screenSwipeCounter++) {
						/*String searchCount=mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.searchCount, "text");
						String requiredCount="Connections "+Integer.toString(screenSwipeCounter)+" of "+totalConnectionsFound;
						verify.verifyHeading(requiredCount, searchCount);*/
						mobileActions.swipeLeftToRight(androidDriver);
						
						mobileActions.setImplicityWait(androidDriver,10);
					}
					mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"guided search");
					
					mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
					
				}
				break;
			case "Partner Profile":
				mobileActions.click(androidDriver, RH_Search_Constants.partnerProfileOption);
				currentOptionToSearchArrayRedHatProductFamily = numberOfValuesToSearchArray[searchCounter].split("#");
				currentOptionToSearch = currentOptionToSearchArrayRedHatProductFamily[1];
				System.out.println(currentOptionToSearch);
				currentFilterToSelect = currentOptionToSearch.split("\\*");
				mobileActions.scrollTo(androidDriver,currentFilterToSelect[0]);
				List<WebElement> listOfSearches=mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.selectPartnerprofileSubMenuList);
				System.out.println(listOfSearches);
				int partnerSearchNo=0;
				for(int i=0;i<listOfSearches.size();i++){
					String text=mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.selectPartnerprofileSubMenuText.replace("*", Integer.toString(i)), "text");
					if(text.equals(currentFilterToSelect[0]))
						break;
					partnerSearchNo++;
				}
				mobileActions.setImplicityWait(androidDriver,30);
				mobileActions.click(androidDriver, RH_Search_Constants.selectPartnerprofileSubMenu.replace("*", Integer.toString(partnerSearchNo)));
				//mobileActions.click(androidDriver, RH_Search_Constants.subCategorySelection,currentFilterToSelect[0]+" ");
				System.out.println(currentFilterToSelect[1]);
				if(!currentFilterToSelect[1].contains(",")) {
					mobileActions.click(androidDriver, RH_Search_Constants.selectSubCategoryOptions,currentFilterToSelect[1]);	
				} else {
					String numberOfSearchsToApply[] = currentFilterToSelect[1].split(",");
					for(int currentOptionSearchCounter=0;currentOptionSearchCounter<numberOfSearchsToApply.length;currentOptionSearchCounter++) {
						System.out.println(numberOfSearchsToApply[currentOptionSearchCounter]);
						mobileActions.scrollTo(androidDriver, numberOfSearchsToApply[currentOptionSearchCounter].trim());
						mobileActions.click(androidDriver, RH_Search_Constants.selectSubCategoryOptions,numberOfSearchsToApply[currentOptionSearchCounter].trim());
					}
				}
				mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
				header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
				verify.assertion(header,"guided search");
				mobileActions.click(androidDriver, RH_Search_Constants.advancedSearchFindConnectionsButton);
				//totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
				//System.out.println("totalConnectionsFound"+totalConnectionsFound);
				if(header.contains("guided")) {
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"guided search");
					mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
				} else {
				
					//List<WebElement> connectionsHeader=mobileActions.findMultipleElementsInList(androidDriver, RH_Connections_Constants.connectionsHeader);
					//String connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"CONNECTIONS");
					mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
					mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
					mobileActions.setImplicityWait(androidDriver,2);
					
					if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
						mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
					}
					
					mobileActions.setImplicityWait(androidDriver,10);	
					
					
					for(int screenSwipeCounter=1;screenSwipeCounter<=1;screenSwipeCounter++) {
						/*String searchCount=mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.searchCount, "text");
						String requiredCount="Connections "+Integer.toString(screenSwipeCounter)+" of "+totalConnectionsFound;
						verify.verifyHeading(requiredCount, searchCount);*/
						mobileActions.swipeLeftToRight(androidDriver);
						mobileActions.setImplicityWait(androidDriver,1);
						while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
						}
						mobileActions.setImplicityWait(androidDriver,10);
					}
					
					mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"guided search");
					mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
				}
				break;
			case "Expertise":
				mobileActions.click(androidDriver, RH_Search_Constants.expertiseOption);
				currentOptionToSearchArray = numberOfValuesToSearchArray[searchCounter].split("#");
				currentOptionToSearch = currentOptionToSearchArray[1];
				if(!currentOptionToSearch.contains(",")) {
					mobileActions.click(androidDriver, RH_Search_Constants.selectOptionToSearch,currentOptionToSearch);	
				} else {
					String numberOfSearchsToApply[] = currentOptionToSearch.split(",");
					for(int currentOptionSearchCounter=0;currentOptionSearchCounter<numberOfSearchsToApply.length;currentOptionSearchCounter++) {
						System.out.println(numberOfSearchsToApply[currentOptionSearchCounter]);
						mobileActions.scrollTo(androidDriver, numberOfSearchsToApply[currentOptionSearchCounter].trim());
						mobileActions.click(androidDriver, RH_Search_Constants.selectOptionToSearch,numberOfSearchsToApply[currentOptionSearchCounter].trim());	
					}
				}
				mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
				header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
				verify.assertion(header,"guided search");
				mobileActions.click(androidDriver, RH_Search_Constants.advancedSearchFindConnectionsButton);
				//totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
				//System.out.println("totalConnectionsFound"+totalConnectionsFound);
				if(header.contains("guided")) {
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"guided search");
					mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
				} else {
					//List<WebElement> connectionsHeader=mobileActions.findMultipleElementsInList(androidDriver, RH_Connections_Constants.connectionsHeader);
					//String connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"CONNECTIONS");
					mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
					mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
					/*mobileActions.setImplicityWait(androidDriver,2);			
					if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
						mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
					}*/
					mobileActions.setImplicityWait(androidDriver,10);
					
					for(int screenSwipeCounter=1;screenSwipeCounter<=1;screenSwipeCounter++) {
						/*String searchCount=mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.searchCount, "text");
						String requiredCount="Connections "+Integer.toString(screenSwipeCounter)+" of "+totalConnectionsFound;
						verify.verifyHeading(requiredCount, searchCount);*/
						mobileActions.swipeLeftToRight(androidDriver);
						
						mobileActions.setImplicityWait(androidDriver,1);
						
						while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
						}
						
						mobileActions.setImplicityWait(androidDriver,10);
					}
					mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
					header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
					verify.assertion(header,"guided search");
					mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
				}
				break;
			}
		}
			
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
	}catch (Exception e) {
		WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
		Assert.assertTrue(false);
	}
}
	
	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void recentSearch(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			
			
			String accessToken=ApiCall.apiCall((String)gettcdata.get("username"),(String)gettcdata.get("password"));
			System.out.println(accessToken);
			System.out.println(ApiCall.userFirstName);
			System.out.println(ApiCall.userLastName);
			System.out.println(ApiCall.userEmail);
			//System.out.println(ApiCall.userPhone);
			//System.out.println(ApiCall.socialLinks);
			System.out.println(ApiCall.userID);
			String url="";
			String instance=(String)gettcdata.get("instance");
			REST_Test rest=new REST_Test();
			REST_Verification restVerify=new REST_Verification();
			if(instance.equalsIgnoreCase("stage")){
				url=Configuration.apiStageURL+(String)gettcdata.get("url");
			}
			else{
				url=Configuration.apiTestURL+(String)gettcdata.get("url");
			}
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("user-id",ApiCall.userID);
			hashMap.put("x-access-token",accessToken);
			hashMap.put("x-fh-auth-app",(String)gettcdata.get("authApp"));
			
			hashMap.put("Content_Type", (String)gettcdata.get("contentType"));
			hashMap.put("Method", (String)gettcdata.get("method"));
			hashMap.put("ExpectedStatus", (String)gettcdata.get("status"));
			hashMap.put("URL", url);
			
			
			String response=rest.executeREST_Service(hashMap);
			
			List<String> searchData=restVerify.retrieveRecentSearch(response);
			System.out.println(searchData);
			
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			//RH_Login.login(androidDriver,mobileActions,verify,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);	
			mobileActions.click(androidDriver,RH_Search_Constants.find_connection_home);
			
			//mobileActions.click(androidDriver,RH_Search_Constants.advanceSearchButton);
			mobileActions.click(androidDriver,RH_Search_Constants.guided_search);
			String header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
			verify.assertion(header,"guided search");

			
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_Search_Constants.recentSearchButton);
			List<String> searchHeadings=mobileActions.totalListHeading(androidDriver, RH_Search_Constants.nameListString,RH_Connections_Constants.loaderLogo);
			
			
			verify.verifyHeading(searchData.get(0), Integer.toString(searchHeadings.size()));
			System.out.println(searchData);
			searchData.remove(0);
			
			//mobileActions.click(androidDriver,RH_Search_Constants.guided_search);
			header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
			verify.assertion(header,"RECENT SEARCHES");
			
			for(int searchCounter=0;searchCounter<searchData.size();searchCounter++){
				String searchDataSplit=searchHeadings.get(searchCounter).split(":",2)[1].trim().substring(3);
				
				
				//verify.verifyHeading(searchData.get(searchCounter),searchDataSplit);
			}
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
		}
		catch (Exception e) {
		WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
		Assert.assertTrue(false);
		}
	}
	
	private void locationCheck(boolean swipeStatus) throws InterruptedException{
		String header;
		mobileActions.click(androidDriver, RH_Search_Constants.locateButton);
			mobileActions.setImplicityWait(androidDriver,1);
		
		mobileActions.setImplicityWait(androidDriver,10);
		List<WebElement> headerList=mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.connectionHeader);
		verify.verifyHeading((String)gettcdata.get("connectionHeader"), headerList.get(0).getAttribute("text"));
		String connectionFound=mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.locationUserLocated, "text").replace("PARTNERS LOCATED", "").trim();
		mobileActions.click(androidDriver, RH_Search_Constants.backButton);
		mobileActions.click(androidDriver, RH_Search_Constants.backButton);
		mobileActions.click(androidDriver, RH_Search_Constants.advancedSearchFindConnectionsButton);
	//	String totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
	//	System.out.println("totalConnectionsFound"+totalConnectionsFound);
		header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
		if(header.contains("Guided")) {
			header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
			verify.assertion(header,"guided search");
			mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
		} else {
			
			verify.verifyHeading(header, "CONNECTIONS");
			//List<WebElement> connectionsHeader=mobileActions.findMultipleElementsInList(androidDriver, RH_Connections_Constants.connectionsHeader);
			//String connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
			//verify.assertion(connectionsHeaderValue,"CONNECTIONS");

			mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
			mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
			mobileActions.setImplicityWait(androidDriver,2);			
			if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
				mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
			}
			mobileActions.setImplicityWait(androidDriver,10);			
			for(int screenSwipeCounter=1;screenSwipeCounter<=1 && swipeStatus;screenSwipeCounter++) {
				String searchCount=mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.searchCount, "text");
				//String requiredCount="Connections "+Integer.toString(screenSwipeCounter)+" of "+totalConnectionsFound;
				//verify.verifyHeading(requiredCount, searchCount);
				mobileActions.swipeLeftToRight(androidDriver);
				Thread.sleep(1000);
				
				mobileActions.setImplicityWait(androidDriver,10);
			}

			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		
			header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
			verify.assertion(header,"guided search");
			mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
		}
	}
}
