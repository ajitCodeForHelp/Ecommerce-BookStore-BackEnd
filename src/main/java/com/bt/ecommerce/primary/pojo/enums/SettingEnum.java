package com.bt.ecommerce.primary.pojo.enums;


import java.util.ArrayList;
import java.util.List;

public enum SettingEnum {

    // Toggle Setting > Boolean
    TestMode("TestMode"),
//    AdminLogin("AdminLogin"),
    StaffLogin("StaffLogin"),
    UnderMaintenance("UnderMaintenance"),
    // Double Setting
    PackingWeight("PackingWeight"),

    CodCharges("CodCharges"),

    PackingCharges("PackingCharges"),
    AppVersion("AppVersion"),

    // String Setting
    BaseUrl("BaseUrl"),
    SupportEmail("SupportEmail"),
    SupportContactNo("SupportContactNo"),
    CompanyAddress("CompanyAddress"),
    HeaderAlertLine("HeaderAlertLine"),

    // DeliveryCharges Json >
    DeliveryChargeJson("DeliveryChargeJson"),

    CashOnDelivery("CashOnDelivery"),
    FacebookUrl("facebookUrl"),
    TwitterUrl("TwitterUrl"),
    YoutubeUrl("YoutubeUrl"),
    TelegramUrl ("TelegramUrl"),
    InstagramUrl("InstagramUrl");
    String type;

    SettingEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static List<SettingEnum> getToggleSettingsKey() {
        List<SettingEnum> settingList = new ArrayList<>();
        settingList.add(TestMode);
//        settingList.add(AdminLogin);
        settingList.add(StaffLogin);
        settingList.add(UnderMaintenance);
        settingList.add(CashOnDelivery);
        return settingList;
    }

    public static List<SettingEnum> getFloatSettingsKey() {
        List<SettingEnum> settingList = new ArrayList<>();
        settingList.add(AppVersion);
        settingList.add(PackingCharges);
        settingList.add(PackingWeight);
        settingList.add(CodCharges);
        return settingList;
    }

    public static List<SettingEnum> getStringSettingsKey() {
        List<SettingEnum> settingList = new ArrayList<>();
        settingList.add(BaseUrl);
        settingList.add(SupportContactNo);
        settingList.add(SupportEmail);
        settingList.add(CompanyAddress);
        settingList.add(HeaderAlertLine);
        settingList.add(FacebookUrl);
        settingList.add(InstagramUrl);
        settingList.add(TelegramUrl);
        settingList.add(YoutubeUrl);
        settingList.add(YoutubeUrl);
        return settingList;
    }

    // For calculation
    public static List<SettingEnum> getChargesList() {
        List<SettingEnum> settingList = new ArrayList<>();
        settingList.add(PackingCharges);
        settingList.add(PackingWeight);
        settingList.add(CodCharges);
        return settingList;
    }

}