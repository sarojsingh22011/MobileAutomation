package webonise.automation.core;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;
import com.relevantcodes.extentreports.LogStatus;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;


public class WeboActions {

	static WebDriver driver ;
	WeboAutomation webo;
	static String tc_name;
	static String path;
	String filename;
	By byObject = null;
	String originalHandle;
	CommonUtility utils;
	String dir;

	public WeboActions(CommonUtility utils, String dir) {
		this.utils=utils;
		this.dir=dir;
	}

	protected void setDriver(WebDriver mydriver){
		driver = mydriver;
	}

	public void launchBrowser(String url){
		try{
			driver.get(url);
			WeboAutomation.extent.log(LogStatus.INFO, "Launched browser with URL: " +  url);
			Reporter.log("Launched browser with URL " +  url,true);
			driver.manage().window().maximize();
			WeboAutomation.extent.log(LogStatus.INFO, "Window maximized");
			Reporter.log("Window maximized",true);
		}
		catch(Exception e)
		{
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to launch the URL:" +  url);
		}
	}

	public String getCurrentURL()
	{
		return	driver.getCurrentUrl();
	}

	public void navigateTo(String url){
		try {
			driver.navigate().to(url);
			WeboAutomation.extent.log(LogStatus.INFO, "Navigated to :" +  url);
			Reporter.log("navigate to " +  url,true);
		} catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to navigate to:" + url);
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();	
		}
	}

	public void selectDate(By byObj){
		try{
			List<WebElement> we =  driver.findElements(byObj);
			System.out.println("Size of array is " + we.size());
			for(int i=0;i< we.size();i++){
				System.out.println("Text of "+ i +" is " + we.get(i).getText());
				System.out.println("Enabled status of "+ i +" is " + we.get(i).isEnabled());
				System.out.println("Enabled status of "+ i +" is " + we.get(i).getAttribute("class"));
			}
			we.get(0).click();		
			WeboAutomation.extent.log(LogStatus.INFO,"Clicked on " + byObj.toString());
			Reporter.log("Clicked on " + byObj.toString(),true);
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Clicked on " + byObj.toString());
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void click(WebElement byObj){
		try{
			byObj.click();
			WeboAutomation.extent.log(LogStatus.INFO, "Clicked on: " + byObj.toString());
			Reporter.log("Clicked on " + byObj.toString(),true);
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to Clicke on: " + byObj.toString());
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void clickAndHold(WebElement byObj){
		try{
			org.openqa.selenium.interactions.Actions builder = new org.openqa.selenium.interactions.Actions(driver);
			builder.moveToElement(byObj).build().perform();
			WeboAutomation.extent.log(LogStatus.INFO, "Clicked and hold element: " + byObj.toString());
			Reporter.log("Clicked on " + byObj.toString(),true);
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to Click on " + byObj.toString());
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void selectFrame(WebElement byObj){
		try{
			originalHandle = driver.getWindowHandle();
			driver.switchTo().frame(byObj);
			WeboAutomation.extent.log(LogStatus.INFO, "Changed frame to:"+driver.getCurrentUrl());
			Reporter.log("Changed frame" ,true);
		}

		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to switch the frame.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void selectWindow(){
		try{
			driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
			WeboAutomation.extent.log(LogStatus.INFO, "Changed Window:");
			Reporter.log("Changed Window" ,true);
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to change the window");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void sendKeys(WebElement byObj, String textToSet){
		try{
			byObj.sendKeys(textToSet);
			String str = byObj.toString();
			WeboAutomation.extent.log(LogStatus.INFO, "Setting text :" + textToSet  +  " to " + str);
			Reporter.log("Setting text \"" + textToSet  + "\" on " + byObj.toString(),true);
		}
		catch(Exception e)
		{
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to set text " + byObj.toString()+"not found");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}


	public void sendKeys(WebElement byObj, String textToSet,Keys key){	
		try{
			byObj.sendKeys(textToSet,key);
			WeboAutomation.extent.log(LogStatus.INFO, "Setting text \"" + textToSet  + "\" on " + byObj.toString());
			Reporter.log("Setting text \"" + textToSet  + "\" on " + byObj.toString(),true);
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to set text " + byObj.toString()+"not found");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public String getText(WebElement byObj){
		String text = "";
		try{
			text=byObj.getText();
		}
		catch(Exception e)
		{
			WeboAutomation.extent.log(LogStatus.ERROR, "Element :" + byObj.toString()+" not found.Not able to get Text value for the element.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
		return text;
	}

	public String getTitle(){
		String text = "";
		try {
			text=driver.getTitle();
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Not able to get the title.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
		return text;
	}

	public void takeScreenShot(){
		WeboAutomation.extent.log(LogStatus.FAIL,driver.getCurrentUrl());
		Reporter.log("Take screenshot",true);
		try {
			attachSS();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void attachSS() throws IOException 
	{
		final Screenshot screenshot = new AShot().shootingStrategy(new ViewportPastingStrategy(500)).takeScreenshot(driver);
		final BufferedImage image = screenshot.getImage();

		String file=dir+"ss\\"+tc_name+"\\";
		filename=tc_name+CommonUtility.dateFormat("ss")+".jpg";
		File f = new File(file);
		f.mkdirs();
		ImageIO.write(image, "png", new File(dir+"ss\\"+tc_name+"\\" + tc_name+CommonUtility.dateFormat("ss")+".jpg"));
		WeboAutomation.extent.attachScreenshot(dir +"ss\\"+tc_name+"\\" + filename);
	}

	public void selectbyVisibilityOfText(WebElement byObj, String textToSet){
		try{
			Select element = new Select(byObj);
			element.selectByVisibleText(textToSet);
			System.out.println(textToSet+" text selected");
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Element " + byObj.toString()+"not found");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void selectByValue(WebElement byObj, String textToSet){
		try{
			Select element = new Select(byObj);
			element.selectByValue(textToSet);
			System.out.println(textToSet+" text selected");
		}
		catch(Exception e)
		{
			WeboAutomation.extent.log(LogStatus.ERROR, "Element " + byObj.toString()+"not found");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void clear(WebElement byObj){
		try{
			byObj.clear();
		}
		catch(org.openqa.selenium.NoSuchElementException e)
		{
			WeboAutomation.extent.log(LogStatus.ERROR, "Element " + byObj.toString()+"not found.Not able to clear the contents.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public boolean chkVisible(WebElement byObj){
		try{
			byObj.isDisplayed();
			Reporter.log("Verified the visibility of the element:" + byObj.toString(),true);
			return true;
		}
		catch(Exception e){
			Reporter.log("Not able to verify the visibility of the element:" + byObj.toString(),true);
			WeboAutomation.extent.log(LogStatus.ERROR, "Not visible" + byObj.toString());
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
		}
		return false;
	}

	public boolean isPresent(WebElement byObj,String expectedString){
		try{
			String elementFound = byObj.getText();
			WeboAutomation.extent.log(LogStatus.INFO, "Visible element found:" + elementFound);
			Reporter.log("Visible element found:" + elementFound,true);
			if(elementFound.equalsIgnoreCase(expectedString)) {
				return true;
			} else {
				return false;	
			}
		}
		catch(Exception e){
			Reporter.log("Element not present" + byObj.toString(),true);
			WeboAutomation.extent.log(LogStatus.INFO, "Not visible" + byObj.toString());
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");			
		}
		return false;
	}


	public boolean chkEnabled(WebElement byObj){
		try{
			byObj.isEnabled();
			WeboAutomation.extent.log(LogStatus.INFO, "Element enabled= " + byObj.toString());
			Reporter.log("Enable element" + byObj.toString(),true);
			return true;
		}
		catch(Exception e){
			WeboAutomation.extent.log(LogStatus.ERROR, "Element Not enabled" + byObj.toString());
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
		}
		return false;
	}

	public void openNewTab() throws AWTException
	{
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
	}

	public void switchToNewTab() {
		try {
			originalHandle = driver.getWindowHandle();
			driver.switchTo().window(driver.getWindowHandles().toArray()[(1)].toString());
			Reporter.log("Chnaged frame"+ driver.getTitle(),true);
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Not able to switch to new tab");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
		}
	}

	public void switchBack() {
		try {
			for(String handle : driver.getWindowHandles()) {
				if (!handle.equals(originalHandle)) {
					driver.switchTo().window(handle);
					driver.close();
				}
			}
			driver.switchTo().window(originalHandle);
			Reporter.log("Chnaged frame"+ driver.getTitle(),true);
		} catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Not able to switch back to the original window");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
		}
	}

	public static void  setTCNameToFile(String name){

		tc_name=name;				
	}

	public static String getLocalPath() {
		try {
			path = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public void sign(By byObj)
	{
		org.openqa.selenium.interactions.Actions builder = new org.openqa.selenium.interactions.Actions(driver);  // Configure the Action  
		WebElement signatureElement=driver.findElement(byObj);
		org.openqa.selenium.interactions.Action mouseOver1 =builder.moveToElement(signatureElement,40,20).clickAndHold().moveByOffset(20,20).release().build();
		mouseOver1.perform();
		org.openqa.selenium.interactions.Action mouseOver2 =builder.moveToElement(signatureElement,20,20).clickAndHold().moveByOffset(10, 30).release().build();
		mouseOver2.perform();
	}

	public void switchToAlertAndAccept(){
		try
		{
			driver.switchTo().alert().accept();
			WeboAutomation.extent.log(LogStatus.INFO, "clicked on OK on alert box");
			Reporter.log("clicked on OK on alert box" ,true);
		}
		catch(Exception e)
		{
			WeboAutomation.extent.log(LogStatus.ERROR, "No Alert present ");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void Wait()
	{
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	public void defaultGUIWait()
	{
		driver.manage().timeouts().implicitlyWait(Configuration.defaultGUIWaitPeriod, TimeUnit.SECONDS);
	}

	public void waitForTitle() throws InterruptedException
	{
		WebDriverWait wait=new WebDriverWait(driver,10);
		wait.withTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

	}

	public void waitForItems(final By byObj,int time) throws InterruptedException
	{
		WebElement element = (new WebDriverWait(driver, time)).until(new
				ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				return d.findElement(byObj);
			}
		});
	}

	public void refreshPage()
	{
		driver.navigate().refresh();
		System.out.println("Page Refreshed");
	}

	public List getContents(WebElement byObj)
	{
		List<WebElement> cells = null;
		List<WebElement> allRows=byObj.findElements(By.tagName("td"));
		return allRows;
	}

	public void dragDrop(WebElement drag,WebElement drop) 
	{
		try {
			org.openqa.selenium.interactions.Actions dragdrop;
			org.openqa.selenium.interactions.Actions act = new org.openqa.selenium.interactions.Actions(driver);
			dragdrop = act.clickAndHold(drag).moveToElement(drop).release(drop);  
			dragdrop.build().perform();
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Faild to drag and drop from one location to other.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void waitForItemToBeClickable(By byObj,int waitTime) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(byObj));
	}

	public void waitForItemToBeVisible(By byObj,int waitTime) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(byObj));
	}


	public void presenceOfElementLocated(By byObj,int waitTime) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.presenceOfElementLocated(byObj));
	}

	public void scrollToHorizontal(WebElement element)
	{

		Actions dragger=new Actions(driver);
		WebElement draggablePartOfScrollbar = element;

		int numberOfPixelsToDragTheScrollbarDown = 50;
		for (int i=10;i<500;i=i+numberOfPixelsToDragTheScrollbarDown)
		{
			try
			{  
				dragger.moveToElement(draggablePartOfScrollbar).clickAndHold().moveByOffset(numberOfPixelsToDragTheScrollbarDown,0).release().perform();
				Thread.sleep(1000L);
			} catch(Exception e1){
				WeboAutomation.extent.log(LogStatus.ERROR, "Failed to scroll horizontally.");
				WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e1.toString() +"</pre>");
				takeScreenShot();
				e1.printStackTrace();
			}
		}
	}

	public void scrollToVertical(WebElement element)
	{

		Actions dragger=new Actions(driver);
		WebElement draggablePartOfScrollbar = element;

		int numberOfPixelsToDragTheScrollbarDown = 50;
		for (int i=10;i<500;i=i+numberOfPixelsToDragTheScrollbarDown)
		{
			try
			{  
				dragger.moveToElement(draggablePartOfScrollbar).clickAndHold().moveByOffset(0,numberOfPixelsToDragTheScrollbarDown).release().perform();
				Thread.sleep(1000L);
			} catch(Exception e1){
				WeboAutomation.extent.log(LogStatus.ERROR, "Failed to scroll vertically.");
				WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e1.toString() +"</pre>");
				takeScreenShot();
				e1.printStackTrace();
			}
		}

	}

	public void checkChkBox(WebElement element) {
		try {
			boolean isCheckBoxSet;
			isCheckBoxSet = element.isSelected();
			if (!isCheckBoxSet) {
				element.click();
			}
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to check/uncheck checkbox.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void javaScriptClick(WebElement element) {
		try {
			((JavascriptExecutor) driver).executeScript("return arguments[0].click();", element);
		} catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to click on element using java script executor.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	public void actionsClick(WebElement element) {
		try {
			Actions builder = new Actions(driver);
			builder.moveToElement(element).build().perform(); 
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to click on element using actions class.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}


	public void mouseHover(WebElement element)
	{
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).perform();
		} catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to execute mouse hover.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}


	public void mouseHoverUsingJavaScript(WebElement element)
	{
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		((JavascriptExecutor) driver).executeScript(mouseOverScript,element);
	}

	public void selectRandomOptionFromDropDown(WebElement element) throws InterruptedException 
	{
		try {
			Select elementSelect = new Select(element);
			List<WebElement> allOptions = elementSelect.getOptions();
			for(WebElement option: allOptions) {
				String value= option.getText();
				System.out.println("Value"+value);
			}
			elementSelect.selectByIndex(new Random().nextInt(elementSelect.getOptions().size()));
		} catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to select random value from drop down.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}


	public String selectRandomOptionFromCombo(WebElement selectCombo) throws InterruptedException
	{
		String selectedOption = "";
		Thread.sleep(2);
		List<WebElement> getAllOption = selectCombo.findElements(By.xpath("option"));
		ArrayList<String> arrayOfAllOption=new ArrayList<String>();
		for(WebElement ele:getAllOption)
		{
			if(!ele.getText().startsWith("All"))
			{
				arrayOfAllOption.add(ele.getText());
			}
		}
		int index = new Random().nextInt(arrayOfAllOption.size());
		if(Integer.signum(index)==-1)
		{
			index = -index;
		}
		selectedOption=arrayOfAllOption.get(index);
		System.out.println("Selected Option Is---->"+selectedOption);
		return selectedOption;
	}

	public static void selectFromComboByVisibleElement(WebElement element,String value)
	{
		Select select = new Select(element);
		select.selectByVisibleText(value);
	}

	public void getTheListOfAllSelectedOptions(WebElement element) {
		try {
			Select se=new Select(element);
			List<WebElement> allSelectedOptions = se.getAllSelectedOptions();
			for (WebElement webElement : allSelectedOptions)
			{
				WeboAutomation.extent.log(LogStatus.INFO, "Option selected ::"+ webElement.getText());
			}
		}catch(Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to fetch the list of selected options.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	//This method is for auto-complete selection using text 
	public void selectOptionWithText(WebElement autoOptions,String textToSelect) {
		try {
			WebDriverWait wait=new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.visibilityOf(autoOptions));

			List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName("li"));
			for(WebElement option : optionsToSelect){
				if(option.getText().equals(textToSelect)) {
					System.out.println("Trying to select: "+textToSelect);
					WeboAutomation.extent.log(LogStatus.INFO, "Option selected ::"+ textToSelect);
					option.click();
					break;
				}
			} 
		} catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to select text using autocomplete from list.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	//This method is for auto-complete selection using index 
	public void selectOptionWithIndex(WebElement autoOptions,int indexToSelect) {

		try {
			WebDriverWait wait=new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.visibilityOf(autoOptions));

			List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName("li"));
			if(indexToSelect<=optionsToSelect.size()) {
				System.out.println("Trying to select based on index: "+indexToSelect);
				WeboAutomation.extent.log(LogStatus.INFO, "Option selected using index::"+ indexToSelect);
				optionsToSelect.get(indexToSelect).click();
			}
		} 	catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to select text using autocomplete from list.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}

	//This method is to select a date from calender pop-up
	public void selectDateFromCalender(WebElement dateToSelect,String date) {

		try {
			WebElement datepicker = driver.findElement(By.id("ui-datepicker-div"));
			List<WebElement> rows=datepicker.findElements(By.tagName("tr"));  
			List<WebElement> columns=datepicker.findElements(By.tagName("td")); 
			for (WebElement cell: columns){  
				if (cell.getText().equals(date)){  
					cell.findElement(By.linkText(date)).click();  
					break;  
				}  
			}

		} 	catch (Exception e) {
			WeboAutomation.extent.log(LogStatus.ERROR, "Failed to select text using autocomplete from list.");
			WeboAutomation.extent.log(LogStatus.INFO, "<pre>"+ e.toString() +"</pre>");
			takeScreenShot();
			e.printStackTrace();
		}
	}


}
