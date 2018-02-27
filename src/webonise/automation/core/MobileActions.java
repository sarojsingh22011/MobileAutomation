package webonise.automation.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;
import com.sun.org.apache.xpath.internal.operations.And;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;
import webonise.redHat.constants.RH_Search_Constants;

public class MobileActions {
	Dimension size;
	TouchAction touchAction;
	static ATUTestRecorder recorder;

	public static void startAppium() throws ExecuteException, IOException, InterruptedException {
		try {
			System.out.println("In start appium");
			/*CommandLine command = new CommandLine("cmd");
			command.addArgument("/c");
			command.addArgument(Configuration.nodePath);
			command.addArgument(Configuration.appiumJavaScriptPath);
			command.addArgument("--address");
			command.addArgument("127.0.0.1");
			command.addArgument("--port"); 
			command.addArgument("4723");*/
			CommandLine command = new CommandLine("C:\\Program Files (x86)\\Appium\\node.exe");
			command.addArgument("C:\\Program Files (x86)\\Appium\\node_modules\\appium\\bin\\Appium.js");
			command.addArgument("--address");
			command.addArgument("127.0.0.1");
			command.addArgument("--port");
			command.addArgument("4723");
			if(Configuration.fullReset.equalsIgnoreCase("true")){
				command.addArgument("--full-reset");
			}
			command.addArgument("--log");
			command.addArgument(Configuration.path + "/appiumLogs.txt");
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler(); 
			DefaultExecutor executor = new DefaultExecutor(); 
			executor.setExitValue(1); 
			executor.execute(command, resultHandler);
			Thread.sleep(10000);
			for(int i=1;i<=10;i++) {
				System.out.println(i);
			 }
			System.out.println("@@@@@@@@@@");
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions startAppium()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			e.printStackTrace();
		}
	}

