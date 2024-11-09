package com.bt.ecommerce.primary.userAccess.pojo.enums;

public enum MethodTypeEnum {

    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE"),
    OTHER("OTHER"),
    ;

    private String type;

    private MethodTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}