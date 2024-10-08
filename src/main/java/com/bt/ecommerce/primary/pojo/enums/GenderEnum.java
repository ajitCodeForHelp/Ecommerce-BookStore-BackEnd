package com.bt.ecommerce.primary.pojo.enums;

public enum GenderEnum {
    Male("Male"),
    Female("Female"),
    Other("Other"),
    ;

    String type;

    GenderEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
