package com.bt.ecommerce.primary.pojo.enums;

public enum RoleEnum {
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SHOP_ADMIN("ROLE_SHOP_ADMIN"),
    ROLE_STAFF("ROLE_STAFF"),
    ROLE_CLIENT("ROLE_CLIENT"),
    ROLE_CLIENT_ADMIN("ROLE_CLIENT_ADMIN"),
    ROLE_RESTAURANT("ROLE_RESTAURANT"),
    ROLE_RESTAURANT_ADMIN("ROLE_RESTAURANT_ADMIN"),

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
