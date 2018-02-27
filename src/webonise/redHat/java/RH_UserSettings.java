package webonise.redHat.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import webonise.automation.core.MobileActions;
import webonise.automation.core.WeboAutomation;
import webonise.automation.core.verification.MobileVerification;
import webonise.redHat.constants.RH_Common_Constants;
import webonise.redHat.constants.RH_OnBoarding_Constants;
import webonise.redHat.constants.RH_UserSettings_Constants;

public class RH_UserSettings extends WeboAutomation{

	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	MobileVerification mobileVerification = new MobileVerification();
	
	public RH_UserSettings() throws Exception 
	{
		super();
	    initalizeReport(this.getClass().getName());
		PageFactory.initElements(androidDriver,RH_Common_Constants.class);
		PageFactory.initElements(androidDriver,RH_UserSettings_Constants.class);
		PageFactory.initElements(androidDriver,RH_OnBoarding_Constants.class);
	} 
	
/*	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void valid_login(Integer iteration, Boolean expectedResult) {
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
		
		androidDriver = initializeMobileDrivers();
		WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
		
		RH_Login.login(androidDriver,mobileActions,verify,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
		
		mobileActions.setImplicityWait(androidDriver,10);
		}catch (Exception e) {
		}
	}
	*/
	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void valid_login(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
		
			mobileActions.click(androidDriver, RH_Common_Constants.loginButton);
			mobileActions.click(androidDriver, RH_Common_Constants.userNameTextbox);
			mobileActions.sendKeys(androidDriver,RH_Common_Constants.userNameTextbox, (String)gettcdata.get("username"));
			mobileActions.hideKeyboard(androidDriver);
			mobileActions.click(androidDriver, RH_Common_Constants.passwordTextbox);
			mobileActions.sendKeys(androidDriver,RH_Common_Constants.passwordTextbox, (String)gettcdata.get("password"));
			mobileActions.hideKeyboard(androidDriver);
			mobileActions.click(androidDriver, RH_Common_Constants.loginButton2);
			mobileActions.setImplicityWait(androidDriver,1);
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	
	@Test(dataProvider ="xml" , enabled = true , priority = 2)
	public void userSettings(Integer iteration, Boolean expectedResult) {
		try {
		updateTCData(iteration);
		gettcdata=(HashMap) getTestData();
		List<String> displayedDataVerify=new ArrayList<String>();
		String checkboxValue,presentCheckboxValue;
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.settingTab);
		Thread.sleep(1000);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		
		displayedDataVerify.add(ApiCall.userFirstName);
		displayedDataVerify.add(ApiCall.userLastName);
		displayedDataVerify.add(ApiCall.userEmail);
		displayedDataVerify.add(ApiCall.userPhone);
		//displayedDataVerify.add(ApiCall.socialLinks);
		
		for(int i=0;i<displayedDataVerify.size();i++){
			String dispalyedData=(androidDriver.findElementByXPath(RH_UserSettings_Constants.displayedData.replace("#",Integer.toString(i)))).getAttribute("text");
			verify.verifyMessage(displayedDataVerify.get(i), dispalyedData);
		}
		mobileActions.click(androidDriver, RH_UserSettings_Constants.photoClick);
		displayedDataVerify.clear();
		displayedDataVerify.add((String)gettcdata.get("photoPopUpFirstButtton"));
		displayedDataVerify.add((String)gettcdata.get("photoPopUpSecButtton"));
		displayedDataVerify.add((String)gettcdata.get("photoPopUpThirdButtton"));
		
		verify.verifyHeading((String)gettcdata.get("photoPopUpHeading"),androidDriver.findElementById(RH_UserSettings_Constants.photoPopupHeading).getAttribute("text"));
		List<WebElement> displayedData=androidDriver.findElementsById(RH_UserSettings_Constants.photoPopupDisplayedData);
		for(int i=0;i<displayedDataVerify.size();i++){
			verify.verifyMessage(displayedDataVerify.get(i), displayedData.get(i).getAttribute("text"));
		}
		displayedData.get(1).click();
		List<WebElement> menuButton=androidDriver.findElementsByXPath(RH_UserSettings_Constants.menuButton);
		menuButton.get(1).click();
		if(mobileActions.getTextDataFromUI(androidDriver, RH_UserSettings_Constants.listViewTxt, "text").equalsIgnoreCase("List view")){
			mobileActions.click(androidDriver, RH_UserSettings_Constants.listViewButton);
		}
		else{
			mobileActions.click(androidDriver, RH_UserSettings_Constants.listViewButton);
			menuButton=androidDriver.findElementsByXPath(RH_UserSettings_Constants.menuButton);
			menuButton.get(1).click();
			mobileActions.click(androidDriver, RH_UserSettings_Constants.listViewButton);
		}
		mobileActions.click(androidDriver, RH_UserSettings_Constants.selectPhoto);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.cropImageDoneButton);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		
		List<WebElement> checkboxs=androidDriver.findElementsByXPath(RH_UserSettings_Constants.checkboxSocial);
		for(int allCheckboxes=0;allCheckboxes<checkboxs.size();allCheckboxes++){
			String checkBoxValue=checkboxs.get(allCheckboxes).getAttribute("checked");
			String apiValue = null;
			switch(allCheckboxes){
			case 0:
				apiValue=String.valueOf(ApiCall.preferences.getBoolean("email"));
				break;
			case 1:
				apiValue=String.valueOf(ApiCall.preferences.getBoolean("phone"));
				break;
			case 2:
				apiValue=String.valueOf(ApiCall.preferences.getBoolean("social_links"));
				break;
			}

			if(checkBoxValue.equalsIgnoreCase(apiValue)){
				verify.verifyHeading("API Call checkbox Value", "Api Call checkbox value mismatch");
			}
		}
		checkboxValue=checkboxs.get(2).getAttribute("checked");
		checkboxs.get(2).click();
		mobileActions.click(androidDriver, RH_UserSettings_Constants.userSettingSaveButton);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_Common_Constants.dealMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.settingTab);
		Thread.sleep(1000);
		checkboxs=androidDriver.findElementsByXPath(RH_UserSettings_Constants.checkboxSocial);
		
