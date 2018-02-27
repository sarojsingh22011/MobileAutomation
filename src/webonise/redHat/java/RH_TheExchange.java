package webonise.redHat.java;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import webonise.automation.core.CommonUtility;
import webonise.automation.core.MobileActions;
import webonise.automation.core.WeboAutomation;
import webonise.automation.core.verification.MobileVerification;
import webonise.redHat.constants.RH_Common_Constants;
import webonise.redHat.constants.RH_Connections_Constants;
import webonise.redHat.constants.RH_DealRegistration_Constants;
import webonise.redHat.constants.RH_Search_Constants;
import webonise.redHat.constants.RH_TheExchange_Constants;

public class RH_TheExchange extends WeboAutomation{

	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	MobileVerification mobileVerification = new MobileVerification();
	String firstName,lastName,email,companyName,redHatSubscriptionRevenue,userType;
	List<WebElement> completedTab;
	
	public RH_TheExchange() throws Exception 
	{
		super();
		initalizeReport(this.getClass().getName());
		PageFactory.initElements(androidDriver,RH_Common_Constants.class);
		PageFactory.initElements(androidDriver,RH_Search_Constants.class);
		PageFactory.initElements(androidDriver,RH_Connections_Constants.class);
		PageFactory.initElements(androidDriver,RH_DealRegistration_Constants.class);
		completedTab=new ArrayList<WebElement>();
	} 

	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void createSeeking(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			mobileActions.setImplicityWait(androidDriver,15);
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,15);
			//mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			//Thread.sleep(1000);
			//mobileActions.click(androidDriver, RH_Common_Constants.dealMenu);
			//Thread.sleep(1000);
			//mobileActions.setImplicityWait(androidDriver,1);
			//while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			//}
			
			//To click on The Exchange
			mobileActions.click(androidDriver, RH_TheExchange_Constants.theExchange);
			mobileActions.setImplicityWait(androidDriver,3);
			//To click on Submit New Deal
			mobileActions.click(androidDriver, RH_TheExchange_Constants.createListing);
			mobileActions.setImplicityWait(androidDriver,3);
			
			//while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			//}
			
			//To verify Create a listing title
			String createListingHeader = mobileActions.getTextDataFromUI(androidDriver,RH_TheExchange_Constants.createListingHeader,"name");
			System.out.println("createListingHeader = "+createListingHeader);
			verify.assertion(createListingHeader,"Create A Listing");
			userType=(String)gettcdata.get("userType");
			
			//To click on About Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.about);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To click on Seeking button
			mobileActions.click(androidDriver, RH_TheExchange_Constants.seekingButton);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To select service engagement check box
			mobileActions.click(androidDriver, RH_TheExchange_Constants.serviceEngagement);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To click on About Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.about);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			// To click on Listing Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.listing);
			mobileActions.setImplicityWait(androidDriver, 7);
			
			//To enter Seeking Title
			mobileActions.sendKeys(androidDriver, RH_TheExchange_Constants.seekingTitle,(String)gettcdata.get("seekingTitle"));
			mobileActions.hideKeyboard(androidDriver);
			
			//To enter Your Posting
			mobileActions.sendKeys(androidDriver, RH_TheExchange_Constants.yourPosting,(String)gettcdata.get("yourPosting"));
			mobileActions.hideKeyboard(androidDriver);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To enter URL
			mobileActions.sendKeys(androidDriver, RH_TheExchange_Constants.addUrl,(String)gettcdata.get("addUrl"));
			mobileActions.hideKeyboard(androidDriver);
			
			//To click on Expertise Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.expertise);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To select Big Data check box
			mobileActions.click(androidDriver, RH_TheExchange_Constants.bigData);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To click on Industry Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.industry);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To select Aerospace
			mobileActions.click(androidDriver, RH_TheExchange_Constants.aerospace);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To click on Preview Listing button
			mobileActions.click(androidDriver, RH_TheExchange_Constants.previewListing);
			mobileActions.setImplicityWait(androidDriver, 5);
			
			//To click on List It button
			mobileActions.click(androidDriver, RH_TheExchange_Constants.listIt);
			mobileActions.setImplicityWait(androidDriver, 5);								
			
			androidDriver.quit();
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	

	@Test(dataProvider ="xml" , enabled = true , priority = 2)
	public void createOffering(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			mobileActions.setImplicityWait(androidDriver,15);
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,15);
			//mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			//Thread.sleep(1000);
			//mobileActions.click(androidDriver, RH_Common_Constants.dealMenu);
			//Thread.sleep(1000);
			//mobileActions.setImplicityWait(androidDriver,1);
			//while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			//}
			
			//To click on The Exchange
			mobileActions.click(androidDriver, RH_TheExchange_Constants.theExchange);
			mobileActions.setImplicityWait(androidDriver,3);
			//To click on Submit New Deal
			mobileActions.click(androidDriver, RH_TheExchange_Constants.createListing);
			mobileActions.setImplicityWait(androidDriver,3);
			
			//while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			//}
			
			//To verify Create a listing title
			String createListingHeader = mobileActions.getTextDataFromUI(androidDriver,RH_TheExchange_Constants.createListingHeader,"name");
			System.out.println("createListingHeader = "+createListingHeader);
			verify.assertion(createListingHeader,"Create A Listing");
			userType=(String)gettcdata.get("userType");
			
			//To click on About Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.about);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To click on Offering button
			mobileActions.click(androidDriver, RH_TheExchange_Constants.offeringButton);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To select service engagement check box
			mobileActions.click(androidDriver, RH_TheExchange_Constants.serviceEngagement);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To click on About Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.about);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			// To click on Listing Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.listing);
			mobileActions.setImplicityWait(androidDriver, 7);
			
			//To enter Seeking Title
			mobileActions.sendKeys(androidDriver, RH_TheExchange_Constants.seekingTitle,(String)gettcdata.get("seekingTitle"));
			mobileActions.hideKeyboard(androidDriver);
			
			//To enter Your Posting
			mobileActions.sendKeys(androidDriver, RH_TheExchange_Constants.yourPosting,(String)gettcdata.get("yourPosting"));
			mobileActions.hideKeyboard(androidDriver);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To enter URL
			mobileActions.sendKeys(androidDriver, RH_TheExchange_Constants.addUrl,(String)gettcdata.get("addUrl"));
			mobileActions.hideKeyboard(androidDriver);
			
			//To click on Expertise Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.expertise);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To select Big Data check box
			mobileActions.click(androidDriver, RH_TheExchange_Constants.bigData);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To click on Industry Accordion
			mobileActions.click(androidDriver, RH_TheExchange_Constants.industry);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To select Aerospace
			mobileActions.click(androidDriver, RH_TheExchange_Constants.aerospace);
			mobileActions.setImplicityWait(androidDriver, 3);
			
			//To click on Preview Listing button
			mobileActions.click(androidDriver, RH_TheExchange_Constants.previewListing);
			mobileActions.setImplicityWait(androidDriver, 5);
			
			//To click on List It button
			mobileActions.click(androidDriver, RH_TheExchange_Constants.listIt);
			mobileActions.setImplicityWait(androidDriver, 5);								
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	
	

}
