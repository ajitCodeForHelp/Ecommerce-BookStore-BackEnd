package com.bt.ecommerce.primary.pojo.enums;

public enum PaymentTypeEnum {
    CASH_ON_DELIVERY("CASH_ON_DELIVERY"),
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
