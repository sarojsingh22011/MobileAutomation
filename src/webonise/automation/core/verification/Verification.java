package webonise.automation.core.verification;


import org.junit.Assert;

import com.relevantcodes.extentreports.LogStatus;

import webonise.automation.core.UserException;
import webonise.automation.core.WeboActions;
import webonise.automation.core.WeboAutomation;

public class Verification
{
	WeboActions actions;
	WeboAutomation weboAutomation = new WeboAutomation();
	
	public Verification(WeboActions actions) {
		this.actions=actions;
	}
	
	public boolean assertion(String expectedString,String actualString) {
		System.out.println("Expected:"+expectedString);
		System.out.println("Actual:"+actualString);
		try {
			if(!expectedString.equals(actualString)) {
				return false;
			}
		}catch(Exception e) {
			System.out.println("Exception:"+e);
		}
		return true;
		
	}
	public boolean checkTitle(String expected)
	{
		System.out.println("Title is        : " + actions.getTitle());
		System.out.println("Title should be : " + expected);
		if(!expected.equalsIgnoreCase(actions.getTitle()))
		{
			actions.takeScreenShot();
			WeboAutomation.extent.log(LogStatus.FAIL,"<br> Actual Title:"+actions.getTitle()+"<br> Expected Title:"+expected);
			return false;
		}
		return true;
	}

	public boolean verifyMessage(String expected,String actualText)
	{
		System.out.println("Message is        : " +actualText);
		System.out.println("Message should be : " +expected);
		if(!expected.equalsIgnoreCase(actualText))
		{
			actions.takeScreenShot();
			WeboAutomation.extent.log(LogStatus.FAIL,"<br> Actual Message:"+actualText+"<br> Expected Message:"+expected);
			Assert.fail();
			return false;
		}
		return true;
	}

	public boolean verifyHeading(String expected,String actualText)
	{
		System.out.println("Heading is        : " +actualText);
		System.out.println("Heading should be : " +expected);
		if(!expected.equalsIgnoreCase(actualText))
		{
			actions.takeScreenShot();
			WeboAutomation.extent.log(LogStatus.FAIL,"<br> Actual Heading:"+actualText+"<br> Expected Heading:"+expected);
			Assert.fail();return false;
		}
		return true;
	}

	public boolean verifyRegistration(String expected,String actualText)
	{
		actualText=actualText.replaceAll("\\s+"," ");
		System.out.println("Heading is        : " +actualText);
		System.out.println("Heading should be : " +expected);

		if(!expected.equalsIgnoreCase(actualText))
		{
			actions.takeScreenShot();
			return false;
		}
		return true;
	}

	public boolean verifyTextLength(String text,int length){

		if(text.length()!=length)
		{
			actions.takeScreenShot();
			return false;
		}
		return true;
	}
}
