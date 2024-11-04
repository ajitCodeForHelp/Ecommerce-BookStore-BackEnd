package com.bt.ecommerce.primary.pojo.enums;

public enum PaymentStatusEnum {
    CASH_ON_DELIVERY("CASH_ON_DELIVERY"),
    UPI("UPI"),
    ;
    String type;

    PaymentStatusEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