	public static void stopAppium() throws ExecuteException, IOException, InterruptedException 
	{
		try {
			CommandLine command = new CommandLine("cmd"); 
			command.addArgument("/c"); 
			command.addArgument("taskkill");
			command.addArgument("/F"); 
			command.addArgument("/IM");
			command.addArgument("node.exe");

			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1); executor.execute(command, resultHandler); 
			Thread.sleep(3000);
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions stopAppium()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			e.printStackTrace();
		}
	}

	public static void startRecording () throws ATUTestRecorderException {
		try{
			WeboAutomation.extent.log(LogStatus.INFO,"Video recording has been started.");
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy_HH-mm-ss"); 
			Date date = new Date();
			recorder = new ATUTestRecorder(Configuration.path+"\\ScriptVideos\\","RedHatVideo-"+dateFormat.format(date),false); 
			recorder.start(); 
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions startRecording()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			e.printStackTrace();
		}

	}

	public static void stopRecording () throws ATUTestRecorderException {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Video recording has been stopped.");
			recorder.stop();
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions stopRecording()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			e.printStackTrace();
		}
	}

	public void swipeLeftToRight(AndroidDriver androidDriver) throws InterruptedException {
		try{
			WeboAutomation.extent.log(LogStatus.INFO,"Swipped from left to right");
			System.out.println("Actions androdDriver="+androidDriver.toString());
			size = androidDriver.manage().window().getSize(); 
			int startx = (int) (size.width * 0.80); 
			int endx = (int) (size.width * 0.20); 
			int starty = size.height / 2; 
			androidDriver.swipe(startx, starty, endx, starty, 300);
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions swipeLeftToRight()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void swipeRightToLeft(AndroidDriver androidDriver) {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Swipped from right to left");
			System.out.println("Actions androdDriver="+androidDriver.toString());
			size = androidDriver.manage().window().getSize(); 
			int startx = (int) (size.width * 0.80); 
			int endx = (int) (size.width * 0.20); 
			int starty = size.height / 2; 
			androidDriver.swipe(endx, starty, startx, starty, 300);
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions swipeRightToLeft()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void swipeBottomToTop(AndroidDriver androidDriver) {
		try{
			WeboAutomation.extent.log(LogStatus.INFO,"Swipped from bottom to top");
			System.out.println("Actions androdDriver="+androidDriver.toString());
			size = androidDriver.manage().window().getSize(); 
			int starty = (int) (size.height * 0.80); 
			int endy = (int) (size.height * 0.20); 
			int startx = size.width / 2; 
			androidDriver.swipe(startx, starty, startx, endy, 1000);
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions swipeBottomToTop()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void swipeDown(AndroidDriver androidDriver, int by) {
		WeboAutomation.extent.log(LogStatus.INFO, "Swipped from bottom to top");
		System.out.println("Actions androdDriver=" + androidDriver.toString());
		size = androidDriver.manage().window().getSize();
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * 0.20);
		int startx = size.width / 2;
		androidDriver.swipe(startx, endy, startx, endy + by, 1000);
	}

	public void swipeUp(AndroidDriver androidDriver, int by) {
		WeboAutomation.extent.log(LogStatus.INFO, "Swipped from bottom to top");
		System.out.println("Actions androdDriver=" + androidDriver.toString());
		size = androidDriver.manage().window().getSize();
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * 0.20);
		int startx = size.width / 2;
		androidDriver.swipe(startx, starty, startx, starty - by, 1000);
	}
	
	public void swipeTopToBottom(AndroidDriver androidDriver) {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Swipped from bottom to top");
			System.out.println("Actions androdDriver="+androidDriver.toString());
			size = androidDriver.manage().window().getSize(); 
			int starty = (int) (size.height * 0.80); 
			int endy = (int) (size.height * 0.20); 
			int startx = size.width / 2; 
			androidDriver.swipe(startx, endy, startx, starty, 1000);
			
			//androidDriver.swipe(startx, starty, startx, endy, 1000);
			
			
			
			
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions swipeTopToBottom()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}
	
	public void swipeUsingTouchActionRightToLeft(AndroidDriver androidDriver,String byObj,String valueToBeReplaced) {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Swipped Element using TouchAction");
			touchAction  = new TouchAction((MobileDriver)androidDriver);
			size = androidDriver.manage().window().getSize(); 
			int x1 = (int) (size.width  * 0.10);
			//byObj = byObj.replace("*", valueToBeReplaced);
			WebElement webElement = getWebElement(androidDriver,byObj);
			touchAction.longPress(webElement).moveTo(x1,270).release().perform();
			
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions swipeUsingTouchAction()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}
	
	public void swipeUsingTouchActionLeftToRight(AndroidDriver androidDriver,String byObj,String valueToBeReplaced) {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Swipped Element using TouchAction");
			touchAction  = new TouchAction((MobileDriver)androidDriver);
			size = androidDriver.manage().window().getSize(); 
			int x1 = (int) (size.width  * 0.80);
			//byObj = byObj.replace("*", valueToBeReplaced);
			WebElement webElement = getWebElement(androidDriver,byObj);
			//touchAction.press(webElement).
			//touchAction.longPress(webElement).moveTo(x1,250).release().perform();
			int wide  = size.width;
			int hgt = size.height;
			
			int startx = (int) (wide * (0.2));
			int endx = (int)(wide *(0.8));
			int starty =  hgt /2 ;
		    int endy = hgt/2;
		    
		    //To move from Fav to all contacts, we need to swipe from right to left
		    androidDriver.swipe(startx, starty, endx, endy, 1000);
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions swipeUsingTouchAction()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void sendKeys(AndroidDriver androidDriver,String byObj, String value) throws InterruptedException {
		try{
			WeboAutomation.extent.log(LogStatus.INFO,"Inserting data = "+ value +".");
			WebElement webElement = getWebElement(androidDriver,byObj);
			if(!webElement.getAttribute("text").equals(""))
				webElement.clear();
			if(!webElement.getAttribute("text").equals(""))
				webElement.clear();
			webElement.sendKeys(value);
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions sendKeys()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void click(AndroidDriver androidDriver,String byObj) throws InterruptedException {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Clicked on element with xpath="+byObj);
			WebElement webElement = getWebElement(androidDriver,byObj);
			webElement.click();
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions click()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void click(AndroidDriver androidDriver,String byObj,String valueToReplace) throws InterruptedException {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Clicked on element");
			byObj = byObj.replace("*", valueToReplace);
			WeboAutomation.extent.log(LogStatus.INFO,"Clicked on element with xpath="+byObj);
			WebElement webElement = getWebElement(androidDriver,byObj);
			webElement.click();
		}
		catch(Exception extraSpace){
			try{
				if(valueToReplace.endsWith(" "))
					byObj = byObj.replace(valueToReplace,"*");
					click(androidDriver, byObj,valueToReplace.replaceAll("\\s+$", ""));
			}catch(Exception e) {
				WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions click()");
				WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
				takeScreenShot(androidDriver);
				e.printStackTrace();
		}
		}
	}

	public void scrollTo(AndroidDriver androidDriver,String textToScrollTo) throws InterruptedException {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Scrolled upto text:"+textToScrollTo);
			androidDriver.scrollTo(textToScrollTo);
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions scrollto()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void scrollToExact(AndroidDriver androidDriver,String textToScrollTo) throws InterruptedException {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Scrolled upto text:"+textToScrollTo);
			androidDriver.scrollToExact(textToScrollTo);
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions sendKeys()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void hideKeyboard(AndroidDriver androidDriver) throws InterruptedException {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Hide keyboard");
			androidDriver.hideKeyboard();
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions sendKeys()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}

	public void pause(AndroidDriver andriodDriver,int time) {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Waiting for time="+time);
			andriodDriver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
			Thread.sleep(2000);
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions sendKeys()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(andriodDriver);
			e.printStackTrace();
		}
	}

	public void setImplicityWait(AndroidDriver androidDriver,int time) {
		try {
			WeboAutomation.extent.log(LogStatus.INFO,"Waiting for time="+time);
			androidDriver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions sendKeys()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			e.printStackTrace();
		}
	}
	
	public String getTextDataFromUI(AndroidDriver androidDriver,String objectLocator,String attributeToBeFetched){
		String textToReturn =null;
		try {
			WebElement webElement = getWebElement(androidDriver,objectLocator);
			textToReturn = webElement.getAttribute(attributeToBeFetched);
			WeboAutomation.extent.log(LogStatus.INFO,"Fetched attribute value:"+textToReturn);
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions sendKeys()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
		return textToReturn;
	}

	public void takeScreenShot(AndroidDriver androidDriver) {
		String destDir=Configuration.path+"/Report/screenshots/";
		File f = new File(destDir);
		f.mkdirs();
		File scrFile = ((TakesScreenshot) androidDriver).getScreenshotAs(OutputType.FILE);
		DateFormat  dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
		new File(destDir).mkdirs();
		String destFile = dateFormat.format(new Date()) + ".png";
		WeboAutomation.extent.attachScreenshot(destDir+"\\" + destFile);
		try {
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));

		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions takeScreenShot()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}


	public WebElement getWebElement(AndroidDriver androidDriver,String objectLocator)
	{ 
		WebElement webelement = null;
		try {
			Thread.sleep(8000);
			if(objectLocator.contains("#")) {
				String elementToFetchArray[] = objectLocator.split("#");
				String elementToFetch = elementToFetchArray[1];
				switch(elementToFetchArray[0]) {
				case "xpath" :				webelement = androidDriver.findElementByXPath(elementToFetch);					break;
				case "id":					webelement =  androidDriver.findElementById(elementToFetch);					break;
				case "name" :	            webelement = androidDriver.findElementByName(elementToFetch);					break;
				case "accessibilityId": 	webelement = androidDriver.findElementByAccessibilityId(objectLocator);			break;
				case "androidUIAutomator":	webelement = androidDriver.findElementByAndroidUIAutomator(objectLocator);		break;
				case "className":			webelement = androidDriver.findElementByClassName(objectLocator);				break;
				case "cssSelector":			webelement = androidDriver.findElementByCssSelector(objectLocator);				break;
				case "linkText":			webelement = androidDriver.findElementByLinkText(objectLocator);				break;
				case "partialLinkText":		webelement = androidDriver.findElementByPartialLinkText(objectLocator);			break;
				case "tagName":				webelement = androidDriver.findElementByTagName(objectLocator);					break;
				}
			}
			
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions getWebElement()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
		return webelement;
	}
	
	public List<WebElement> findMultipleElementsInList(AndroidDriver androidDriver,String objectLocator)
	{ 
		List<WebElement> availableOptions =null;
		try {
			if(objectLocator.contains("#")) {
				String elementToFetchArray[] = objectLocator.split("#");
				String elementToFetch = elementToFetchArray[1];
				switch(elementToFetchArray[0]) {
				case "xpath" :				availableOptions = androidDriver.findElementsByXPath(elementToFetch);					break;
				case "id":					availableOptions =  androidDriver.findElementsById(elementToFetch);					    break;
				case "name" :	            availableOptions = androidDriver.findElementsByName(elementToFetch);					break;
				case "accessibilityId": 	availableOptions = androidDriver.findElementsByAccessibilityId(objectLocator);			break;
				case "androidUIAutomator":	availableOptions = androidDriver.findElementsByAndroidUIAutomator(objectLocator);		break;
				case "className":			availableOptions = androidDriver.findElementsByClassName(objectLocator);				break;
				case "cssSelector":			availableOptions = androidDriver.findElementsByCssSelector(objectLocator);				break;
				case "linkText":			availableOptions = androidDriver.findElementsByLinkText(objectLocator);				    break;
				case "partialLinkText":		availableOptions = androidDriver.findElementsByPartialLinkText(objectLocator);			break;
				case "tagName":				availableOptions = androidDriver.findElementsByTagName(objectLocator);					break;
				}
			}
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions findMultipleElementsInList()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
		return availableOptions;
	}

	public void keyBoardEvent(int eventNumber) 
	   {
	   try 
	   {
		Runtime.getRuntime().exec("cmd /C adb shell input keyevent " + eventNumber);
	    Thread.sleep(3000);
	    Runtime.getRuntime().exec("cmd /C adb shell input keyevent " + 4);
	    Thread.sleep(3000);   
	   } 
	   catch (Throwable t) 
	   {
	    t.printStackTrace();
	   }
	  }
	
	public void scrollEvent(AndroidDriver androidDriver,String objectLocator){
		List<WebElement> availableOptions =new ArrayList<>();
		try{
			while(availableOptions.size()==0){
				String elementToFetchArray[] = objectLocator.split("#");
				String elementToFetch = elementToFetchArray[1];
				switch(elementToFetchArray[0]) {
				case "xpath" :				availableOptions = androidDriver.findElementsByXPath(elementToFetch);					break;
				case "id":					availableOptions =  androidDriver.findElementsById(elementToFetch);					    break;
				case "name" :	            availableOptions = androidDriver.findElementsByName(elementToFetch);					break;
				case "accessibilityId": 	availableOptions = androidDriver.findElementsByAccessibilityId(objectLocator);			break;
				case "androidUIAutomator":	availableOptions = androidDriver.findElementsByAndroidUIAutomator(objectLocator);		break;
				case "className":			availableOptions = androidDriver.findElementsByClassName(objectLocator);				break;
				case "cssSelector":			availableOptions = androidDriver.findElementsByCssSelector(objectLocator);				break;
				case "linkText":			availableOptions = androidDriver.findElementsByLinkText(objectLocator);				    break;
				case "partialLinkText":		availableOptions = androidDriver.findElementsByPartialLinkText(objectLocator);			break;
				case "tagName":				availableOptions = androidDriver.findElementsByTagName(objectLocator);					break;
				}
				
				WeboAutomation.extent.log(LogStatus.INFO, "Swipped from bottom to top");
				System.out.println("Actions androdDriver=" + androidDriver.toString());
				size = androidDriver.manage().window().getSize();
				int starty = (int) (size.height * 0.80);
				int endy = (int) (size.height * 0.20);
				int startx = size.width / 2;
				androidDriver.swipe(startx, starty, startx, starty - 100, 1000);
			}
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions swipeBottomToTop()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}
	
	public int totalListCount(AndroidDriver androidDriver,String headingObjectLocator) throws InterruptedException{
		boolean scroll=true;
		List<String> headingData=new ArrayList<String>();
		while(scroll){
			List<WebElement> pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			List<String> pageElementsHeading=new ArrayList<String>();
			int count=0;
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				pageElementsHeading.add(heading);
				if(!headingData.contains(heading)){
					headingData.add(heading);
				}
			}
			swipeBottomToTop(androidDriver);
			Thread.sleep(2000);
			pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				if(pageElementsHeading.contains(heading)){
					count++;
				}
			}
			if(count==pageHeadingElements.size()){
				scroll=false;
			}
		}
		return headingData.size();
	}
	
	public int totalListCount(AndroidDriver androidDriver,String headingObjectLocator,String loadingObjectLocator) throws InterruptedException{
		boolean scroll=true;
		List<String> headingData=new ArrayList<String>();
		while(scroll){
			List<WebElement> pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			List<String> pageElementsHeading=new ArrayList<String>();
			int count=0;
			
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				pageElementsHeading.add(heading);
				if(!headingData.contains(heading)){
					headingData.add(heading);
				}
			}
			swipeBottomToTop(androidDriver);
			setImplicityWait(androidDriver, 1);
			while(findMultipleElementsInList(androidDriver, loadingObjectLocator).size()>0){
			}
			setImplicityWait(androidDriver, 10);
			pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				if(pageElementsHeading.contains(heading)){
					count++;
				}
				else{
					break;
				}
			}
			if(count==pageHeadingElements.size()){
				scroll=false;
			}
		}
		return headingData.size();
	}
	
	public List<String> totalListHeading(AndroidDriver androidDriver,String headingObjectLocator,String loadingObjectLocator) throws InterruptedException{
		boolean scroll=true;
		List<String> headingData=new ArrayList<String>();
		while(scroll){
			List<WebElement> pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			List<String> pageElementsHeading=new ArrayList<String>();
			int count=0;
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				pageElementsHeading.add(heading);
				if(!headingData.contains(heading)){
					headingData.add(heading);
				}
			}
			swipeBottomToTop(androidDriver);
			setImplicityWait(androidDriver, 1);
			while(findMultipleElementsInList(androidDriver, loadingObjectLocator).size()>0){
			}
			setImplicityWait(androidDriver, 10);
			pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				if(pageElementsHeading.contains(heading)){
					count++;
				}
				else{
					break;
				}
			}
			if(count==pageHeadingElements.size()){
				scroll=false;
			}
		}
		return headingData;
	}
	
	public List<String> totalListWithMatching(AndroidDriver androidDriver,String headingObjectLocator,String matchingObjectLocator,String loadingObjectLocator,List matchingCriteria) throws InterruptedException{
		boolean scroll=true;
		List<String> headingData=new ArrayList<String>();
		while(scroll){
			List<WebElement> pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			List<String> pageElementsHeading=new ArrayList<String>();
			List<WebElement> matchingElements=findMultipleElementsInList(androidDriver, matchingObjectLocator);
			int count=0;
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				pageElementsHeading.add(heading);
				if(!headingData.contains(heading)){
					headingData.add(heading);
				}
			}
			for(int i=0;i<matchingElements.size();i++){
				String matchingString=matchingElements.get(i).getAttribute("text");
				if(!matchingCriteria.contains(matchingString)){
					headingData=new ArrayList<String>();
					return headingData;
				}
			}
			swipeBottomToTop(androidDriver);
			setImplicityWait(androidDriver, 1);
			while(findMultipleElementsInList(androidDriver, loadingObjectLocator).size()>0){
			}
			setImplicityWait(androidDriver, 10);
			pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				if(pageElementsHeading.contains(heading)){
					count++;
				}
				else{
					break;
				}
			}
			if(count==pageHeadingElements.size()){
				scroll=false;
			}
		}
		return headingData;
	}
	
	public List<String> totalListWithMatching(AndroidDriver androidDriver,String headingObjectLocator,String matchingObjectLocator,List matchingCriteria) throws InterruptedException{
		boolean scroll=true;
		List<String> headingData=new ArrayList<String>();
		while(scroll){
			List<WebElement> pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			List<String> pageElementsHeading=new ArrayList<String>();
			List<WebElement> matchingElements=findMultipleElementsInList(androidDriver, matchingObjectLocator);
			int count=0;	
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				pageElementsHeading.add(heading);
				if(!headingData.contains(heading)){
					headingData.add(heading);
				}
			}
			for(int i=0;i<matchingElements.size();i++){
				String matchingString=matchingElements.get(i).getAttribute("text");
				if(!matchingCriteria.contains(matchingString)){
					headingData=new ArrayList<String>();
					return headingData;
				}
			}
			swipeBottomToTop(androidDriver);
			Thread.sleep(3000);
			pageHeadingElements=findMultipleElementsInList(androidDriver, headingObjectLocator);
			for(int i=0;i<pageHeadingElements.size();i++){
				String heading=pageHeadingElements.get(i).getAttribute("text");
				if(pageElementsHeading.contains(heading)){
					count++;
				}
				else{
					break;
				}
			}
			if(count==pageHeadingElements.size()){
				scroll=false;
			}
		}
		return headingData;
	}
	
	public void scrollBottom(AndroidDriver androidDriver,String textToScrollTo) throws InterruptedException {
		try {
			int count=0;
			WeboAutomation.extent.log(LogStatus.INFO,"Scrolled upto text:"+textToScrollTo);
			setImplicityWait(androidDriver, 1);
			while(count<30){
				try{
					androidDriver.findElementByAndroidUIAutomator("new UiSelector().text(\""+textToScrollTo+"\")");
					break;
				}
				catch(Exception elementNoFound){
				}
				swipeUp(androidDriver,400);
				count++;
			}
			setImplicityWait(androidDriver, 10);
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions scrollto()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}
	
	
	
	public void swipingVertical(AndroidDriver androiddriver) throws InterruptedException 
	{
		  //Get the size of screen.
		 Dimension size = androiddriver.manage().window().getSize();
		  System.out.println(size);

		  //Find swipe start and end point from screen's with and height.
		  //Find starty point which is at bottom side of screen.
		  int starty = (int) (size.height * 0.80);
		  //Find endy point which is at top side of screen.
		  int endy = (int) (size.height * 0.20);
		  //Find horizontal point where you wants to swipe. It is in middle of screen width.
		  int startx = size.width / 2;
		  System.out.println("starty = " + starty + " ,endy = " + endy + " , startx = " + startx);

		  //Swipe from Bottom to Top.
		  androiddriver.swipe(startx, starty, startx, endy, 3000);
		  Thread.sleep(2000);
		  
		  
		  //Swipe from Top to Bottom.
		  //driver.swipe(startx, endy, startx, starty, 3000);
		  //Thread.sleep(2000);		 
		  
	}	
	
	
	
	
	public void scrollTop(AndroidDriver androidDriver,String textToScrollTo) throws InterruptedException {
		try {
			int count=0;
			WeboAutomation.extent.log(LogStatus.INFO,"Scrolled upto text:"+textToScrollTo);
			setImplicityWait(androidDriver, 1);
			while(count<30){
				try{
					androidDriver.findElementByAndroidUIAutomator("new UiSelector().textContains(\""+textToScrollTo+"\")");
					break;
				}
				catch(NoSuchElementException elementNoFound){
				}
				swipeDown(androidDriver,400);
				count++;
			}
			setImplicityWait(androidDriver, 10);
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Exception in MobileActions scrollto()");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot(androidDriver);
			e.printStackTrace();
		}
	}
}
