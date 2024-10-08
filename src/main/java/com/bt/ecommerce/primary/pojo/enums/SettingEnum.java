package com.bt.ecommerce.primary.pojo.enums;


public enum SettingEnum {
    NotificationEmailTo("NotificationEmailTo");


    String type;

    SettingEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

//    public static List<ClientSettingEnum> getNumericKeyList() {
//        List<ClientSettingEnum> list = new ArrayList<>();
////        list.add(VersionCode);
////        list.add(AndroidVersionCode);
//
//        return list;
//    }
}