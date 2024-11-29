package com.bt.ecommerce.primary.pojo.enums;

public enum OrderStatusEnum {
    ORDER("ORDER"),
    DISPATCHED("DISPATCHED"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED"),
    REFUND("REFUND"),
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
