package com.bt.ecommerce.utils;

import com.bt.ecommerce.primary.pojo.enums.SettingEnum;
import com.bt.ecommerce.primary.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("constants")
public class Const {
    @Autowired
    private SettingService settingService;
    public static final boolean DEVELOPMENT = true;
    @PostConstruct
    public void postConstructInitialization() {
        refreshSettingVariables();
    }

    public void refreshSettingVariables() {
//        refreshSmsSettingVariable();
//        refreshEmailSetting();
//        refreshAmazonS3Bucket();
        refreshSystemSetting();
        refreshStaticContentCMSVariables();
        refreshSystemAdminSetting();
        refreshWebSiteCMSVariables();
    }


    public void refreshSystemSetting() {
//        SystemSetting.ContactUsQueryToEmail = settingService.getSettingValueByKey(SettingEnum.ContactUsQueryToEmail);
        SystemSetting.TestMode = settingService.getSettingValueByKey(SettingEnum.TestMode).equalsIgnoreCase("1");
        SystemSetting.BaseUrl = settingService.getSettingValueByKey(SettingEnum.BaseUrl);
        SystemSetting.NotificationMailToCompany = settingService.getSettingValueByKey(SettingEnum.NotificationEmailTo);
//        refreshPrivatePublicKey();
    }

    public void refreshSystemAdminSetting() {
//        SystemAdminSetting.SESEmail = settingService.getSettingValueByKey(SettingKeysEnum.SESEmail);
//        SystemAdminSetting.SESHost = settingService.getSettingValueByKey(SettingKeysEnum.SESHost);
//        SystemAdminSetting.SESPort = settingService.getSettingValueByKey(SettingKeysEnum.SESPort);
//        SystemAdminSetting.SESEmailSender = settingService.getSettingValueByKey(SettingKeysEnum.SESEmailSender);
//        SystemAdminSetting.SESAccessKey = settingService.getSettingValueByKey(SettingKeysEnum.SESAccessKey);
//        SystemAdminSetting.SESSecretKey = settingService.getSettingValueByKey(SettingKeysEnum.SESSecretKey);
    }

//    public void refreshPrivatePublicKey() {
//        SystemSetting.PublicKey = settingService.getSettingValueByKey(SettingEnum.PublicKey);
//        SystemSetting.PrivateKey = settingService.getSettingValueByKey(SettingKeysEnum.PrivateKey);
//    }

//    public void refreshAmazonS3Bucket() {
//        AmazonS3Bucket.S3AccessKey = settingService.getSettingValueByKey(SettingKeysEnum.S3AccessKey);
//        AmazonS3Bucket.S3SecretKey = settingService.getSettingValueByKey(SettingKeysEnum.S3SecretKey);
//        AmazonS3Bucket.S3Bucket = settingService.getSettingValueByKey(SettingKeysEnum.S3Bucket);
//        AmazonS3Bucket.S3BucketBaseURL = settingService.getSettingValueByKey(SettingKeysEnum.S3BucketBaseURL);
//        AmazonS3Bucket.refresh();
//    }

    public void refreshWebSiteCMSVariables() {
//        WebSiteCMS.CompanyLogoDark = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.CompanyLogoDark);
//        WebSiteCMS.FaviconIcon = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.FaviconIcon);
//        WebSiteCMS.CompanyName = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.CompanyName);

//        WebSiteCMS.CompanyLogoLight = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.CompanyLogoLight);
//        WebSiteCMS.CompanyAddress = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.CompanyAddress);
//        WebSiteCMS.CompanyContactNumber = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.CompanyContactNumber);
//        WebSiteCMS.CompanyContactEmail = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.CompanyContactEmail);
    }

    public static class SystemSetting {
        public static String ContactUsQueryToEmail;

        public static String AnonymousCustomer;

        public static String AndroidAppUrl;
        public static String IosAppUrl;

        public static String
                FcmAuthKey;
        public static String CustomerAndroidAppVersion;
        public static String CustomerIosAppVersion;
        public static String DriverAndroidAppVersion;
        public static String PosAndroidAppVersion;

        public static boolean UnderMaintenance;
        public static boolean AdminLogin;
        public static boolean CustomerLogin;
        public static boolean DriverModuleAccess;
        public static boolean DriverLogin;
        public static boolean LimitDeliveryArea;
        public static boolean LalaMoveDelivery;
        public static boolean AutoOrderRouting;
        public static boolean TestMode;
        public static boolean DebugMode;
        public static boolean SkipUser;

        public static String GoogleMapApiKey;

