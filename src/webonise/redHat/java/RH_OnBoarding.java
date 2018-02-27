package webonise.redHat.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import webonise.automation.REST.REST_Test;
import webonise.automation.REST.REST_Verification;
import webonise.automation.core.CommonUtility;
import webonise.automation.core.Configuration;
import webonise.automation.core.MobileActions;
import webonise.automation.core.WeboAutomation;
import webonise.automation.core.verification.MobileVerification;
import webonise.redHat.constants.*;

public class RH_OnBoarding extends WeboAutomation{

	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	MobileVerification mobileVerification = new MobileVerification();

	public RH_OnBoarding() throws Exception 
	{
		super();
		initalizeReport(this.getClass().getName());
		PageFactory.initElements(androidDriver,RH_Common_Constants.class);
		PageFactory.initElements(androidDriver,RH_OnBoarding_Constants.class);
	} 

	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void onBoarding(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			
			///////////////////////////////////////////////////////////
			
		//	String accessToken=ApiCall.apiCall_forboarding((String)gettcdata.get("username"),(String)gettcdata.get("password"));
			
			JSONObject j=ApiCall.apiCall_forboarding_details((String)gettcdata.get("username"),(String)gettcdata.get("password"));
			
			
			String accessToken=ApiCall.access_token;
			System.out.println(accessToken);
			/*System.out.println(ApiCall.userFirstName);
			System.out.println(ApiCall.userLastName);
			System.out.println(ApiCall.userEmail);*/
			//System.out.println(ApiCall.userPhone);
			//System.out.println(ApiCall.socialLinks);
			System.out.println("$$$"+ApiCall.userID);
			String u_id=ApiCall.userID;
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
			//HashMap<String, String> hashMap = new HashMap<String, String>();
			HashMap hashMap = new HashMap();
			hashMap.clear();
			hashMap.put("user-id",u_id);
			hashMap.put("x-access-token",accessToken);
			hashMap.put("x-fh-auth-app",(String)gettcdata.get("authApp"));
			
			hashMap.put("Content_Type", (String)gettcdata.get("contentType"));
			hashMap.put("Method", (String)gettcdata.get("method"));
			hashMap.put("ExpectedStatus", (String)gettcdata.get("status"));
			hashMap.put("URL", url);
			hashMap.put("details", j);
							
			
			String response=rest.executeREST_Service(hashMap);
			//HashMap<String,String> searchData=restVerify.retrieveUserData_foronboarding(response);
			HashMap searchData=restVerify.retrieveUserData_foronboarding(response);
			
			//System.out.println(searchData);
			
					
			//////////////////////////////////////////////////////////
			
			//RH_Login.login(androidDriver,mobileActions,verify,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
				mobileActions.setImplicityWait(androidDriver,30);
			String confirmYourPartnerInfoHeader = mobileActions.getTextDataFromUI(androidDriver,RH_OnBoarding_Constants.confirmYourPartnerInfoHeader,"name");
			System.out.println("confirmYourPartnerInfoHeader:"+confirmYourPartnerInfoHeader);
			verify.assertion(confirmYourPartnerInfoHeader,"confirm your partner info");

			List<WebElement> partnerInfoLabels = mobileActions.findMultipleElementsInList(androidDriver,RH_OnBoarding_Constants.partnerLabels);
									
			System.out.println("partnerInfoLabels:"+partnerInfoLabels.size());
			MobileElement listItem = null;
			for(int i=0; i< partnerInfoLabels.size(); i++){
				listItem = (MobileElement) partnerInfoLabels.get(i);   
				String label = listItem.getText();
				switch(i) {
				case 0:
					verify.assertion(label,"First Name");
					break;
				case 1:
					verify.assertion(label,"Last Name");
					break;
				case 2:
					verify.assertion(label,"Company");
					break;
				case 3:
					verify.assertion(label,"Phone");
					if(((String) gettcdata.get("phone")).equalsIgnoreCase("Hide")) {
						mobileActions.click(androidDriver, RH_OnBoarding_Constants.hidePhoneNumber);
					}
					break;
				case 4:
					verify.assertion(label,"Email");
					if(((String) gettcdata.get("email")).equalsIgnoreCase("Hide")) {
						mobileActions.click(androidDriver, RH_OnBoarding_Constants.hideEmail);
					}
					break;
				}
			}  
			
						
			List<WebElement> partnerinfovalues = mobileActions.findMultipleElementsInList(androidDriver,RH_OnBoarding_Constants.partnerinfovalues);
			MobileElement listItem_val = null;
			for(int i=0; i< partnerinfovalues.size(); i++){
				listItem_val = (MobileElement) partnerinfovalues.get(i);   
				String value = listItem_val.getText();
				
				switch(i) {
				case 0:
					verify.assertion(value,ApiCall.userFirstName);
					break;
				case 1:
					verify.assertion(value,ApiCall.userLastName);
					break;
				case 2:
					verify.assertion(value,ApiCall.company_name);
					break;
				case 3:
					if(ApiCall.Phone=="null" )
					{
						ApiCall.Phone="";
					}
					verify.assertion(value,ApiCall.Phone);
					break;
				case 4:
					verify.assertion(value,ApiCall.userEmail);
					break;
				}
			} 
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.continueButton);
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.location);
			mobileActions.sendKeys(androidDriver,RH_OnBoarding_Constants.streetAddress , (String)gettcdata.get("stresstAddress"));
			mobileActions.hideKeyboard(androidDriver);
			
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.countryDropDown);
			mobileActions.scrollToExact(androidDriver, ((String)gettcdata.get("country")));
			//mobileActions.click(androidDriver, RH_OnBoarding_Constants.countrySelect,(String)gettcdata.get("country"));
			mobileActions.click(androidDriver,"name#"+(String)gettcdata.get("country"));
			
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.state);
			mobileActions.scrollTo(androidDriver, (String)gettcdata.get("state"));
			//mobileActions.click(androidDriver, RH_OnBoarding_Constants.stateSelect,(String)gettcdata.get("state"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("state"));
			
			//mobileActions.scrollEvent(androidDriver, "city");
			mobileActions.sendKeys(androidDriver, RH_OnBoarding_Constants.city, (String)gettcdata.get("city"));
			mobileActions.hideKeyboard(androidDriver);
			
			mobileActions.sendKeys(androidDriver, RH_OnBoarding_Constants.postalCode, (String)gettcdata.get("postalCode"));
			mobileActions.hideKeyboard(androidDriver);
			
			mobileActions.scrollTo(androidDriver,"Location");
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.location);
			
			mobileActions.setImplicityWait(androidDriver,50);	
			
			mobileActions.scrollTo(androidDriver,"Industry");
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.industry);
			
			
			String industriesToSelect[] = ((String) gettcdata.get("industry")).split(",");
			for(int industrySelectionCounter=0;industrySelectionCounter<industriesToSelect.length;industrySelectionCounter++) {
				Thread.sleep(1000);
				
				mobileActions.scrollTo(androidDriver,industriesToSelect[industrySelectionCounter]);
				mobileActions.click(androidDriver, RH_OnBoarding_Constants.industryToSelect,industriesToSelect[industrySelectionCounter]);
			
				
			}
			mobileActions.scrollTo(androidDriver,"Industry");
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.industry);
			
			Thread.sleep(1000);
			
			mobileActions.setImplicityWait(androidDriver,50);	
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.areaOfFocus);
			String areaOfFocussToSelect[] = ((String) gettcdata.get("areaOfFocus")).split(",");
			for(int areaOfFocusSelectionCounter=0;areaOfFocusSelectionCounter<areaOfFocussToSelect.length;areaOfFocusSelectionCounter++) {
				mobileActions.setImplicityWait(androidDriver,20);
				mobileActions.scrollTo(androidDriver,areaOfFocussToSelect[areaOfFocusSelectionCounter]);
				mobileActions.click(androidDriver, RH_OnBoarding_Constants.areaOfFocusToSelect,areaOfFocussToSelect[areaOfFocusSelectionCounter]);
			}
			mobileActions.scrollTo(androidDriver,"Area of Focus");
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.areaOfFocus);
			
			
			mobileActions.setImplicityWait(androidDriver,50);
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.redHatProductExpertise);
			String redHatExpertiseToSelect[] = ((String) gettcdata.get("redHatProductExpertise")).trim().split(",");
			for(int redHatExpertiseSelectionCounter=0;redHatExpertiseSelectionCounter<redHatExpertiseToSelect.length;redHatExpertiseSelectionCounter++) {
				String productExpertiseOptionArray[] = redHatExpertiseToSelect[redHatExpertiseSelectionCounter].split(":");
				String productsToSelectList = productExpertiseOptionArray[1].trim();
			
				mobileActions.scrollTo(androidDriver,productExpertiseOptionArray[0].trim());
				mobileActions.click(androidDriver, RH_OnBoarding_Constants.redHatExpertiseToSelect,productExpertiseOptionArray[0].trim());
				if(productsToSelectList.contains("#")) {
					String productsToSelectArray[] = productsToSelectList.split("#");
					for(int innerProductSelectionCounter=0;innerProductSelectionCounter<productsToSelectArray.length;innerProductSelectionCounter++) {
						
						mobileActions.click(androidDriver, RH_OnBoarding_Constants.selectInnerProduct,productsToSelectArray[innerProductSelectionCounter]);
					}
					mobileActions.click(androidDriver, RH_OnBoarding_Constants.doneButton);
				} else {
					mobileActions.click(androidDriver, RH_OnBoarding_Constants.selectInnerProduct,productsToSelectList);
					mobileActions.click(androidDriver, RH_OnBoarding_Constants.doneButton);
				}
			}
			mobileActions.scrollTo(androidDriver,"Red Hat Product Expertise");
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.redHatProductExpertise);
			
			mobileActions.sendKeys(androidDriver, RH_OnBoarding_Constants.socialLinks, (String) gettcdata.get("socialLinkURL"));
			mobileActions.hideKeyboard(androidDriver);
			mobileActions.scrollTo(androidDriver, "Is there anything else Red Hat partners should know about you?");
			mobileActions.sendKeys(androidDriver, RH_OnBoarding_Constants.aboutYou, (String) gettcdata.get("anythingElseAboutYou"));
			mobileActions.hideKeyboard(androidDriver);
			
			mobileActions.setImplicityWait(androidDriver,20);	
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.continueButton);
			
			Thread.sleep(20000);
			String preferencesHeader = mobileActions.getTextDataFromUI(androidDriver,RH_OnBoarding_Constants.preferencesHeader,"name");
			System.out.println("preferencesHeader:"+preferencesHeader);
			verify.assertion(preferencesHeader,"Preferences");
			
			String notifyTextHeader = mobileActions.getTextDataFromUI(androidDriver,RH_OnBoarding_Constants.preferencesHeader,"name");
			System.out.println("notifyTextHeader:"+notifyTextHeader);
			verify.assertion(notifyTextHeader,"NOTIFY");
			
			if(((String)gettcdata.get("favorites")).equalsIgnoreCase("Yes")) {
				mobileActions.click(androidDriver,RH_OnBoarding_Constants.notificationSettings,"When someone favorites you");
			}
			
			if(((String)gettcdata.get("message")).equalsIgnoreCase("Yes")) {
				mobileActions.click(androidDriver,RH_OnBoarding_Constants.notificationSettings,"When someone messages you");
			}
			
			mobileActions.setImplicityWait(androidDriver,20);	
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.continueButton);
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}

}
