package com.bt.ecommerce.primary.pojo.enums;

public enum ClientStatusEnum {

    Active("Active"),
    Inactive("Inactive"),
    ;

    String type;

    ClientStatusEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}