        public static String PublicKey;
        public static String PrivateKey;

        public static String BaseUrl;
        public static String CurrencySymbol;
        public static String CurrencyCode;

        public static String NotificationMailToCompany;
        public static String CleverTapAccountId;
        public static String CleverTapPassCode;
        public static String FacebookAccessToken;
        public static String FacebookApplicationId;

        public static String OtpSignature;
        public static String GrcOtpSignature;
        public static String PosOtpSignature;
        public static String Theme;

        public static boolean DineIn;
        public static boolean PostPaidDineIn;
        public static boolean TakeAway;
        public static boolean HomeDelivery;
        public static boolean StealDeal;
        public static boolean TableReservation;
        public static boolean Event;
        public static boolean Voucher;
        public static boolean RewardDineInCashBack;
        public static boolean RewardQrCode;
        public static boolean ATMBar;
        public static boolean RFID;
        public static boolean TOTPEnable;
        public static boolean Reports;
        public static boolean Offers;
        public static boolean AdminCanAddMenu;
        public static boolean RestaurantCanAddMenu;
    }

    public static class SystemAdminSetting {
        public static String ConsumeAfter;
        public static String ConsumeExpires;
        public static Double PackingChargesForDelivery;
        public static Double PackingChargesForTakeAway;
        public static String CustomerCareNumber;
        public static String TimeBasedOTPSecretKey;

        public static String SESEmail;
        public static String SESHost;
        public static String SESPort;
        public static String SESEmailSender;
        public static String SESAccessKey;
        public static String SESSecretKey;
    }

    public static class WebSiteCMS {
        public static String AboutApp;
        public static String TermsAndCondition;
        public static String HelpAndSupport;

        public static String CompanyLogoDark;
        public static String FaviconIcon;
        public static String CompanyName;
        public static String CompanyLogoLight;
        public static String CompanyAddress;
        public static String CompanyAddressLatitude;
        public static String CompanyAddressLongitude;
        public static String CompanyContactNumber;
        public static String CompanyContactEmail;
        public static String SplashGif;

        public static String Instagram;
        public static String GPlus;
        public static String Pinterest;
        public static String Facebook;
        public static String Twitter;
        public static String Youtube;
        public static String Tumblr;
    }

    public void refreshStaticContentCMSVariables() {
//        WebSiteCMS.AboutApp = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.AboutApp);
//        WebSiteCMS.TermsAndCondition = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.TermsAndCondition);
//        WebSiteCMS.HelpAndSupport = webSiteCMSService.getWebSiteCMSValueByKey(WebSiteCMSKeysEnum.HelpAndSupport);
    }

    public static class AmazonS3Bucket {
        public static String S3AccessKey;
        public static String S3SecretKey;
        public static String S3Bucket;
        public static String S3BucketBaseURL;

        public static String BUCKET_File_Folder;
        public static String BUCKET_Primary_Image_Folder;
        public static String BUCKET_Thumbnail_Image_Folder;

        public static String BUCKET_File_Folder_URL;
        public static String BUCKET_Primary_Image_Folder_URL;
        public static String BUCKET_Thumbnail_Image_Folder_URL;

        public static void refresh() {
            BUCKET_File_Folder = S3Bucket + "/" + "files";
            BUCKET_Primary_Image_Folder = S3Bucket + "/" + "primary-image";
            BUCKET_Thumbnail_Image_Folder = S3Bucket + "/" + "thumbnail-image";

            BUCKET_File_Folder_URL = S3BucketBaseURL + "/" + BUCKET_File_Folder;
            BUCKET_Primary_Image_Folder_URL = S3BucketBaseURL + "/" + BUCKET_Primary_Image_Folder;
            BUCKET_Thumbnail_Image_Folder_URL = S3BucketBaseURL + "/" + BUCKET_Thumbnail_Image_Folder;
        }

        public static String getSmallImageUrl(String fileName) {
            return BUCKET_Thumbnail_Image_Folder_URL + "/" + fileName;
        }

        public static String getLargeImageUrl(String fileName) {
            return BUCKET_Primary_Image_Folder_URL + "/" + fileName;
        }

        public static String getFileUrl(String fileName) {
            return BUCKET_File_Folder_URL + "/" + fileName;
        }
    }

    public enum PaymentMode {
        Razorpay("Razorpay"),
        Paytm("Paytm"),
        CashOnDelivery("Cash On Delivery");

        String mode;

        PaymentMode(String pMode) {
            this.mode = pMode;
        }

        @Override
        public String toString() {
            return mode;
        }

        public String getMode() {
            return mode;
        }
    }

}
