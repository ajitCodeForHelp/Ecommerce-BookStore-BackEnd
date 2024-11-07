package com.bt.ecommerce.primary.pojo.enums;


public enum WebsiteCmsSettingEnum {
    PrivacyPolicy("PrivacyPolicy"),
    TermsAndCondition("TermsAndCondition"),
    ;
    String type;

    WebsiteCmsSettingEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}