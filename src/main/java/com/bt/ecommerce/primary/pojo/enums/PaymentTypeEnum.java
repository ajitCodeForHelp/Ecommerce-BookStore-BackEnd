package com.bt.ecommerce.primary.pojo.enums;

public enum PaymentTypeEnum {
    COD("COD"),
    UPI("UPI"),
    ONLINE("ONLINE"),
    ;
    String type;

    PaymentTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
