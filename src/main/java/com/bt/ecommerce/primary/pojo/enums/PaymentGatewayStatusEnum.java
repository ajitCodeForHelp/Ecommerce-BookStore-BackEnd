package com.bt.ecommerce.primary.pojo.enums;

public enum PaymentGatewayStatusEnum {
    paid("paid"),
    cancelled("cancelled"),
    expired("expired"),
    Refunded("Refunded"),
    created("created"),
    opened("opened"),
    failed("failed"),
    under_verification("under_verification"),
    RefundPending("RefundPending"),
    RefundFailed("RefundFailed"),

    authorized("authorized"),
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
