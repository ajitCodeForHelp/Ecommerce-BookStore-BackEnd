package com.bt.ecommerce.primary.pojo.enums;

public enum DiscountTypeEnum {
    ValueDiscount("ValueDiscount"),
    PercentageDiscount("PercentageDiscount"),
    FreeItemDiscount("FreeItemDiscount"),
    DeliveryFeeDiscount("DeliveryFeeDiscount"),
    ;

    String type;

    DiscountTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
