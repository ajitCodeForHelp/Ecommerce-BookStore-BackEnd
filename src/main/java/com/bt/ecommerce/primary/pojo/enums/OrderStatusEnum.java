package com.bt.ecommerce.primary.pojo.enums;

public enum OrderStatusEnum {
    ORDER("ORDER"),
    DISPATCHED("DISPATCHED"),
    DELIVERED("DELIVERED"),

    CANCELLED("CANCELLED"),


    PARTIAL_REFUNDED("PARTIAL_REFUNDED"),

    REFUND("REFUND"),

    MESSAGE_SENT("MESSAGE_SENT"),

    EMAIL_SENT("EMAIL_SENT"),

    TRACKING_DETAIL_UPDATED("TRACKING_DETAIL_UPDATED"),

    ;
    String type;

    OrderStatusEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
