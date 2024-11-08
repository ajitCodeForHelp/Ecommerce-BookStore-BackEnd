package com.bt.ecommerce.primary.userAccess.pojo.enums;

import java.util.ArrayList;
import java.util.List;

public enum AccessTypeEnum {

    CREATE("CREATE"), // C
    READ("READ"),     // R
    UPDATE("UPDATE"), // U
    DELETE("DELETE"), // D
    ;

    String type;

    AccessTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static List<AccessTypeEnum> getTypes() {
        List<AccessTypeEnum> list = new ArrayList<>();
        list.add(CREATE);
        list.add(READ);
        list.add(UPDATE);
        list.add(DELETE);
        return list;
    }
}