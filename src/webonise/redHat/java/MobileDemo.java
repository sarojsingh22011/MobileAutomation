package webonise.redHat.java;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.android.AndroidDriver;
import webonise.automation.core.Configuration;
import webonise.automation.core.MobileActions;
import webonise.automation.core.WeboAutomation;

public class MobileDemo extends WeboAutomation{
	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();


	public MobileDemo() throws Exception 
	{
 		super();
		System.out.println("Inside TEST CLASS!");
		initalizeReport(this.getClass().getName());
		
	} 

	
	@Test
	public void atlasTest() {
		try {
			System.out.println("Inside method atlas Test!!");
			androidDriver = initializeMobileDrivers();
			System.out.println("-----------------------------------------------"+androidDriver);
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);

	}catch (Exception e) {
		WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
		Assert.assertTrue(false);
	}
}
	
	
}
