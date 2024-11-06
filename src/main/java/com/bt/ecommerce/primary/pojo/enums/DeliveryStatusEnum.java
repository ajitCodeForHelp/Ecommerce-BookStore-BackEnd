package com.bt.ecommerce.primary.pojo.enums;

public enum DeliveryStatusEnum {
    ORDER("ORDER"),
    DISPATCHED("DISPATCHED"),
    DELIVERED("DELIVERED"),
    ;
    String type;

    DeliveryStatusEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
