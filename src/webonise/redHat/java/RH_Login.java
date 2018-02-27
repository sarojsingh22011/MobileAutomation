package webonise.redHat.java;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.android.AndroidDriver;
import webonise.automation.core.MobileActions;
import webonise.automation.core.WeboAutomation;
import webonise.automation.core.verification.MobileVerification;
import webonise.automation.core.verification.Verification;
import webonise.redHat.constants.RH_Common_Constants;

public class RH_Login extends WeboAutomation{
	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	MobileVerification mobileVerification = new MobileVerification();

	public RH_Login() throws Exception 
	{
		super();
		initalizeReport(this.getClass().getName());
		PageFactory.initElements(androidDriver,RH_Common_Constants.class);
	} 

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
	
	public static void valid_login(AndroidDriver androidDriver,MobileActions mobileActions,String username , String password) {
		try {
			mobileActions.click(androidDriver, RH_Common_Constants.loginButton);
			mobileActions.click(androidDriver, RH_Common_Constants.userNameTextbox);
			mobileActions.sendKeys(androidDriver,RH_Common_Constants.userNameTextbox, username);
			mobileActions.hideKeyboard(androidDriver);
			mobileActions.click(androidDriver, RH_Common_Constants.passwordTextbox);
			mobileActions.sendKeys(androidDriver,RH_Common_Constants.passwordTextbox, password);
			mobileActions.hideKeyboard(androidDriver);
			mobileActions.click(androidDriver, RH_Common_Constants.loginButton2);
			mobileActions.setImplicityWait(androidDriver,1);
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	protected static void login(AndroidDriver androidDriver,MobileActions mobileActions,Verification verify,String username,String password) {
		try {
			mobileActions.setImplicityWait(androidDriver,5);
			if(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.signInButton).size()>0){
				mobileActions.click(androidDriver, RH_Common_Constants.signInButton);
				mobileActions.setImplicityWait(androidDriver,1);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
				}
			}
			else if(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.hamburgMenu).size()>0){
				mobileActions.setImplicityWait(androidDriver,3);
				mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
				if(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.sessionExpiry).size()>0){
						mobileActions.click(androidDriver, RH_Common_Constants.sessionExpiry);
						mobileActions.click(androidDriver, RH_Common_Constants.signInButton);
						mobileActions.setImplicityWait(androidDriver,1);
						while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
						}
				 }
				 else{
					mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
					Thread.sleep(1000);
					mobileActions.click(androidDriver, RH_Common_Constants.signInButton);
					mobileActions.setImplicityWait(androidDriver,1);
					while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
					}
				 }
			}
			else if(mobileActions.findMultipleElementsInList(androidDriver,  RH_Common_Constants.startButton).size()>0){
				//Walk Through -1
				String startButton = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.startButton, "name");
				verify.assertion(startButton,"start");
				mobileActions.click(androidDriver, RH_Common_Constants.startButton);
				//Walk Through -2
				String welcome = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitle, "name");
				verify.assertion(welcome,"Welcome");
				String welcomeMessage = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitleMessage, "name");
				verify.assertion(welcomeMessage,"Partner Link is your gateway to the Red Hat Partner Ecosystem.");
				mobileActions.click(androidDriver, RH_Common_Constants.nextButton);
				//Walk Through -3
				String registerDeals = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitle, "name");
				verify.assertion(registerDeals,"Register Deals");
				String registerDealsMessage = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitleMessage, "name");
				verify.assertion(registerDealsMessage,"Land business opportunities instantly with real time deal registrations.");
				mobileActions.click(androidDriver, RH_Common_Constants.nextButton);
				//Walk Through -4
				String connect = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitle, "name");
				verify.assertion(connect,"Connect");
				String connectMessage = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitleMessage, "name");
				verify.assertion(connectMessage,"Find and match with pre-vetted connections tailored to your strategic needs.");
				mobileActions.click(androidDriver, RH_Common_Constants.nextButton);
				//Walk Through -5
				String expand = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitle, "name");
				verify.assertion(expand,"Expand");
				String expandMessage = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitleMessage, "name");
				verify.assertion(expandMessage,"Grow your business network globally with geolocalised search.");
				mobileActions.click(androidDriver, RH_Common_Constants.nextButton);
				//Walk Through -6
				String message = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitle, "name");
				verify.assertion(message,"Message");
				String messageMessage = mobileActions.getTextDataFromUI(androidDriver, RH_Common_Constants.textTitleMessage, "name");
				verify.assertion(messageMessage,"Directly message partners to collaborate and exchange ideas.");
				mobileActions.click(androidDriver, RH_Common_Constants.nextButton);
				mobileActions.setImplicityWait(androidDriver,1);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
				}
			}
			else if(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.sessionExpiry).size()>0){
				mobileActions.click(androidDriver, RH_Common_Constants.sessionExpiry);
				mobileActions.click(androidDriver, RH_Common_Constants.signInButton);
				mobileActions.setImplicityWait(androidDriver,1);
				while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
				}
			}
			mobileActions.setImplicityWait(androidDriver,10);
			List<WebElement> loginText=androidDriver.findElementsByXPath(RH_Common_Constants.welcomeMessageTextOnLoginPage);
			String loginHeader= loginText.get(2).getAttribute("name");
			verify.assertion(loginHeader,"Log in to your Red Hat account");
			
			mobileActions.click(androidDriver, RH_Common_Constants.userNameTextbox);
			mobileActions.sendKeys(androidDriver,RH_Common_Constants.userNameTextbox,username);
			mobileActions.hideKeyboard(androidDriver);
			mobileActions.click(androidDriver, RH_Common_Constants.passwordTextbox);
			mobileActions.sendKeys(androidDriver,RH_Common_Constants.passwordTextbox, password);
			mobileActions.hideKeyboard(androidDriver);
			mobileActions.click(androidDriver, RH_Common_Constants.loginButton);
			mobileActions.setImplicityWait(androidDriver,1);
			while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			}
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}

	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void logout(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}

}
