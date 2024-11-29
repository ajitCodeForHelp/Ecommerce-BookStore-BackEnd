package com.bt.ecommerce.primary.pojo.enums;

public enum PaymentGateWayEnum {
    RazorPay("RazorPay"),

    ;

    String type;

    PaymentGateWayEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}