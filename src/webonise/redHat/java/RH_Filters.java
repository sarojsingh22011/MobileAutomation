package webonise.redHat.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import webonise.automation.REST.REST_Test;
import webonise.automation.REST.REST_Verification;
import webonise.automation.core.Configuration;
import webonise.automation.core.MobileActions;
import webonise.automation.core.WeboAutomation;
import webonise.redHat.constants.RH_Common_Constants;
import webonise.redHat.constants.RH_DealRegistration_Constants;

public class RH_Filters extends WeboAutomation {
	
	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	HashMap dealsData=new HashMap();
	public RH_Filters() throws IOException {
		super();
		initalizeReport(this.getClass().getName());
		PageFactory.initElements(androidDriver,RH_Common_Constants.class);
		PageFactory.initElements(androidDriver,RH_DealRegistration_Constants.class);
	}

	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void login(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			
			String accessToken=ApiCall.apiCall((String)gettcdata.get("username"), (String)gettcdata.get("password"));
			System.out.println(accessToken);
			System.out.println(ApiCall.userFirstName);
			System.out.println(ApiCall.userLastName);
			System.out.println(ApiCall.userEmail);
			System.out.println(ApiCall.userPhone);
			System.out.println(ApiCall.socialLinks);
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
			
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("user-id",ApiCall.userID);
			hashMap.put("x-access-token",accessToken);
			hashMap.put("x-fh-auth-app",(String)gettcdata.get("authApp"));
			
			hashMap.put("Content_Type", (String)gettcdata.get("contentType"));
			hashMap.put("Method", (String)gettcdata.get("method"));
			hashMap.put("ExpectedStatus", (String)gettcdata.get("status"));
			hashMap.put("URL", url);
			String response=rest.executeREST_Service(hashMap);
			dealsData=restVerify.retrieveDealsData(response);
					
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			//RH_Login.login(androidDriver,mobileActions,verify,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
		}
	
	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void filterDeals(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			Thread.sleep(1000);
			mobileActions.click(androidDriver, RH_Common_Constants.dealMenu);
			Thread.sleep(1000);
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.viewallDeal);
			mobileActions.setImplicityWait(androidDriver,2);
			while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
			}
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.filterButton);
			List<String> draftList=new ArrayList<String>();
			List<String> rejectedList=new ArrayList<String>();
			List<String> pendingList=new ArrayList<String>();
			List<String> approvedList=new ArrayList<String>();
			Iterator mapiterator=dealsData.entrySet().iterator();
			while(mapiterator.hasNext()){
				Map.Entry entry = (Map.Entry)mapiterator.next();
				String email=((String) entry.getValue()).split("#")[0].trim();
		        String status=((String) entry.getValue()).split("#")[1].trim();
		        switch(status){
		        case "Draft":
		         	draftList.add(email);
		        	break;
		        case "Pending":
		        	pendingList.add(email);
		        	break;
		        case "Rejected":
		        	rejectedList.add(email);
		        	break;
		        case "Approved":
		        	approvedList.add(email);
		        	break;
		        }
			}
			System.out.println(""+draftList);
			System.out.println(""+pendingList);
			System.out.println(""+rejectedList);
			System.out.println(""+approvedList);
			
			//Draft Filter
			String draftChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.draftFilter, "checked");
			String pendingChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.pendingFilter, "checked");
			String rejectedChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.rejectedFilter, "checked");
			String approvedChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.approvedFilter, "checked");
			mobileActions.setImplicityWait(androidDriver,1);
			if(draftChecked.equalsIgnoreCase("false")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.draftFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){		
				}
			}
			if(pendingChecked.equalsIgnoreCase("true")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.pendingFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			if(rejectedChecked.equalsIgnoreCase("true")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.rejectedFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			if(approvedChecked.equalsIgnoreCase("true")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.approvedFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.filterButton);
			// code for counting the number of "DRAFT" deals displayed 
			boolean scrollEvent=true;
			int scrollEventCount=0; 
			while(scrollEvent){
				List<WebElement> statusElement=mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.dealStatus);
				List<WebElement> emailElement=mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.userEmail);
				for(int statusCounter=0;statusCounter<emailElement.size();statusCounter++){
					String statusString=statusElement.get(statusCounter).getAttribute("text");
					String userEmail=emailElement.get(statusCounter).getAttribute("text");
					if(userEmail.equals("")){
						userEmail="null";
					}
					if(!statusString.equalsIgnoreCase("draft")){
						verify.verifyHeading("Draft", statusString);
					}
					if(!draftList.contains(userEmail)){
						verify.verifyHeading("API match Fail", userEmail);
					}
				}
				mobileActions.swipeBottomToTop(androidDriver);
				mobileActions.setImplicityWait(androidDriver,1);
				if(mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.viewMoreButton).size()>0){
					mobileActions.click(androidDriver, RH_DealRegistration_Constants.viewMoreButton);
					while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
					}
					scrollEventCount=0;
				}
				else{
					scrollEventCount++;
					if(scrollEventCount>1)
						scrollEvent=false;
				}
				mobileActions.setImplicityWait(androidDriver,10);
			}
			
			// Pending and Approved filter
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.filterButton);
			mobileActions.setImplicityWait(androidDriver,1);
			draftChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.draftFilter, "checked");
			pendingChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.pendingFilter, "checked");
			rejectedChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.rejectedFilter, "checked");
			approvedChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.approvedFilter, "checked");
			if(draftChecked.equalsIgnoreCase("true")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.draftFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){		
				}
			}
			if(pendingChecked.equalsIgnoreCase("false")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.pendingFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			if(rejectedChecked.equalsIgnoreCase("true")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.rejectedFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			if(approvedChecked.equalsIgnoreCase("false")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.approvedFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.filterButton);
			// code for counting the number of "PENDING & APPROVED" deals displayed
			scrollEvent=true;
			scrollEventCount=0;
		if(!(androidDriver.findElementByXPath(RH_DealRegistration_Constants.dealStatus).isDisplayed()))
		{
			
		}
		else {
			while(scrollEvent){
				List<WebElement> statusElement=mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.dealStatus);
				List<WebElement> emailElement=mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.userEmail);
				for(int statusCounter=0;statusCounter<emailElement.size();statusCounter++){
					String statusString=statusElement.get(statusCounter).getAttribute("text");
					String userEmail=emailElement.get(statusCounter).getAttribute("text");
					if(userEmail.equals("")){
						userEmail="null";
					}
					if(! (statusString.equalsIgnoreCase("rejected") || statusString.equalsIgnoreCase("pending")) ){
						verify.verifyHeading("Draft", statusString);
					}
					if(statusString.equalsIgnoreCase("rejected") ){
						if(!rejectedList.contains(userEmail)){
							verify.verifyHeading("API match Fail", userEmail);
						}
					}
					if(statusString.equalsIgnoreCase("pending") ){
						if(!pendingList.contains(userEmail)){
							verify.verifyHeading("API match Fail", userEmail);
						}
					}
				}
				mobileActions.swipeBottomToTop(androidDriver);
				mobileActions.setImplicityWait(androidDriver,1);
				if(mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.viewMoreButton).size()>0){
					mobileActions.click(androidDriver, RH_DealRegistration_Constants.viewMoreButton);
					while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
					}
					scrollEventCount=0;
				}
				else{
					scrollEventCount++;
					if(scrollEventCount>1)
						scrollEvent=false;
				}
				mobileActions.setImplicityWait(androidDriver,10);
			}
		}
			//Rejected Filter
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.filterButton);
			draftChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.draftFilter, "checked");
			pendingChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.pendingFilter, "checked");
			rejectedChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.rejectedFilter, "checked");
			approvedChecked=mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.approvedFilter, "checked");
			mobileActions.setImplicityWait(androidDriver,1);
			if(draftChecked.equalsIgnoreCase("true")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.draftFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){		
				}
			}
			if(pendingChecked.equalsIgnoreCase("true")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.pendingFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			if(rejectedChecked.equalsIgnoreCase("false")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.rejectedFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			if(approvedChecked.equalsIgnoreCase("true")){
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.approvedFilter);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
			}
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.filterButton);
			// code for counting the number of "Rejected" deals displayed 
			scrollEvent=true;
			scrollEventCount=0; 
			if(!(androidDriver.findElementByXPath(RH_DealRegistration_Constants.dealStatus).isDisplayed()))
			{
				
			}
			else {
			while(scrollEvent){
				List<WebElement> statusElement=mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.dealStatus);
				List<WebElement> emailElement=mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.userEmail);
				for(int statusCounter=0;statusCounter<emailElement.size();statusCounter++){
					String statusString=statusElement.get(statusCounter).getAttribute("text");
					String userEmail=emailElement.get(statusCounter).getAttribute("text");
					if(userEmail.equals("")){
						userEmail="null";
					}
					if(!statusString.equalsIgnoreCase("rejected")){
						verify.verifyHeading("Rejected", statusString);
					}
					if(!rejectedList.contains(userEmail)){
						verify.verifyHeading("API match Fail", userEmail);
					}
				}
				mobileActions.swipeBottomToTop(androidDriver);
				mobileActions.setImplicityWait(androidDriver,1);
				if(mobileActions.findMultipleElementsInList(androidDriver, RH_DealRegistration_Constants.viewMoreButton).size()>0){
					mobileActions.click(androidDriver, RH_DealRegistration_Constants.viewMoreButton);
					while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
					}
					scrollEventCount=0;
				}
				else{
					scrollEventCount++;
					if(scrollEventCount>1)
						scrollEvent=false;
				}
				mobileActions.setImplicityWait(androidDriver,10);
				mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
				mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
			}
			}
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
		}
}
