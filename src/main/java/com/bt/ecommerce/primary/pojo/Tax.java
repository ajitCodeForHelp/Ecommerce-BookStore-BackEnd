package com.bt.ecommerce.primary.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(value = "tax")
public class Tax extends _BasicEntity{
    private String title;
    private Double percentage = 0.0;
}
