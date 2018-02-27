package webonise.redHat.constants;

public class RH_OnBoarding_Constants {

	public static String confirmYourPartnerInfoHeader = "id#com.redhat.partnerlink:id/txtStepTitle";
	public static String partnerLabels = "id#com.redhat.partnerlink:id/txtLable";
	public static String partnerinfovalues = "id#com.redhat.partnerlink:id/txtValue";
	public static String continueButton = "id#com.redhat.partnerlink:id/btnContinue";
	public static String hidePhoneNumber = "xpath#//android.widget.TextView[@text='Phone']/following-sibling::android.widget.CheckBox[@text='HIDE']";
	public static String hideEmail = "xpath#//android.widget.TextView[@text='Email']/following-sibling::android.widget.CheckBox[@text='HIDE']";
	
	//Location
	public static String location = "id#com.redhat.partnerlink:id/cbLocation";
	public static String streetAddress = "id#com.redhat.partnerlink:id/edtStreetAddress";
	public static String countryDropDown = "id#com.redhat.partnerlink:id/spinnerCountry";
	public static String countrySelect="xpath#//android.widget.TextView[@text='*']";
	public static String state = "id#com.redhat.partnerlink:id/spinnerState";
	public static String stateSelect="xpath#//android.widget.TextView[@text='*']";
	public static String city = "id#com.redhat.partnerlink:id/edtCity";
	public static String postalCode = "id#com.redhat.partnerlink:id/edtPostalCode";
	
	//Industry
	public static String industry = "xpath#//android.widget.CheckBox[@text='Industry']";
	public static String industryToSelect = "xpath#//android.widget.CheckBox[@text='*']";
	
	//Area Of Focus
	public static String areaOfFocus = "xpath#//android.widget.CheckBox[@text='Area of Focus']";
	public static String areaOfFocusToSelect = "xpath#//android.widget.CheckBox[@text='*']";
	
	public static String redHatProductExpertise = "xpath#//android.widget.CheckBox[@text='Red Hat Product Expertise']";
	public static String redHatExpertiseToSelect = "xpath#//android.widget.CheckBox[@text='*']";
	public static String selectInnerProduct = "xpath#//android.widget.CheckBox[@text='*']";
	public static String doneButton = "id#com.redhat.partnerlink:id/btnDone";
	
	public static String socialLinks = "id#com.redhat.partnerlink:id/edtUrls";
	public static String aboutYou = "id#com.redhat.partnerlink:id/edtAboutSelf";
	
	//Preferences
	public static String preferencesHeader = "id#com.redhat.partnerlink:id/txtStepTitle";
	public static String notificationSettings = "xpath#//android.widget.CheckBox[@text='*']";
	
}
