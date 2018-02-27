package webonise.redHat.constants;


public class RH_Common_Constants {
	
	public static String userNameTextbox = "xpath#//android.widget.EditText[@index='1']"; 
	public static String passwordTextbox = "xpath#//android.widget.EditText[@index='3']"; 
	public static String passwordHelpLink = "Password Help";
	public static String loginButton = "name#Log In";
	public static String loginButton2 = "xpath#//android.widget.Button[@index='0']";
	public static String progressBar="id#android:id/progress";
	public static String startProgressBar="id#com.redhat.partnerlink:id/pbLoader";
	public static String signInButton="id#com.redhat.partnerlink:id/btnSignIn";
	public static String sessionExpiry="xpath#//android.widget.Button[@text='Ok']";
	
	//Verification string constants  com.redhat.partnerlink:id/rbMenu
	public static String welcomeMessageTextOnLoginPage = "//android.view.View[@index='0']";
	public static String hamburgMenu = "xpath#//android.widget.ImageButton[@index='0']";
	public static String logoutMenu = "xpath#//android.widget.RadioButton[@text='Log out']";
	public static String cancelButton = "id#com.redhat.partnerlink:id/txtCancel";
	public static String dealMenu = "xpath#//android.widget.RadioButton[@text='Deal Registration']";
	public static String favoriteMenu = "xpath#//android.widget.RadioButton[@text='Favorites']";
	public static String searchMenu = "xpath#//android.widget.RadioButton[@text='Search']";
	
	//walkThrough
	public static String startButton = "id#com.redhat.partnerlink:id/btnStart";
	public static String nextButton = "id#com.redhat.partnerlink:id/btnNext";
	public static String textTitle = "xpath#//android.widget.TextView[@resource-id='com.redhat.partnerlink:id/txtTitle' and @index='0']";
	public static String textTitleMessage = "xpath#//android.widget.TextView[@resource-id='com.redhat.partnerlink:id/txtMessage' and @index='1']";	
}
