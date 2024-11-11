package com.bt.ecommerce.primary.pojo.enums;


import java.util.ArrayList;
import java.util.List;

public enum SettingEnum {

    // Toggle Setting > Boolean
    TestMode("TestMode"),
    AdminLogin("AdminLogin"),
    StaffLogin("StaffLogin"),
    UnderMaintenance("UnderMaintenance"),

    // Double Setting
    AppVersion("AppVersion"),

    // String Setting
    BaseUrl("BaseUrl"),
    NotificationEmailTo("NotificationEmailTo"),

    // DeliveryCharges Json >
    DeliveryChargeJson("DeliveryChargeJson");

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
        settingList.add(AdminLogin);
        settingList.add(StaffLogin);
        settingList.add(UnderMaintenance);
        return settingList;
    }

    public static List<SettingEnum> getFloatSettingsKey() {
        List<SettingEnum> settingList = new ArrayList<>();
        settingList.add(AppVersion);
        return settingList;
    }

    public static List<SettingEnum> getStringSettingsKey() {
        List<SettingEnum> settingList = new ArrayList<>();
        settingList.add(BaseUrl);
        settingList.add(NotificationEmailTo);
        return settingList;
    }

}