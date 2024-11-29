package com.bt.ecommerce.primary.pojo.enums;

public enum PaymentGatewayStatusEnum {
    paid("paid"),
    cancelled("cancelled"),
    expired("expired"),
    Refunded("Refunded"),
    created("created")
    ;
    String type;

    PaymentGatewayStatusEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
