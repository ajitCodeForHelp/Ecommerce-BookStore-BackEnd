package com.bt.ecommerce.primary.pojo.enums;

public enum DiscountTypeEnum {
    Amount("Amount"),
    Percentage("Percentage"),

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
