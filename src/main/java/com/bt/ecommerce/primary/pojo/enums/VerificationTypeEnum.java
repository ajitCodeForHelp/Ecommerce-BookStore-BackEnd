package com.bt.ecommerce.primary.pojo.enums;

public enum VerificationTypeEnum {
    Login("Login"),
    ForgotPassword("ForgotPassword"),
    ;

    String type;

    VerificationTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }


}
