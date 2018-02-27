package webonise.redHat.constants;

public class RH_Connections_Constants {
	
	public static String connectionsHeader = "xpath#//android.widget.TextView[@text='CONNECTIONS']"; 
	public static String connectionList = "xpath#//android.support.v7.widget.RecyclerView/android.widget.FrameLayout";
	public static String connectionListString = "xpath#//android.support.v7.widget.RecyclerView/android.widget.FrameLayout";
	public static String nameListString="xpath#//android.support.v7.widget.RecyclerView/android.widget.FrameLayout/android.widget.LinearLayout[@index='0']//android.widget.LinearLayout[@index='1']/android.widget.TextView[@resource-id='com.redhat.partnerlink:id/txtName']";
	public static String nameList = "id#com.redhat.partnerlink:id/txtName";
	public static String backToAdvancedSearchPage = "xpath#//android.widget.ImageButton[@index='0']";
	public static String loaderLogo="xpath#//android.widget.ProgressBar[@index='0']";
	
	//Check in Carousel view
	public static String partnerName="id#com.redhat.partnerlink:id/txtPartnerName";
	public static String partnerEmail="id#com.redhat.partnerlink:id/txtPartnerEmail";
	public static String partnerContact="id#com.redhat.partnerlink:id/txtPartnerContact";
	public static String aboutMe="id#com.redhat.partnerlink:id/txtAboutMe";
	public static String socialLinkTitle="id#com.redhat.partnerlink:id/txtSocialLinksTitle";
	public static String socialLinkText="xpath#//android.widget.LinearLayout[@resource-id='com.redhat.partnerlink:id/llSocialLinkLayout']/android.widget.TextView[@index='1']";
	public static String matchingCriteria="id#com.redhat.partnerlink:id/txtMatchingCriteriaTitle";
	public static String matchingCriteriaText="xpath#//android.view.ViewGroup[@resource-id='com.redhat.partnerlink:id/plTagLayout']/android.widget.TextView";
	public static String industryTitle="id#com.redhat.partnerlink:id/txtIndustryFocusTitle";
	public static String industryText="id#com.redhat.partnerlink:id/txtIndustryFocus";
	public static String areaOfFocusTitle="id#com.redhat.partnerlink:id/txtAreaOfFocusTitle";
	public static String areaOfFocusText="id#com.redhat.partnerlink:id/txtAreaOfFocus";
	public static String productexpertise="id#com.redhat.partnerlink:id/txtRedHatProductExpertiseTitle";
	public static String productExpertiseText="id#com.redhat.partnerlink:id/txtRedHatProductExpertise";
	public static String locationTitle="id#com.redhat.partnerlink:id/txtLocationsTitle";
	public static String locationText="id#com.redhat.partnerlink:id/txtLocations";
	public static String accredationTitle="id#com.redhat.partnerlink:id/txtAccreditationsTitle";
	public static String accredationTetx="id#com.redhat.partnerlink:id/txtAccreditations";
}
