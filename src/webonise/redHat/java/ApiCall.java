package webonise.redHat.java;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ApiCall {
	static String userFirstName,userLastName,userEmail,userPhone,socialLinks,userID,access_token,company_name,Phone;
	static JSONObject preferences;
	
	public static String apiCall(String username,String password) throws IOException, InterruptedException, JSONException{
		try{
			String chromeDriverPath=new File(".").getCanonicalPath()+"/resources/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		
		String crxPath=new File(".").getCanonicalPath()+"/resources/ModHeader.crx";
		ChromeOptions  options = new ChromeOptions();
		options.addExtensions(new File(crxPath));
		
		WebDriver driver = new ChromeDriver(options);
		
		driver.get("chrome-extension://idgpnmonknjnojddfkpgkljpfnnfcklj/icon.png");

		// setup ModHeader with two headers (token1 and token2)
		JavascriptExecutor js = (JavascriptExecutor) driver; 
		js.executeScript(
		    "localStorage.setItem('profiles', JSON.stringify([{                " +
		    "  title: 'Selenium', hideComment: true, appendMode: '',           " +
		    "  headers: [                                                      " +
		    "    {enabled: true, name: 'x-fh-auth-app', value: '574123cdff6a6d6cc3620ca2c0b14f33518b2575', comment: ''} " +
		  
		    "  ],                                                              " +
		    "  respHeaders: [],                                                " +
		    "  filters: []                                                     " +
		    "}]));                                                             " );

		driver.get("https://rhgpm-omnxxhjbwy2jba6mvnfaygv7-dev.mbaas1.eu.feedhenry.com/api/v1/oauth2/authenticate");
		
		WebDriverWait wait=new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//input[@id='username']")));
		WebElement usernameElement=driver.findElement(By.xpath("//input[@id='username']"));
		usernameElement.sendKeys(username);
		WebElement passwordElement=driver.findElement(By.xpath("//input[@id='password']"));
		passwordElement.sendKeys(password);
		WebElement loginButton=driver.findElement(By.xpath("//button[@type='submit']"));
		loginButton.click();
		
		String htmlData=driver.getPageSource();
		//System.out.println(htmlData);
		Document doc=Jsoup.parse(htmlData);
		String bodyText=doc.body().text();
		//System.out.println(bodyText);
		
		JSONObject jsonResponse = new JSONObject(bodyText);
		String accessToken=jsonResponse.getString("access_token");
		//System.out.println(accessToken);
		
		JSONObject userData=jsonResponse.getJSONObject("details");
		
		userFirstName=userData.getString("FirstName");
		userLastName=userData.getString("LastName");
		userEmail=userData.getString("Email");
		//userPhone=userData.getString("Phone");
		userID=jsonResponse.getString("_id");
		
		JSONArray socialLinksArray=jsonResponse.getJSONArray("social_links");
		for(int allLinks=0;allLinks<socialLinksArray.length();allLinks++){
			if(allLinks==0)
				socialLinks=(String) socialLinksArray.get(allLinks);
			else
				socialLinks+=","+(String) socialLinksArray.get(allLinks);
		}
		
		preferences=jsonResponse.getJSONObject("preferences");
		driver.close();
		
		return accessToken;
		}
		catch(Exception e){
			System.out.println(e);
			return "";
		}
	}
	
	///////////////////////////////////////////////
	public static String apiCall_forboarding(String username,String password) throws IOException, InterruptedException, JSONException{
		try{
			String chromeDriverPath=new File(".").getCanonicalPath()+"/resources/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		
		String crxPath=new File(".").getCanonicalPath()+"/resources/ModHeader.crx";
		ChromeOptions  options = new ChromeOptions();
		options.addExtensions(new File(crxPath));
		
		WebDriver driver = new ChromeDriver(options);
		
		driver.get("chrome-extension://idgpnmonknjnojddfkpgkljpfnnfcklj/icon.png");

		// setup ModHeader with two headers (token1 and token2)
		JavascriptExecutor js = (JavascriptExecutor) driver; 
		js.executeScript(
		    "localStorage.setItem('profiles', JSON.stringify([{                " +
		    "  title: 'Selenium', hideComment: true, appendMode: '',           " +
		    "  headers: [                                                      " +
		    "    {enabled: true, name: 'x-fh-auth-app', value: '574123cdff6a6d6cc3620ca2c0b14f33518b2575', comment: ''} " +
		  
		    "  ],                                                              " +
		    "  respHeaders: [],                                                " +
		    "  filters: []                                                     " +
		    "}]));                                                             " );

		driver.get("https://rhgpm-omnxxhjbwy2jba6mvnfaygv7-dev.mbaas1.eu.feedhenry.com/api/v1/oauth2/authenticate");
		
		WebDriverWait wait=new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//input[@id='username']")));
		WebElement usernameElement=driver.findElement(By.xpath("//input[@id='username']"));
		usernameElement.sendKeys(username);
		WebElement passwordElement=driver.findElement(By.xpath("//input[@id='password']"));
		passwordElement.sendKeys(password);
		WebElement loginButton=driver.findElement(By.xpath("//button[@type='submit']"));
		loginButton.click();
		
		String htmlData=driver.getPageSource();
		//System.out.println(htmlData);
		Document doc=Jsoup.parse(htmlData);
		String bodyText=doc.body().text();
		System.out.println("###"+bodyText);
		
		JSONObject jsonResponse = new JSONObject(bodyText);
		String accessToken=jsonResponse.getString("access_token");
		//System.out.println(accessToken);
		
		JSONObject userData=jsonResponse.getJSONObject("details");
		
		userFirstName=userData.getString("FirstName");
		userLastName=userData.getString("LastName");
		userEmail=userData.getString("Email");
		//userPhone=userData.getString("Phone");
		userID=jsonResponse.getString("_id");
		//jsonResponse.toJSONArray(names)
		
		driver.close();
		
		return accessToken;
		}
		catch(Exception e){
			System.out.println(e);
			return "";
		}
	}
	
	
///////////////////////////////////////////////
public static JSONObject apiCall_forboarding_details(String username,String password) throws IOException, InterruptedException, JSONException{
try{
String chromeDriverPath=new File(".").getCanonicalPath()+"/resources/chromedriver.exe";
System.setProperty("webdriver.chrome.driver", chromeDriverPath);

String crxPath=new File(".").getCanonicalPath()+"/resources/ModHeader.crx";
ChromeOptions  options = new ChromeOptions();
options.addExtensions(new File(crxPath));

WebDriver driver = new ChromeDriver(options);

driver.get("chrome-extension://idgpnmonknjnojddfkpgkljpfnnfcklj/icon.png");

// setup ModHeader with two headers (token1 and token2)
JavascriptExecutor js = (JavascriptExecutor) driver; 
js.executeScript(
"localStorage.setItem('profiles', JSON.stringify([{                " +
"  title: 'Selenium', hideComment: true, appendMode: '',           " +
"  headers: [                                                      " +
"    {enabled: true, name: 'x-fh-auth-app', value: '574123cdff6a6d6cc3620ca2c0b14f33518b2575', comment: ''} " +

"  ],                                                              " +
"  respHeaders: [],                                                " +
"  filters: []                                                     " +
"}]));                                                             " );

driver.get("https://rhgpm-omnxxhjbwy2jba6mvnfaygv7-dev.mbaas1.eu.feedhenry.com/api/v1/oauth2/authenticate");


WebDriverWait wait=new WebDriverWait(driver, 10);
wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//input[@id='username']")));
WebElement usernameElement=driver.findElement(By.xpath("//input[@id='username']"));
usernameElement.sendKeys(username);
WebElement passwordElement=driver.findElement(By.xpath("//input[@id='password']"));
passwordElement.sendKeys(password);
WebElement loginButton=driver.findElement(By.xpath("//button[@type='submit']"));
loginButton.click();


String htmlData=driver.getPageSource();
//System.out.println(htmlData);
Document doc=Jsoup.parse(htmlData);
String bodyText=doc.body().text();
System.out.println("###"+bodyText);

JSONObject jsonResponse = new JSONObject(bodyText);
access_token=jsonResponse.getString("access_token");
//System.out.println(accessToken);

JSONObject userData=jsonResponse.getJSONObject("details");

userFirstName=userData.getString("FirstName");
userLastName=userData.getString("LastName");
userEmail=userData.getString("Email");
Phone=userData.getString("Phone");
userID=jsonResponse.getString("_id");
company_name=userData.getString("companyName");
//jsonResponse.toJSONArray(names)

driver.close();

return userData;
}
catch(Exception e){
System.out.println(e);
JSONObject jsonResponse = new JSONObject();
return jsonResponse;
}
}
	/*public static void main(String[] args) throws IOException, InterruptedException, JSONException{
		apiCall("yetopensrl","redhat");
	}*/
}
