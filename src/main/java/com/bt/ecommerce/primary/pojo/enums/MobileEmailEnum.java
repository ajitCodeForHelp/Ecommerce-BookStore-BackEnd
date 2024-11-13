package com.bt.ecommerce.primary.pojo.enums;

public enum MobileEmailEnum {
    Mobile("Mobile"),
    Email("Email"),
    ;

    String type;

    MobileEmailEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }


}
