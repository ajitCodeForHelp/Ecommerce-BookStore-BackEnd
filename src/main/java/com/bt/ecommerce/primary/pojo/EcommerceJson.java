package com.bt.ecommerce.primary.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(value = "ecommerce_json")
public class EcommerceJson extends _BasicEntity {

    private String key;
    private String value;
}
