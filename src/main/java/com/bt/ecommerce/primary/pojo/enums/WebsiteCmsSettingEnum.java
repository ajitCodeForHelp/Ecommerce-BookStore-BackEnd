package com.bt.ecommerce.primary.pojo.enums;

public enum WebsiteCmsSettingEnum {

    PrivacyPolicy("PrivacyPolicy"),
    TermsAndCondition("TermsAndCondition"),
    AboutUs("AboutUs"),
    ContactUs("ContactUs"),
    ReturnAndReplacement("ReturnAndReplacement"),
    RefundPolicy("RefundPolicy"),
    TrackOrder("TrackOrder"),
    ShippingPolicy("ShippingPolicy"),
    CancellationPolicy("CancellationPolicy"),

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