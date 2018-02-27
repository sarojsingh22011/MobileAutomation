package webonise.automation.core;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

import atu.testrecorder.exceptions.ATUTestRecorderException;
import webonise.automation.core.verification.Verification;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class WeboAutomation {

	public WebDriver driver;
	public AndroidDriver<MobileElement> androidDriver;
	public String report_name;
	String dir;
	String dir1;

	public TestData testdata = new TestData();
	public Configuration config=new Configuration();
	public CommonUtility utils = new CommonUtility();
	public WeboActions weboActions;
	public Verification verify;
	public static ExtentReports extent = ExtentReports.get(WeboAutomation.class);
	public String methodName;
	static String filename;
	public String scriptName;
	public Date date = new Date();
	public String reportName= "";
	public HashMap<String,String> globalMap;
	public static int reportMethodInitializer = 1;
	//public AndroidDriver androidDriver;

	public void initalizeReport(String name) throws IOException
	{
		Configuration.initializeSettings();
		dir=Configuration.path+"/Report/";
		File f = new File(dir);
		FileUtils.deleteDirectory(f);
		f.mkdirs();
		if(Configuration.mobileAutomation.equalsIgnoreCase("yes")) {
			dir1=Configuration.path+"/ScriptVideos/";
			File f1 = new File(dir1);
			FileUtils.deleteDirectory(f1);
			f1.mkdirs();
		}
		reportName = dir+"Weboniselab-QA-Execution-Report.html";
		if(!dir.isEmpty()) {
			extent.init(reportName, true);
			extent.config().reportHeadline("Weboniselab QA Execution Report.");
			extent.config().documentTitle(name);
			extent.config().displayCallerClass(false);
		} else {
			System.err.println("Failed to initialize report");
		}

		weboActions = new WeboActions(utils,dir);	
		verify = new Verification(weboActions);
		globalMap=new HashMap<String,String>();
	}

	private void initializeLocalBrowser(String type) throws Exception {
		switch(type) {
		case "firefox":
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("dom.max_chrome_script_run_time", "999");
			profile.setPreference("dom.max_script_run_time", "999");
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.helperApps.neverAsk.openFile","text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk","text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
			profile.setPreference("browser.download.manager.showWhenStarting",false);
			profile.setPreference("browser.download,manager.focusWhenStarting",false);
			profile.setPreference("browser.helperApps.alwaysAsk.force", false);
			profile.setPreference("browser.download.manager.alertOnEXEOpen",false);
			profile.setPreference("browser.download.manager.closeWhenDone",false);
			profile.setPreference("browser.download.manager.showAlertOnComplete", false);
			profile.setPreference("browser.download.manager.useWindow", false);
			profile.setPreference("browser.download.manager.showWhenStarting",false);
			profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting",false);
			profile.setPreference("pdfjs.disabled", true);
			profile.setAcceptUntrustedCertificates(true);
			profile.setPreference("security.OCSP.enabled", 0);
			profile.setEnableNativeEvents(false);
			profile.setPreference("network.http.use-cache", false);
			profile.setPreference("gfx.direct2d.disabled", true);
			profile.setPreference("layers.acceleration.disabled", true);

			DesiredCapabilities capability = null;
			capability = DesiredCapabilities.firefox();
			capability.setCapability("marionette", true);
			//capability.setCapability(FirefoxDriver.PROFILE, profile);


			System.setProperty("webdriver.gecko.driver",Configuration.path+"/resources/geckodriver.exe");
			driver =new MarionetteDriver(capability);
			break;
		case "chrome":
			System.setProperty("webdriver.chrome.driver", Configuration.path+"/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		case "ie":
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer(); 
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			System.setProperty("webdriver.ie.driver",Configuration.path+"/resources/IEDriverServer.exe");
			driver = new InternetExplorerDriver(capabilities);
			break;
		case "safari":
			driver=new SafariDriver();
			break;
		default :
			type = "phantomJs";   
			break;
		}

		weboActions.setDriver(driver);       
	}

	public void closeBrowser()
	{
		driver.close();
	}

	//@BeforeMethod(alwaysRun = true)
	public void setUp(Method method) throws Exception {
		methodName=method.getName();
		extent.startTest(methodName);
		/*	if(reportMethodInitializer==1) {
			WeboAutomation.extent.log(LogStatus.INFO, "<br><center>"+"Script name:" +  scriptName+"</center></br>");
			WeboAutomation.extent.log(LogStatus.INFO, "<br></br>");
			reportMethodInitializer++;
			} */
		try{
			if (driver != null) {
				testdata.initialize(filename);
			} else if(Configuration.mobileAutomation.equalsIgnoreCase("yes")) {
				testdata.initialize(filename);
			} else {
				System.err.println("**********************************************************************Driver not initialized**********************************************************************");
			}
			weboActions.setTCNameToFile(method.getName());
			System.out.println("Method Name is : "+ method.getName());
			testdata.setTCNode(method.getName());
		}
		catch(NullPointerException e)
		{
			System.err.println("Driver not defined.Please define Driver in config file");
		}
	}

	@AfterMethod
	public static void TearDown() throws Exception {
		extent.endTest();
	}


	@AfterTest
	public void testEnd() throws InterruptedException, ATUTestRecorderException{
		testdata.hmap.clear();
		String css = ".exec-info { display: block; }";
		extent.config().addCustomStyles(css);
		Reporter.log("TestCase completed",true);
		extent.endTest();
		if(Configuration.mobileAutomation.equalsIgnoreCase("yes")) {
			MobileActions.stopRecording();
		} else {
			Thread.sleep(3000);
			driver.quit();
		}
		Thread.sleep(3000);
	}

	@AfterSuite
	public static void generateZIPFile(){
		try{
			File directoryToZip = new File(Configuration.path+"\\Report");
			List<File> fileList = new ArrayList<File>();
			getAllFiles(directoryToZip, fileList);
			writeZipFile(directoryToZip, fileList);
			System.out.println("---Zip file created.");
		}catch(Exception e) {
			System.out.println("Exception e:"+ e);
		}
	}

	public static void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileList.add(file);
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					System.out.println("file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeZipFile(File directoryToZip, List<File> fileList) {

		try {
			FileOutputStream fos = new FileOutputStream(Configuration.path+"//"+directoryToZip.getName() + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) { 
					addToZip(directoryToZip, file, zos);
				}
			}
			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,IOException {

		FileInputStream fis = new FileInputStream(file);
		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,file.getCanonicalPath().length());
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}
		zos.closeEntry();
		fis.close();
	}

	@BeforeTest
	public void testStart(){
		Reporter.log("TestCase Started",true);
		try {
			if(Configuration.mobileAutomation.equalsIgnoreCase("no")) { //selenium execution
				initializeLocalBrowser(Configuration.browser);
			} else { //mobile execution
				//Stop appium server first
				MobileActions.stopAppium();
				//Start appium server first
				MobileActions.startAppium();
			}
			//	objRepo.initialize(); Harshali
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public AndroidDriver<MobileElement> initializeMobileDrivers() throws MalformedURLException, ATUTestRecorderException {
		
		try {
			
			 
			Thread.sleep(2000);
		/*	File app = new File(System.getProperty("user.dir")+"\\App\\Android\\"+Configuration.androidApkFileName);
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "6.0.1");
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
			capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
			capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "200");*/

			//Emulator code
			File app = new File(System.getProperty("user.dir")+"\\App\\Android\\"+Configuration.androidApkFileName);
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Nexus_4_API_23");
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "6.0");
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
			capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
			capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "200");

			if(Configuration.fullReset.equalsIgnoreCase("false")){
				capabilities.setCapability("noReset","true");
			}
			
			try {
				androidDriver= new AndroidDriver (new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
				
				
			}catch(Exception e) {
				
				System.out.println(e.getMessage());
			}
			androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			MobileActions.startRecording();
		}catch(Exception e) {
			
			e.fillInStackTrace();
			
			e.printStackTrace();
			System.out.println("Problem is initializing mobile driver:"+e.getMessage());
		}
		return androidDriver;
	}

	public void updateTCData(Integer iteration){
		testdata.updateTCData(iteration);
	}

	public String getValue(String varName){
		return testdata.getValue(varName);
	}

	public Map getTestData(){
		return testdata.getHashMap();
	}

	@DataProvider(name = "xml")
	public Object[][] getTestData(Method method , ITestContext context) {
		try {
			filename = context.getSuite().getXmlSuite().getFileName();
			scriptName= context.getSuite().getName();
			setUp(method);
		} catch (Exception e) {
			System.err.println("setUp() failed");
			e.printStackTrace();
		}
		System.out.println("in WeboAutomation.GetTCData");
		try{  
			return testdata.getTCData();
		}

		catch(NullPointerException e)
		{
			System.err.println("getTCData() Failed");
			return null;
		}
	}


}