		presentCheckboxValue=checkboxs.get(2).getAttribute("checked");
		if(presentCheckboxValue.equals(checkboxValue)){
			verify.verifyMessage("Saved", "Not Saved sussesfully");
		}
		
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
		
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	@Test(dataProvider ="xml" , enabled = true , priority = 2)
	public void myProfile(Integer iteration, Boolean expectedResult) {
		try {
		updateTCData(iteration);
		gettcdata=(HashMap) getTestData();
		
		Thread.sleep(2000);
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.settingTab);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.myProfileTab,"My Profile");
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.location);
		mobileActions.sendKeys(androidDriver,RH_OnBoarding_Constants.streetAddress , (String)gettcdata.get("stresstAddress"));
		mobileActions.hideKeyboard(androidDriver);
		
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.countryDropDown);
		mobileActions.scrollBottom(androidDriver, (String)gettcdata.get("country"));
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.countrySelect,(String)gettcdata.get("country"));
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.state);
		mobileActions.scrollTo(androidDriver, (String)gettcdata.get("state"));
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.stateSelect,(String)gettcdata.get("state"));
		
		mobileActions.sendKeys(androidDriver, RH_OnBoarding_Constants.city, (String)gettcdata.get("city"));
		mobileActions.hideKeyboard(androidDriver);
		mobileActions.sendKeys(androidDriver, RH_OnBoarding_Constants.postalCode, (String)gettcdata.get("postalCode"));
		mobileActions.hideKeyboard(androidDriver);
		mobileActions.scrollTo(androidDriver,"Location");
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.location);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.industry);
		String industriesToSelect[] = ((String) gettcdata.get("industry")).split(",");
		for(int industrySelectionCounter=0;industrySelectionCounter<industriesToSelect.length;industrySelectionCounter++) {
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.scrollTo(androidDriver,industriesToSelect[industrySelectionCounter]);
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.industryToSelect,industriesToSelect[industrySelectionCounter]);
		}
		mobileActions.scrollTo(androidDriver,"Industry");
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.industry);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.areaOfFocus);
		String areaOfFocussToSelect[] = ((String) gettcdata.get("areaOfFocus")).split(",");
		for(int areaOfFocusSelectionCounter=0;areaOfFocusSelectionCounter<areaOfFocussToSelect.length;areaOfFocusSelectionCounter++) {
			mobileActions.scrollTo(androidDriver,areaOfFocussToSelect[areaOfFocusSelectionCounter]);
			mobileActions.click(androidDriver, RH_OnBoarding_Constants.areaOfFocusToSelect,areaOfFocussToSelect[areaOfFocusSelectionCounter]);
		}
		mobileActions.scrollTo(androidDriver,"Area of Focus");
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.areaOfFocus);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
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
					mobileActions.setImplicityWait(androidDriver,10);
					mobileActions.click(androidDriver, RH_OnBoarding_Constants.selectInnerProduct,productsToSelectArray[innerProductSelectionCounter]);
					mobileActions.setImplicityWait(androidDriver,1);
					while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
					}
					mobileActions.setImplicityWait(androidDriver,10);
				}
				mobileActions.click(androidDriver, RH_OnBoarding_Constants.doneButton);
				mobileActions.setImplicityWait(androidDriver,1);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
				mobileActions.setImplicityWait(androidDriver,10);
			} else {
				mobileActions.click(androidDriver, RH_OnBoarding_Constants.selectInnerProduct,productsToSelectList);
				mobileActions.click(androidDriver, RH_OnBoarding_Constants.doneButton);
				mobileActions.setImplicityWait(androidDriver,1);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}
				mobileActions.setImplicityWait(androidDriver,10);
			}
		}
		mobileActions.scrollTo(androidDriver,"Red Hat Product Expertise");
		mobileActions.click(androidDriver, RH_OnBoarding_Constants.redHatProductExpertise);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.sendKeys(androidDriver, RH_OnBoarding_Constants.socialLinks, (String) gettcdata.get("socialLinkURL"));
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.hideKeyboard(androidDriver);
		mobileActions.scrollTo(androidDriver, "Is there anything else Red Hat partners should know about you?");
		mobileActions.sendKeys(androidDriver, RH_OnBoarding_Constants.aboutYou, (String) gettcdata.get("anythingElseAboutYou"));
		mobileActions.hideKeyboard(androidDriver);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.userSettingSaveButton);
		mobileActions.setImplicityWait(androidDriver,1);
		while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}
		mobileActions.setImplicityWait(androidDriver,10);
		
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);	
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	@Test(dataProvider ="xml" , enabled = true , priority=2)
	public void preferencesTab(Integer iteration, Boolean expectedResult) {
		try {
		updateTCData(iteration);
		gettcdata=(HashMap) getTestData();
		Thread.sleep(2000);
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.settingTab);
		mobileActions.setImplicityWait(androidDriver,1);
		/*while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}*/
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.myProfileTab,"Preferences");
		mobileActions.setImplicityWait(androidDriver,1);
		/*while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}*/
		mobileActions.setImplicityWait(androidDriver,10);
		
		String notifyTextHeader = mobileActions.getTextDataFromUI(androidDriver,RH_UserSettings_Constants.notifyHeader,"text");
		System.out.println("notifyTextHeader:"+notifyTextHeader);
		verify.assertion(notifyTextHeader,"NOTIFY");
		
		String presentData=mobileActions.getTextDataFromUI(androidDriver, RH_OnBoarding_Constants.notificationSettings.replace("*","When someone favorites you"),"checked");
		
		
		
	//	verify.verifyHeading(String.valueOf(ApiCall.preferences.getBoolean("favorite_notification")), presentData);
		presentData=mobileActions.getTextDataFromUI(androidDriver, RH_OnBoarding_Constants.notificationSettings.replace("*","When someone messages you"),"checked");
	//	verify.verifyHeading(String.valueOf(ApiCall.preferences.getBoolean("message_notification")), presentData);
		
		String favChecked,msgChecked;
		if(((String)gettcdata.get("favorites")).equalsIgnoreCase("Yes")) {
			favChecked="true";
			if(mobileActions.getTextDataFromUI(androidDriver, RH_OnBoarding_Constants.notificationSettings.replace("*","When someone favorites you"),"checked").equals("false")){
				mobileActions.click(androidDriver,RH_OnBoarding_Constants.notificationSettings,"When someone favorites you");
				mobileActions.setImplicityWait(androidDriver,1);
				/*while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}*/
				mobileActions.setImplicityWait(androidDriver,10);
			}
		}
		else{
			favChecked="false";
			if(mobileActions.getTextDataFromUI(androidDriver, RH_OnBoarding_Constants.notificationSettings.replace("*","When someone favorites you"),"checked").equals("true")){
				mobileActions.click(androidDriver,RH_OnBoarding_Constants.notificationSettings,"When someone favorites you");
				mobileActions.setImplicityWait(androidDriver,1);
				/*while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}*/
				mobileActions.setImplicityWait(androidDriver,10);
			}
		}
		if(((String)gettcdata.get("message")).equalsIgnoreCase("Yes")) {
			msgChecked="true";
			if(mobileActions.getTextDataFromUI(androidDriver, RH_OnBoarding_Constants.notificationSettings.replace("*","When someone messages you"),"checked").equals("false")){
				mobileActions.click(androidDriver,RH_OnBoarding_Constants.notificationSettings,"When someone messages you");
				mobileActions.setImplicityWait(androidDriver,1);
				/*while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}*/
				mobileActions.setImplicityWait(androidDriver,10);
			}
		}
		else{
			msgChecked="false";
			if(mobileActions.getTextDataFromUI(androidDriver, RH_OnBoarding_Constants.notificationSettings.replace("*","When someone messages you"),"checked").equals("true")){
				mobileActions.click(androidDriver,RH_OnBoarding_Constants.notificationSettings,"When someone messages you");
				
				/*while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
				}*/
				mobileActions.setImplicityWait(androidDriver,10);
			}
		}
		
		/*while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.progressBar).size()>0){
		}*/
		mobileActions.setImplicityWait(androidDriver,10);
		mobileActions.click(androidDriver, RH_UserSettings_Constants.userSettingSaveButton);
		mobileActions.setImplicityWait(androidDriver,1);
		
		mobileActions.setImplicityWait(androidDriver,10);
		verify.verifyHeading(favChecked, mobileActions.getTextDataFromUI(androidDriver, RH_OnBoarding_Constants.notificationSettings.replace("*","When someone favorites you"),"checked"));
		verify.verifyHeading(msgChecked, mobileActions.getTextDataFromUI(androidDriver, RH_OnBoarding_Constants.notificationSettings.replace("*","When someone messages you"),"checked"));
		
		mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
		Thread.sleep(1000);
		mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
		
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
}
