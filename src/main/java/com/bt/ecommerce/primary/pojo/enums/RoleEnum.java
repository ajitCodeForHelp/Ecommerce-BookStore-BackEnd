package com.bt.ecommerce.primary.pojo.enums;

public enum RoleEnum {
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUB_ADMIN("ROLE_SUB_ADMIN"),

    ROLE_CUSTOMER("ROLE_CUSTOMER"),
    ;
    String type;

    RoleEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
