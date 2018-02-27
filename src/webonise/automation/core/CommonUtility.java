package webonise.automation.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;

public class CommonUtility 
{
	public static CommonUtility commonUtility = new CommonUtility();
	public static String dateFormat(String use)
	{	
		String formattedDate=null;
		DateFormat dateFormat;
		Date date;
		switch(use)
		{
			case "username":	
				dateFormat = new SimpleDateFormat("dd_HH_mm_ss");
				date = new Date();
				formattedDate = dateFormat.format(date);
				break;
		
			case "ss":	
				dateFormat = new SimpleDateFormat("EEE_MMM_d_yyyy_HH_mm_ss");
				date= new Date();
				formattedDate = dateFormat.format(date);
				break;	
				
			case "date":	
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				date= new Date();
				formattedDate = dateFormat.format(date);
				break;	
		}
		return formattedDate;
	}
	
	public static String getEmailAddress()
	{
		String emailAddress;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
		emailAddress = "weboniseQA" + sdf.format(date) + "@weboapps.com";
		return emailAddress;
	}
	
	public static String getPassword()
	{
		String password;
		password = "weboniseQA-" + randomNumericValueGenerate(99999);
		return password;
	}
	
	public static String getNames()
	{
		String name;
		name = "webonise" + RandomStringUtils.random(5, "abcdefghijklmnopqrstuvwxyz");
		return name;
	}
	
	public static String getAddress()
	{
		String name;
		name = "Weboniselab," + RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz")+","+RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz");
		return name;
	}
	
	public static int randomNumericValueGenerate(int length) {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(length);
		return randomInt;
	}
	
	public static String generateRandomChars(int length) {
		String random=RandomStringUtils.random(length);
		return random;
	}
	
	public static String getRandomNumber(int digCount) {
		Random rnd = new Random();
	    StringBuilder sb = new StringBuilder(digCount);
	    for(int i=0; i < digCount; i++)
	        sb.append((char)('0' + rnd.nextInt(10)));
	    return sb.toString();
	}
	
	public static String[] wrongEmailInputs() {
		String wrongInputEmails[] ={"T3$t%%gmail.com"," email.test@gmail.com","test.mail","test email@gmail.com","testmailgmail.com","@gmail.com","testmail@@gmail.com",/*"test.mail@-gmail.com","#$%^%^..&%#^&*@gmail.com",*/"test.mail@gmail..com"};
		return wrongInputEmails;
	}
	
}
