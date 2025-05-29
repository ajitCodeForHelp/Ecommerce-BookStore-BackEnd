package com.bt.ecommerce.primary.pojo.enums;

public enum PaymentStatusEnum {
    PENDING("PENDING"),
    FAILED("FAILED"),
    PAID("PAID"),
    SUCCESS("SUCCESS"),

    Refunded("Refunded"),

    Partial_Refunded("Partial_Refunded"),

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
