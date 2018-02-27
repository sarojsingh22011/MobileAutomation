package webonise.redHat.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import webonise.automation.core.CommonUtility;
import webonise.automation.core.MobileActions;
import webonise.automation.core.WeboAutomation;
import webonise.automation.core.verification.MobileVerification;
import webonise.redHat.constants.*;

public class RH_Help extends WeboAutomation{

	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	MobileVerification mobileVerification = new MobileVerification();

	public RH_Help() throws Exception 
	{
		super();
		initalizeReport(this.getClass().getName());
		PageFactory.initElements(androidDriver,RH_Common_Constants.class);
		PageFactory.initElements(androidDriver,RH_Help_Constants.class);
	} 

	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void partnersupport(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			androidDriver = initializeMobileDrivers();
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			//RH_Login.login(androidDriver,mobileActions,verify,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			Thread.sleep(2000);
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			mobileActions.click(androidDriver, RH_Help_Constants.helpMenu);
			
			
			mobileActions.click(androidDriver, RH_Help_Constants.partnersupport);
			
			mobileActions.click(androidDriver, RH_Help_Constants.departmentDropDown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("department"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("department"));
			
			mobileActions.click(androidDriver, RH_Help_Constants.jobroledropdown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("jobrole"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("jobrole"));
			
			mobileActions.click(androidDriver, RH_Help_Constants.jobroledropdown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("jobrole"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("jobrole"));
			
			mobileActions.click(androidDriver, RH_Help_Constants.issuetyperopdown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("issuetype"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("issuetype"));
				
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);
			
			mobileActions.click(androidDriver, RH_Help_Constants.comments);
			mobileActions.sendKeys(androidDriver, RH_Help_Constants.comments, (String)gettcdata.get("comments"));
			mobileActions.hideKeyboard(androidDriver);
			
			
			
			
			mobileActions.click(androidDriver, RH_Help_Constants.followupdropdown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("followup"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("followup"));
			
			mobileActions.click(androidDriver, RH_Help_Constants.btn_submit);
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}

}
