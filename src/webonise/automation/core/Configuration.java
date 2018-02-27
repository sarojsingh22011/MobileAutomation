package webonise.automation.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	
	public static String browser;
	public static String objRepo;
	public static String dataFile;
	public static String path;
	public static String chromedriver;
	public static String firefoxdriver;
	public static String iedriver;
	public static String log_file_dir;
	public static String mobileAutomation;
	private static String configPath = "/config.properties";
	static InputStream input = null;
	public static String pdf_converter_dir;
	public static String nodePath;
	public static String appiumJavaScriptPath;
	public static String androidApkFileName;
	public static int defaultGUIWaitPeriod;
	public static String restAPIVerification;
	public static String accessToken,apiTestURL,apiStageURL,fullReset;
	
	public static void initializeSettings() {
		try{
		String line = null;
		String[] keyValue = null;
		path = new File(".").getCanonicalPath().replace("\\", "/");
		FileReader fr = new FileReader(new File(path+configPath).getAbsoluteFile());;
		BufferedReader br = new BufferedReader(fr);
		while ((line = br.readLine()) != null) {
				keyValue = line.split("=");
				if(keyValue.length != 1) {
					keyValue[1] = keyValue[1].trim();
				} else {
					throw new Exception("The value for key='"+keyValue[0]+"' should not be blank.Please check config.properties file");
				}		
				switch(keyValue[0].toString()){
				case "browser": 
					browser = keyValue[1];
					System.out.println("browser:"+browser);
					  if (System.getenv("browser")!=null)
						   browser= System.getenv("browser");
						  System.out.println("************browser*********"+browser);
					break;
				case "mobile_automation":
					mobileAutomation = keyValue[1];
					System.out.println("mobileAutomation:"+mobileAutomation);
					break;
				case "nodePath":
					nodePath = keyValue[1];
					System.out.println("nodePath:"+nodePath);
					break;
				case "appiumJavaScriptPath":
					appiumJavaScriptPath = keyValue[1];
					System.out.println("appiumJavaScriptPath:"+appiumJavaScriptPath);
					break;
				case "androidApkFileName":
					androidApkFileName = keyValue[1];
					System.out.println("androidApkFileName:"+androidApkFileName);
					break;
				case "defaultGUIWaitPeriod":
					defaultGUIWaitPeriod = Integer.parseInt(keyValue[1]);
					System.out.println("defaultGUIWaitPeriod"+defaultGUIWaitPeriod);
					break;
			/*	case "restAPIVerification":
					restAPIVerification = keyValue[1];
					System.out.println("restAPIVerification"+restAPIVerification);
					break;
				case "accessToken":
					accessToken = keyValue[1];
					System.out.println("accessToken"+accessToken);
					break;*/
				case "ApiTest":
					apiTestURL = keyValue[1];
					System.out.println("apiTestURL "+defaultGUIWaitPeriod);
					break;
				case "ApiStage":
					apiStageURL = keyValue[1];
					System.out.println("apiStageURL "+defaultGUIWaitPeriod);
					break;
				case "full-reset":
					fullReset = keyValue[1];
					System.out.println("apiStageURL "+defaultGUIWaitPeriod);
					break;
				}
			}
		}catch(Exception e){
				System.out.println(e.getMessage());
			}
	}
}