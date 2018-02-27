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
import webonise.redHat.constants.*;

public class RH_Favorites extends WeboAutomation{
	static AndroidDriver androidDriver;
	MobileActions mobileActions = new MobileActions();
	HashMap gettcdata = new HashMap();
	MobileVerification mobileVerification = new MobileVerification();

	public RH_Favorites() throws Exception 
	{
		super();
		initalizeReport(this.getClass().getName());
		PageFactory.initElements(androidDriver,RH_Common_Constants.class);
		PageFactory.initElements(androidDriver,RH_Search_Constants.class);
		PageFactory.initElements(androidDriver,RH_Connections_Constants.class);
	} 


	@Test(dataProvider ="xml" , enabled = true , priority = 1)
	public void favorites(Integer iteration, Boolean expectedResult) {
		try {
			updateTCData(iteration);
		//	PageFactory.initElements(androidDriver,RH_Common_Constants.class);
		//	PageFactory.initElements(androidDriver,RH_Search_Constants.class);
		//	PageFactory.initElements(androidDriver,RH_Connections_Constants.class);
			
			gettcdata=(HashMap) getTestData();
			
			androidDriver = initializeMobileDrivers();
			
			WeboAutomation.extent.log(LogStatus.INFO, "Received android driver="+androidDriver);
			
			
			//RH_Login.login(androidDriver,mobileActions,verify,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			RH_Login.valid_login(androidDriver,mobileActions,(String)gettcdata.get("username"), (String)gettcdata.get("password"));
			
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver,RH_Search_Constants.find_connection_home);
			String header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
			verify.assertion(header,"CONNECTIONS");
			
			mobileActions.setImplicityWait(androidDriver,10);
			mobileActions.click(androidDriver,RH_Search_Constants.guided_search);

			String searchBy = (String) gettcdata.get("searchBy");
			String favoritePartnerName = null;
			String valuesToSearch = (String) gettcdata.get("valuesToSearch");
			String numberOfValuesToSearchArray[] = valuesToSearch.split(";");
			int numberOfValuesToSearch =numberOfValuesToSearchArray.length;
			String currentOptionToSearch = null;

			for(int searchCounter=0;searchCounter<numberOfValuesToSearch;searchCounter++) {
				switch(searchBy) {
				case "Accreditations":
					mobileActions.click(androidDriver, RH_Search_Constants.accreditationsOption);
					String currentOptionToSearchArray[] = numberOfValuesToSearchArray[searchCounter].split("#");
					currentOptionToSearch = currentOptionToSearchArray[1];
					if(currentOptionToSearch.contains(",")) {
						String numberOfSearchsToApply[] = currentOptionToSearch.split(",");
						for(int currentOptionSearchCounter=0;currentOptionSearchCounter<numberOfSearchsToApply.length;currentOptionSearchCounter++) {
							System.out.println(numberOfSearchsToApply[currentOptionSearchCounter]);
							mobileActions.click(androidDriver, RH_Search_Constants.selectOptionToSearch,numberOfSearchsToApply[currentOptionSearchCounter].trim());	
						}
						mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
						header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
						verify.assertion(header,"GUIDED SEARCH");

						mobileActions.click(androidDriver, RH_Search_Constants.advancedSearchFindConnectionsButton);
						//mobileActions.setImplicityWait(androidDriver,1);
					//	String totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
					//	System.out.println("totalConnectionsFound"+totalConnectionsFound);
						mobileActions.setImplicityWait(androidDriver,10);
						
						if(header.contains("guided")) {
							header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
							verify.assertion(header,"advanced search");
							mobileActions.click(androidDriver, RH_Common_Constants.cancelButton);
						} else {
							//List<WebElement> connectionsHeader=androidDriver.findElementsByXPath(RH_Connections_Constants.connectionsHeader);
							//String connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
							header = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.advancedSearchPageHeader, "name");
							verify.assertion(header,"CONNECTIONS");

							mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
							String viewType = (String) gettcdata.get("viewType"); 
							if(viewType.equalsIgnoreCase("List")) {
								
								String currentSelectedStatus = mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.viewListStatus, "checked");
								if(currentSelectedStatus.equals("true")) {
									mobileActions.click(androidDriver, RH_Search_Constants.listView);
									if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
										mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
									}
									List<WebElement> favoritePartnerNameList = mobileActions.findMultipleElementsInList(androidDriver, RH_Favorites_Constants.favoritePartnerNameFromList);
									System.out.println(favoritePartnerNameList.size());
									//favoritePartnerName = mobileActions.getTextDataFromUI(androidDriver, favoritePartnerNameList.get(0).toString(), "text");
									favoritePartnerName = favoritePartnerNameList.get(0).getAttribute("text");
									mobileActions.swipeUsingTouchActionLeftToRight(androidDriver, RH_Favorites_Constants.markAsFavoriteFromList, favoritePartnerName);
									Thread.sleep(5000);
									if(mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.favoritesArcMenu, "selected").equalsIgnoreCase("false"))
										mobileActions.click(androidDriver, RH_Favorites_Constants.favoritesArcMenu);
								}
							} else {
								
								
								String currentSelectedStatus = mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.viewCarouselStatus, "checked");
								if(!currentSelectedStatus.equals("true")) {
									mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
									if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
										mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
									}
									favoritePartnerName = mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.favoritePartnerName, "name");
									for(int screenSwipeCounter=1;screenSwipeCounter<=5;screenSwipeCounter++) {
										mobileActions.swipeBottomToTop(androidDriver);
									}
									System.out.println("done");
									mobileActions.click(androidDriver, RH_Favorites_Constants.arcViewButton);
									if(mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.favoritesArcMenu, "selected").equalsIgnoreCase("false"))
										mobileActions.click(androidDriver, RH_Favorites_Constants.favoritesArcMenu);
								}
							}

							for(int hamburgMenuCounter=1;hamburgMenuCounter<=3;hamburgMenuCounter++) {
								mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
							} 

							mobileActions.click(androidDriver, RH_Common_Constants.favoriteMenu);
							
							String favoritesHeader = mobileActions.getTextDataFromUI(androidDriver,RH_Favorites_Constants.favoritesHeader,"name");
							System.out.println("favoritesHeader"+favoritesHeader);
							verify.assertion(favoritesHeader,"Favorites");

							List<WebElement> nameOfThePersons = mobileActions.findMultipleElementsInList(androidDriver, RH_Favorites_Constants.favoritesList);
							boolean foundUser=false;
							for(int counter=0;counter<nameOfThePersons.size();counter++) {
								String currentNameFound = nameOfThePersons.get(counter).getAttribute("name");
								System.out.println(currentNameFound);
								if(favoritePartnerName.equalsIgnoreCase(currentNameFound)) {
									
									foundUser=true;
									String verificationType = (String) gettcdata.get("favoriteVerificationType");  
									if(verificationType.equalsIgnoreCase("Favorite-Unfavorite")) {
									mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
									mobileActions.click(androidDriver, RH_Common_Constants.searchMenu);
									mobileActions.click(androidDriver, RH_Search_Constants.naturalLanguageSearchTextBox);
									mobileActions.sendKeys(androidDriver,RH_Search_Constants.naturalLanguageSearchTextBox, favoritePartnerName.trim());
									Thread.sleep(5000);
									mobileActions.hideKeyboard(androidDriver);
									mobileActions.click(androidDriver,RH_Search_Constants.findConnectionsButton);

									//Total connections found
									//totalConnectionsFound = mobileActions.getTextDataFromUI(androidDriver, RH_Search_Constants.totalConnectionsFound, "name");
								//	System.out.println("totalConnectionsFound"+totalConnectionsFound);

								//	connectionsHeader=androidDriver.findElementsByXPath(RH_Connections_Constants.connectionsHeader);
								//	connectionsHeaderValue= connectionsHeader.get(0).getAttribute("name");
									verify.assertion(header,"CONNECTIONS");
									
									mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
									
									if(viewType.equalsIgnoreCase("List")) {
										
										String currentSelectedStatus = mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.viewListStatus, "checked");
										if(!currentSelectedStatus.equals("true")) {
											mobileActions.click(androidDriver, RH_Search_Constants.listView);
											if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
												mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
											}
											//List<WebElement> favoritePartnerNameList = mobileActions.findMultipleElementsInList(androidDriver, RH_Favorites_Constants.favoritePartnerNameFromList);
											//System.out.println(favoritePartnerNameList.size());
											//favoritePartnerName = mobileActions.getTextDataFromUI(androidDriver, favoritePartnerNameList.get(0).toString(), "name");
											mobileActions.swipeUsingTouchActionLeftToRight(androidDriver, RH_Favorites_Constants.markAsFavoriteFromList, favoritePartnerName);
											
											Thread.sleep(5000);
											if(mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.favoritesArcMenu, "selected").equalsIgnoreCase("false"))
												mobileActions.click(androidDriver, RH_Favorites_Constants.favoritesArcMenu);
											Thread.sleep(5000);
										}
									} else {
										
									String currentSelectedStatus = mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.viewCarouselStatus, "checked");
									if(!currentSelectedStatus.equals("true")) {
										mobileActions.click(androidDriver, RH_Search_Constants.carouselView);
										if(mobileActions.findMultipleElementsInList(androidDriver, RH_Search_Constants.carouselView).size()>0){
											mobileActions.click(androidDriver,RH_Search_Constants.settingsButton);
										}
										favoritePartnerName = mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.favoritePartnerName, "name");
										System.out.println(favoritePartnerName);
										for(int screenSwipeCounter=1;screenSwipeCounter<=5;screenSwipeCounter++) {
											mobileActions.swipeBottomToTop(androidDriver);
										}
										mobileActions.click(androidDriver, RH_Favorites_Constants.arcViewButton);
										if(mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.favoritesArcMenu, "selected").equalsIgnoreCase("false"))
											mobileActions.click(androidDriver, RH_Favorites_Constants.favoritesArcMenu);
										Thread.sleep(5000);
									}
									}
								} else if(verificationType.equalsIgnoreCase("Favorite-List-Unfavorite")) {
									mobileActions.swipeUsingTouchActionRightToLeft(androidDriver, RH_Favorites_Constants.scrollToMarkAsUnfavoriteFromMenu, favoritePartnerName);
									List<WebElement> favoritePartnerNameList = mobileActions.findMultipleElementsInList(androidDriver, RH_Favorites_Constants.favoritePartnerNameFromList);
									
									
									favoritePartnerName = favoritePartnerName;
									
									for (WebElement fav_per: favoritePartnerNameList) {
									    if(fav_per.getText().equals(favoritePartnerName)) 
									    {
									    	fav_per.click();
									    	Thread.sleep(1000);
									    	mobileActions.click(androidDriver, RH_Favorites_Constants.arcViewButton);
									    	Thread.sleep(1000);
									    	mobileActions.click(androidDriver, RH_Favorites_Constants.favoritesArcMenu);
									    	verify.verifyHeading("unmarked from favorite", "unmarked from favorite");
									    	
									    }
									}

									/*mobileActions.swipeUsingTouchActionLeftToRight(androidDriver, RH_Favorites_Constants.markAsFavoriteFromList, favoritePartnerName);
									Thread.sleep(5000);
									if(mobileActions.getTextDataFromUI(androidDriver, RH_Favorites_Constants.favoritesArcMenu, "selected").equalsIgnoreCase("false"))
										mobileActions.click(androidDriver, RH_Favorites_Constants.favoritesArcMenu);
									System.out.println("222222");
									Thread.sleep(5000);
									mobileActions.click(androidDriver, RH_Favorites_Constants.markAsUnFavorite);*/
								}
									break;
								} 
							}
						/*	if(foundUser){
								verify.verifyHeading("User MArked Fav", "User not Marked Fav");
							} */
						}
					} 
					break;
				}
			}
			mobileActions.click(androidDriver, RH_Common_Constants.hamburgMenu);
			mobileActions.click(androidDriver, RH_Common_Constants.logoutMenu);
		}catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed="+e.getMessage());
			Assert.assertTrue(false);
		}
	}
}
