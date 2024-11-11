package com.bt.ecommerce.primary.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(value = "dynamic_field")
public class DynamicField extends _BasicEntity{
    private String title;
    private String Type;
}
