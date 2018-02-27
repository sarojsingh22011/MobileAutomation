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

public class RH_Deals extends WeboAutomation{

	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	MobileVerification mobileVerification = new MobileVerification();
	String firstName,lastName,email,companyName,redHatSubscriptionRevenue,userType;
	List<WebElement> completedTab;
	
	public RH_Deals() throws Exception 
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
	public void dealRegistration(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			
			androidDriver = initializeMobileDrivers();
		
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);
			//mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			//Thread.sleep(1000);
			//mobileActions.click(androidDriver, RH_Common_Constants.dealMenu);
			//Thread.sleep(1000);
			//mobileActions.setImplicityWait(androidDriver,1);
			//while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			//}
			
			//To click on Deal Registration
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.dealRegistration);
			mobileActions.setImplicityWait(androidDriver,3);
			//To click on Submit New Deal
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.submitNewDeal);
			mobileActions.setImplicityWait(androidDriver,3);
			
			//while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			//}
			
			//To verify Deal Registration Title  
			String dealRegistrationHeader = mobileActions.getTextDataFromUI(androidDriver,RH_DealRegistration_Constants.dealRegistrationHeader,"name");
			System.out.println("dealRegistrationHeader = "+dealRegistrationHeader);
			verify.assertion(dealRegistrationHeader,"Deal Registration");
			userType=(String)gettcdata.get("userType");
			
			// To click on Customer Information Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.customerInformation);
			if(userType.equalsIgnoreCase("NA"))
			{
				//To enter first name
				firstName=CommonUtility.getNames();			
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.firstName,firstName );
				mobileActions.hideKeyboard(androidDriver);
			}
			
			//To enter last name
			lastName=CommonUtility.getNames();
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.lastName,lastName );
			mobileActions.hideKeyboard(androidDriver);
			
			//To enter email address
			email=CommonUtility.getEmailAddress();
			if(userType.equalsIgnoreCase("NA")){
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.email,email );
				mobileActions.hideKeyboard(androidDriver);
			}
			
			//To select decision role
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.decisionRoleDropDown);
			List<WebElement> decisionRoleOptions = mobileActions.findMultipleElementsInList(androidDriver,RH_DealRegistration_Constants.decisionRoleDropDownValues);
			System.out.println("decisionRoleOptions"+decisionRoleOptions.size());
			for(int i=0; i< decisionRoleOptions.size(); i++)
			{
				MobileElement listItem = (MobileElement) decisionRoleOptions.get(i);   
				System.out.println(listItem.getText());
				if(listItem.getText().equalsIgnoreCase((String)gettcdata.get("decisionRole"))) 
				{
					System.out.println("Found");
					mobileActions.click(androidDriver, RH_DealRegistration_Constants.selectDecisionRole,(String)gettcdata.get("decisionRole"));
					break;
				}
			}  
			
			//To swipe mobile screen vertically upwards
			if(userType.equalsIgnoreCase("NA")){
			mobileActions.swipingVertical(androidDriver);
			}						
			//To enter company name
			companyName=CommonUtility.getNames();
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.companyName, companyName);			
			mobileActions.hideKeyboard(androidDriver);
			
			//To select country of order for EMEA user
			if(userType.equalsIgnoreCase("EMEA")){
				
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.countryOfOrderDropDownEMEA);
				mobileActions.scrollTo(androidDriver,(String)gettcdata.get("countryOfOrder"));
				mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("countryOfOrder"));
				mobileActions.scrollBottom(androidDriver, "Customer Email *");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.email);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.email,email );
				mobileActions.hideKeyboard(androidDriver);
				mobileActions.swipingVertical(androidDriver);
				Thread.sleep(3000);
				System.out.println("After swipe");
			}
		
				
			//To select country for NA user
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.countryDropDown);
			mobileActions.scrollTo(androidDriver,((String)gettcdata.get("country")));
			//mobileActions.scrollEvent(androidDriver, "xpath#//androimobileActions.swipingVertical(androidDriver);d.widget.TextView[@text='"+(String)gettcdata.get("country")+"']");
			mobileActions.click(androidDriver,"name#"+(String)gettcdata.get("country"));
			System.out.println("After country");
			//To select state/province
			//mobileActions.scrollBottom(androidDriver, "state/province *");		
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.stateDropDown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("state"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("state"));
				
			//To select country of order
			if(userType.equalsIgnoreCase("NA")){
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.countryOfOrderDropDown);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("countryOfOrder"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("countryOfOrder"));
			}
			//To click on Opportunity Details Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.opportunityDetails);
			
			//To swipe mobile screen vertically upwards
			
			
			//To select Deal Type for NA user
			if(userType.equalsIgnoreCase("NA"))
			{
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.dealTypeDropDown);
				mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("dealType"));
				mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("dealType"));
			}
			
			//To select product family
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.productFamilyDropDown);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("productFamily"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("productFamily"));
			
			//To enter redHat Subscription Revenue
			if(userType.equalsIgnoreCase("NA")){
				redHatSubscriptionRevenue=(String)gettcdata.get("redHatSubscriptionRevenue");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.redhatSubscriptionRevenue);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.redhatSubscriptionRevenue, redHatSubscriptionRevenue);
				mobileActions.hideKeyboard(androidDriver);
			}
			
			//To select distributor
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.distributorDropDown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("distributor"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("distributor"));					
			
			//For EMEA user
			if(userType.equalsIgnoreCase("EMEA"))
			{
				//To select lead currency for EMEA user
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadCurrencyDropdown);
				mobileActions.scrollTo(androidDriver, (String)gettcdata.get("leadCurrency"));
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadCurrencySelect,(String)gettcdata.get("leadCurrency"));
				
				//To enter deal registration amount
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.dealRegistrationAmt);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.dealRegistrationAmt, (String)gettcdata.get("dealRegistrationAmt"));
				mobileActions.hideKeyboard(androidDriver);
				
				//To select estimated close date
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.estimatedCloseDate);
				//mobileActions.click(androidDriver, RH_DealRegistration_Constants.calendarFirstDate);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.calendarDoneButton);
			}
			
			//For NA user
			if(userType.equalsIgnoreCase("NA"))
			{
				//To select estimated close date
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.estimatedCloseDate);
				//mobileActions.click(androidDriver, RH_DealRegistration_Constants.calendarFirstDate);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.calendarDoneButton);
				
				//To swipe mobile screen vertically upwards
				mobileActions.swipingVertical(androidDriver);
			
				//To select they have funding for this quarter?
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.fundingForThisQuarterDropDown);
				mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("doTheyHaveFundingForThisQuarter"));
				mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("doTheyHaveFundingForThisQuarter"));
				
				//To select funding source
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.fundingSource);
				mobileActions.scrollToExact(androidDriver, (String)gettcdata.get("fundingSource"));
				mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("fundingSource"));
				
				//To select they have open source strategy?				
				mobileActions.scrollBottom(androidDriver, "they have open source strategy? *");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.openSourceStrategyDropDown);
				mobileActions.scrollTo(androidDriver,(String)gettcdata.get("doTheyHaveOpenSourceStrategy"));
				mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("doTheyHaveOpenSourceStrategy"));
			}					
			
			//To click on Partner Information accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerInformation);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);
			
			//For NA user
			if(userType.equalsIgnoreCase("NA"))
			{
				//To select are You The Sales Rep For This Lead?
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.salesPerForLead);
				mobileActions.scrollTo(androidDriver,(String)gettcdata.get("areYouTheSalesRepForThisLead"));
				mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("areYouTheSalesRepForThisLead"));
			}
			
			//For EMEA user
			else
			{
				//mobileActions.scrollBottom(androidDriver, "Partner Program Participation");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerProgram);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerProgramSelect,(String)gettcdata.get("partnerProgramSelect"));
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerProgramDone);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerFirstName);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.partnerFirstName, (String)gettcdata.get("partnerFirstName"));
				mobileActions.hideKeyboard(androidDriver);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerLastName);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.partnerLastName, (String)gettcdata.get("partnerLastName"));
				mobileActions.hideKeyboard(androidDriver);
			}
			
			//To click on Project Description Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.projectDescription);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To enter project description
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.projectDescriptionText, (String)gettcdata.get("projectDescription"));
			mobileActions.hideKeyboard(androidDriver);
	
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);
			
			//For EMEA user
			if(userType.equalsIgnoreCase("EMEA"))
			{
				//mobileActions.scrollBottom(androidDriver, "Products of interest *");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.productInterestDropdown);
				String[] productInterest=((String) gettcdata.get("productInterest")).split(",");
				for(int i=0;i<productInterest.length;i++){
					mobileActions.scrollTo(androidDriver, productInterest[i]);
					mobileActions.click(androidDriver, RH_DealRegistration_Constants.productInterestSelect,productInterest[i]);
				}
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.productDoneButton);
			}
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To click on Lead Notes Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadNotes);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);			
			
			//For NA user
			if(userType.equalsIgnoreCase("NA"))
			{
				//To select Products of interest
				//mobileActions.scrollBottom(androidDriver, "Products of interest *");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.productsOfInterest);
				String productInterestName=RH_DealRegistration_Constants.productsofInterestCheckbox.replace("^", (String)gettcdata.get("productsOfInterest"));
				mobileActions.click(androidDriver, productInterestName);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.doneCheckboxButton);
				
				//To select OEM/Hardware Options
				//mobileActions.scrollBottom(androidDriver, "Oem/hardware options *");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.hardwareOptions);
				mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("oemHardwareOptions"));
				mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("oemHardwareOptions"));
			}
			
			//For EMEA user
			else
			{
				//mobileActions.scrollBottom(androidDriver, "EMEA Lead Source *");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.emeaLeadSource);
				mobileActions.scrollTo(androidDriver, (String)gettcdata.get("emeaLeadSource"));
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.emeaLeadSourceSelect, (String)gettcdata.get("emeaLeadSource"));
				
				//mobileActions.scrollBottom(androidDriver, "EMEA Lead Source sub options *");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.emeaLeadSourceSubOption);
				mobileActions.scrollTo(androidDriver, (String)gettcdata.get("emeaLeadSourceSubOption"));
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.emeaLeadSourceSubOptionSelect, (String)gettcdata.get("emeaLeadSourceSubOption"));
			}
			
			//To click on Submit Button
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.submitDeal);
			mobileActions.setImplicityWait(androidDriver,10);
			
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}

	//===================================================================================
	
	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void dealRegistration_emea(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			
			androidDriver = initializeMobileDrivers();
		
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);
			
			
			//To click on Deal Registration
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.dealRegistration);
			mobileActions.setImplicityWait(androidDriver,3);
			//To click on Submit New Deal
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.submitNewDeal);
			mobileActions.setImplicityWait(androidDriver,3);
			
						
			//To verify Deal Registration Title  
			String dealRegistrationHeader = mobileActions.getTextDataFromUI(androidDriver,RH_DealRegistration_Constants.dealRegistrationHeader,"name");
			System.out.println("dealRegistrationHeader = "+dealRegistrationHeader);
			verify.assertion(dealRegistrationHeader,"Deal Registration");
			userType=(String)gettcdata.get("userType");
			
			// To click on Customer Information Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.customerInformation);
			
			//To enter last name
			lastName=CommonUtility.getNames();
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.lastName,lastName );
			mobileActions.hideKeyboard(androidDriver);
			
						
			//To select decision role
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.decisionRoleDropDown);
			List<WebElement> decisionRoleOptions = mobileActions.findMultipleElementsInList(androidDriver,RH_DealRegistration_Constants.decisionRoleDropDownValues);
			System.out.println("decisionRoleOptions"+decisionRoleOptions.size());
			for(int i=0; i< decisionRoleOptions.size(); i++)
			{
				MobileElement listItem = (MobileElement) decisionRoleOptions.get(i);   
				System.out.println(listItem.getText());
				if(listItem.getText().equalsIgnoreCase((String)gettcdata.get("decisionRole"))) 
				{
					System.out.println("Found");
					mobileActions.click(androidDriver, RH_DealRegistration_Constants.selectDecisionRole,(String)gettcdata.get("decisionRole"));
					break;
				}
			}  
			
								
			//To enter company name
			companyName=CommonUtility.getNames();
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.companyName, companyName);			
			mobileActions.hideKeyboard(androidDriver);
			
			
									
			//To select country of order
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.countryOfOrderDropDown);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("countryOfOrder"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("countryOfOrder"));
			
			
			//To enter email address
				email=CommonUtility.getEmailAddress();
				//mobileActions.scrollBottom(androidDriver, "Customer Email *");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.email);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.email,email );
				
				
				mobileActions.hideKeyboard(androidDriver); 
				Thread.sleep(2000); 
				
				mobileActions.swipingVertical(androidDriver);
				//Thread.sleep(3000);
				System.out.println("After swipe");
			
		
			mobileActions.setImplicityWait(androidDriver, 30);
			//To select country for NA user
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.countryDropDown);
			System.out.println("After click");
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("country"));
			System.out.println("After scrollToExact");
			mobileActions.click(androidDriver,"name#"+(String)gettcdata.get("country"));
			System.out.println("After country");
			
			
			
			//To select state/province
			//mobileActions.scrollBottom(androidDriver, "state/province *");		
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.stateDropDown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("state"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("state"));  
				
			
			//To click on Opportunity Details Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.opportunityDetails);
			
		
			
			
			//To select product family
		mobileActions.click(androidDriver, RH_DealRegistration_Constants.productFamilyDropDown);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("productFamily"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("productFamily"));
			//To select lead currency for EMEA user
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadCurrencyDropdown);
			mobileActions.scrollTo(androidDriver, (String)gettcdata.get("leadCurrency"));
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadCurrencySelect,(String)gettcdata.get("leadCurrency"));
			
			//To enter deal registration amount
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.dealRegistrationAmt);
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.dealRegistrationAmt, (String)gettcdata.get("dealRegistrationAmt"));
			mobileActions.hideKeyboard(androidDriver);
			
			Thread.sleep(1000);  
						
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);
			
			//To select estimated close date
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.estimatedCloseDate);
			//mobileActions.click(androidDriver, RH_DealRegistration_Constants.calendarFirstDate);
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.calendarDoneButton);
			
			//To select distributor
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.distributorDropDown);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("distributor"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("distributor"));	
			
			//To select distributor
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.fundingsourceEMEA);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("fundingsource"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("fundingsource"));	
			
			//For EMEA user
			
			
			//To click on Partner Information accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerInformation);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);
			
			
			
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerProgram);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerProgramSelect,(String)gettcdata.get("partnerProgramSelect"));
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerProgramDone);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerFirstName);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.partnerFirstName, (String)gettcdata.get("partnerFirstName"));
				mobileActions.hideKeyboard(androidDriver);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerLastName);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.partnerLastName, (String)gettcdata.get("partnerLastName"));
				mobileActions.hideKeyboard(androidDriver);
		
			
			//To click on Project Description Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.projectDescription);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To enter project description
		mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.projectDescriptionText, (String)gettcdata.get("projectDescription"));
			mobileActions.hideKeyboard(androidDriver);
	
			
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);
			
			//For EMEA user
		
				
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.productInterestDropdown);
				String[] productInterest=((String) gettcdata.get("productInterest")).split(",");
				for(int i=0;i<productInterest.length;i++){
					mobileActions.scrollTo(androidDriver, productInterest[i]);
					mobileActions.click(androidDriver, RH_DealRegistration_Constants.productInterestSelect,productInterest[i]);
				}
				
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.productDoneButton); 
				
				//To click on Project Description Accordion
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.projectDescription);
		
				
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To click on Lead Notes Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadNotes);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);			
			
		
			//For EMEA user
				
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.emeaLeadSource);
				mobileActions.scrollTo(androidDriver, (String)gettcdata.get("emeaLeadSource"));
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.emeaLeadSourceSelect, (String)gettcdata.get("emeaLeadSource"));
				
				
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.emeaLeadSourceSubOption);
				mobileActions.scrollTo(androidDriver, (String)gettcdata.get("emeaLeadSourceSubOption"));
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.emeaLeadSourceSubOptionSelect, (String)gettcdata.get("emeaLeadSourceSubOption"));
			
				//.sleep(1000);
	/*			mobileActions.scrollTo(androidDriver,"Lead Notes");
				//To click on Lead Notes Accordion
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadNotes);
				//androidDriver.manage().window().getPosition().move(100, 1024);
				mobileActions.scrollTo(androidDriver,"Additional Information");
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.aditionalInfoTab);
			//	androidDriver.swipe(100, 1024, 100, 1024, 1000);
			//	mobileActions.swipeTopToBottom(androidDriver);
				mobileActions.swipingVertical(androidDriver);
				//To click on Lead Notes Accordion
				
			System.out.println("clicked additional tabbbbbbbb");
				//Thread.sleep(1000);
				mobileActions.swipingVertical(androidDriver);
				System.out.println("clicked additional tabbbbbbbb and swipedddddd");
				//To click on Lead Notes Accordion
				//mobileActions.click(androidDriver, RH_DealRegistration_Constants.aditionalInfoTab);
				
				//To swipe mobile screen vertically upwards
				
				mobileActions.setImplicityWait(androidDriver, 30);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerComments);
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.partnerComments, (String)gettcdata.get("partnerComments"));
				mobileActions.hideKeyboard(androidDriver);
				*/
				
			//To click on Submit Button
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.submitDeal);
			mobileActions.setImplicityWait(androidDriver,10);
			
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	
	//===================================================================================
	//APAC user deal registration
	//===================================================================================
	
	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void dealRegistration_apac(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			
			androidDriver = initializeMobileDrivers();
		
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);
			
			//To click on Deal Registration
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.dealRegistration);
			mobileActions.setImplicityWait(androidDriver,3);
			//To click on Submit New Deal
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.submitNewDeal);
			mobileActions.setImplicityWait(androidDriver,3);
			
			//while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			//}
			
			//To verify Deal Registration Title  
			String dealRegistrationHeader = mobileActions.getTextDataFromUI(androidDriver,RH_DealRegistration_Constants.dealRegistrationHeader,"name");
			System.out.println("dealRegistrationHeader = "+dealRegistrationHeader);
			verify.assertion(dealRegistrationHeader,"Deal Registration");
			userType=(String)gettcdata.get("userType");
			
			// To click on Customer Information Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.customerInformation);
			
			
			//To enter last name
			lastName=CommonUtility.getNames();
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.lastName,lastName );
			mobileActions.hideKeyboard(androidDriver);
			
			
			
			//To select decision role
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.decisionRoleDropDown);
			List<WebElement> decisionRoleOptions = mobileActions.findMultipleElementsInList(androidDriver,RH_DealRegistration_Constants.decisionRoleDropDownValues);
			System.out.println("decisionRoleOptions"+decisionRoleOptions.size());
			for(int i=0; i< decisionRoleOptions.size(); i++)
			{
				MobileElement listItem = (MobileElement) decisionRoleOptions.get(i);   
				System.out.println(listItem.getText());
				if(listItem.getText().equalsIgnoreCase((String)gettcdata.get("decisionRole"))) 
				{
					System.out.println("Found");
					mobileActions.click(androidDriver, RH_DealRegistration_Constants.selectDecisionRole,(String)gettcdata.get("decisionRole"));
					break;
				}
			}  
			
			
			//To enter company name
			companyName=CommonUtility.getNames();
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.companyName, companyName);			
			mobileActions.hideKeyboard(androidDriver);
			
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.countryOfOrderDropDownEMEA);
			mobileActions.scrollTo(androidDriver,(String)gettcdata.get("countryOfOrder"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("countryOfOrder"));
			
			
			//To enter email address
			email=CommonUtility.getEmailAddress();
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.email,email );
			mobileActions.hideKeyboard(androidDriver);
			
			
			
			//To swipe mobile screen vertically upwards
			
			mobileActions.swipingVertical(androidDriver);
			Thread.sleep(1000);
			
			//To select lead currency for EMEA user
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadCurrencyDropdown);
			mobileActions.scrollTo(androidDriver, (String)gettcdata.get("leadCurrency"));
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadCurrencySelect,(String)gettcdata.get("leadCurrency"));
								
			//To click on Opportunity Details Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.opportunityDetails);
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.dealTypeDropDown);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("dealType"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("dealType"));
			
			
			
			//To select product family
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.productFamilyDropDown);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("productFamily"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("productFamily"));
			
			mobileActions.swipingVertical(androidDriver);	
			//To enter deal registration amount
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.projectBudgetAmt);
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.projectBudgetAmt, (String)gettcdata.get("dealRegistrationAmt"));
			mobileActions.hideKeyboard(androidDriver);
			
			
			mobileActions.swipingVertical(androidDriver);	
			redHatSubscriptionRevenue=(String)gettcdata.get("redHatSubscriptionRevenue");
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.redhatSubscriptionRevenue);
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.redhatSubscriptionRevenue, redHatSubscriptionRevenue);
			mobileActions.hideKeyboard(androidDriver);
			
			
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.estimatedCloseDate);
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.calendarDoneButton);
			
			///////////////partner info in  apac/////////////////////
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To click on Project Description Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerInformation);
			
			//To select contact customer
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerContactCustomer);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("partnerContactCustomer"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("partnerContactCustomer"));
			
			//To select additional sales
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerAdditionalSupport);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("partnerAdditionalSupport"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("partnerAdditionalSupport"));
			
			mobileActions.swipingVertical(androidDriver);	
			
			/////////////////////////////////////end partner info apac////
			//To click on Project Description Accordion
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.projectDescription);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To enter project description
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.projectDescriptionText, (String)gettcdata.get("projectDescription"));
			mobileActions.hideKeyboard(androidDriver);
	
		
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);
			
			//To swipe mobile screen vertically upwards
			//mobileActions.swipingVertical(androidDriver);	
			
			//To click on Lead Notes Accordion
			//mobileActions.click(androidDriver, RH_DealRegistration_Constants.leadNotes);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			
			//To select funding source
			/*mobileActions.click(androidDriver, RH_DealRegistration_Constants.fundingSource);
			mobileActions.scrollToExact(androidDriver, (String)gettcdata.get("fundingSource"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("fundingSource"));
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.productDoneButton); */
			
			//mobileActions.swipingVertical(androidDriver);
			
			//To click on Lead Notes Accordion
		/*	mobileActions.click(androidDriver, RH_DealRegistration_Constants.aditionalInfoTab);
			
			//To swipe mobile screen vertically upwards
			mobileActions.swipingVertical(androidDriver);	
			//mobileActions.scrollToExact(androidDriver,"PARTNER COMMENTS");
			
		//	Thread.sleep(1000);
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.partnerComments);
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.partnerComments, (String)gettcdata.get("partnerComments"));
			mobileActions.hideKeyboard(androidDriver); */
			
			//To click on Submit Button
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.submitDeal);
			mobileActions.setImplicityWait(androidDriver,10);		
					
			
		
			
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	//===================================================================================
	
	
	
	
	
	@Test(dataProvider ="xml" , enabled = true , priority = 2)
	public void draftEdit(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			

			androidDriver = initializeMobileDrivers();
			
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);
			userType=(String)gettcdata.get("userType");
			
			//To click on Deal Registration
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.dealRegistration);
			mobileActions.setImplicityWait(androidDriver,3);
			//To click on Submit New Deal
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.submitNewDeal);
			mobileActions.setImplicityWait(androidDriver,3);
			
			//while(mobileActions.findMultipleElementsInList(androidDriver, RH_Common_Constants.startProgressBar).size()>0){
			//}
			
			
				
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.saveDraft);
			Thread.sleep(2000);
			
			mobileActions.setImplicityWait(androidDriver,20);
			
			
			List<WebElement> draftHeading=androidDriver.findElementsByXPath(RH_DealRegistration_Constants.draftStatusHeading);
			
		//	List<WebElement> draftData=androidDriver.findElementsByXPath(RH_DealRegistration_Constants.draftStatusDisplayed);
			
			List<String> draftHeadingVerify=new ArrayList<String>();
			List<String> draftDataVerify=new ArrayList<String>();
			
			draftHeadingVerify.add((String)gettcdata.get("statusHeading"));
			draftHeadingVerify.add((String)gettcdata.get("nameHeading"));
			draftHeadingVerify.add((String)gettcdata.get("emailHeading"));
			draftHeadingVerify.add((String)gettcdata.get("companyHeading"));
			
			
			
			draftDataVerify.add("Draft");
	/*		if(userType.equalsIgnoreCase("NA")){
				draftDataVerify.add(firstName+" "+lastName);
			}
			else{
				draftDataVerify.add(lastName);
			}
			draftDataVerify.add(email);
			draftDataVerify.add(companyName);*/
			
			for(int i=0;i<draftHeadingVerify.size();i++){
				verify.verifyHeading(draftHeadingVerify.get(i), draftHeading.get(i+3).getAttribute("text"));
				//verify.verifyHeading(draftDataVerify.get(i), draftData.get(i).getAttribute("text"));
			}
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yy");
			verify.verifyHeading(sdf.format(date), mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.editDealDate, "text"));
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.draftEditBuitton);
			
			mobileActions.setImplicityWait(androidDriver,40);
			
			List<WebElement> presentCompletedTab=androidDriver.findElementsByXPath(RH_DealRegistration_Constants.completeTab);
			//verify.verifyHeading(Integer.toString(completedTab.size()), Integer.toString(presentCompletedTab.size()));
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.customerInformation);
			
			mobileActions.setImplicityWait(androidDriver,10);
			if(userType.equalsIgnoreCase("NA"))
			{
				
				firstName=CommonUtility.getNames();
				mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.firstName,firstName );
				
				mobileActions.hideKeyboard(androidDriver);
				
			}
			
			
			lastName=CommonUtility.getNames();
			mobileActions.sendKeys(androidDriver, RH_DealRegistration_Constants.lastName,lastName );
			mobileActions.hideKeyboard(androidDriver);
			
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.saveDraft);
			
			
			mobileActions.setImplicityWait(androidDriver,10);
			
			draftHeading=androidDriver.findElementsByXPath(RH_DealRegistration_Constants.draftStatusHeading);
			//draftData=androidDriver.findElementsByXPath(RH_DealRegistration_Constants.draftStatusDisplayed);
			
			draftDataVerify.clear();
			draftDataVerify.add("Draft");
			if(userType.equalsIgnoreCase("NA")){
				draftDataVerify.add(firstName+" "+lastName);
			}
			else{
				draftDataVerify.add(lastName);
			}
			draftDataVerify.add(email);
			draftDataVerify.add(companyName);
			for(int i=0;i<draftHeadingVerify.size();i++){
				verify.verifyHeading(draftHeadingVerify.get(i), draftHeading.get(i+3).getAttribute("text"));
				//verify.verifyHeading(draftDataVerify.get(i), draftData.get(i).getAttribute("text"));
			}
			verify.verifyHeading(sdf.format(date), mobileActions.getTextDataFromUI(androidDriver, RH_DealRegistration_Constants.editDealDate, "text"));
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.draftEditBuitton);
			mobileActions.setImplicityWait(androidDriver,1);
			
			mobileActions.setImplicityWait(androidDriver,10);
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.customerInformation);
			mobileActions.setImplicityWait(androidDriver,1);
			
			mobileActions.setImplicityWait(androidDriver,10);
			
			mobileActions.scrollBottom(androidDriver, "Country of order *");
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.countryOfOrderDropDown);
			mobileActions.scrollToExact(androidDriver,(String)gettcdata.get("countryOfOrder"));
			mobileActions.click(androidDriver, "name#"+(String)gettcdata.get("countryOfOrder"));
		
			mobileActions.setImplicityWait(androidDriver,1);
			
			mobileActions.setImplicityWait(androidDriver,10);
			
			mobileActions.click(androidDriver, RH_DealRegistration_Constants.saveDraft);
			mobileActions.setImplicityWait(androidDriver,1);
			
			mobileActions.setImplicityWait(androidDriver,10);
			String dealRegistrationHeader = mobileActions.getTextDataFromUI(androidDriver,RH_DealRegistration_Constants.dealRegistrationHeader,"name");
			redHatSubscriptionRevenue=(String)gettcdata.get("redHatSubscriptionRevenue");
			if(Integer.parseInt(redHatSubscriptionRevenue)<10000){
				verify.verifyHeading((String)gettcdata.get("dealRegistrationHeading"), dealRegistrationHeader);
				mobileActions.click(androidDriver, RH_DealRegistration_Constants.draftBackButton);
				Thread.sleep(2000);
			}
			else{
				verify.verifyHeading((String)gettcdata.get("myDealsHeading"), dealRegistrationHeader);
			}
		
			draftHeading=androidDriver.findElementsByXPath(RH_DealRegistration_Constants.draftStatusHeading);
			//draftData=androidDriver.findElementsByXPath(RH_DealRegistration_Constants.draftStatusDisplayed);
			verify.verifyHeading(draftHeadingVerify.get(0), draftHeading.get(3).getAttribute("text"));
			//verify.verifyHeading("Pending", draftData.get(0).getAttribute("text"));
			Thread.sleep(1000);
			
			mobileActions.setImplicityWait(androidDriver,10);
			
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			
			
			//mobileActions.swipingVertical(androidDriver);
			mobileActions.setImplicityWait(androidDriver,30);
			mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
			
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	@Test(dataProvider ="xml" , enabled = true , priority = 2)
	public void testmenu(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
			gettcdata=(HashMap) getTestData();
			

			androidDriver = initializeMobileDrivers();
			
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);
			
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			System.out.println("hiiiiii");
			mobileActions.swipingVertical(androidDriver);
			mobileActions.setImplicityWait(androidDriver,30);
			mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
}
